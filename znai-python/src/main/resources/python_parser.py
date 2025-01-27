#  Copyright 2021 znai maintainers
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#  http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

import ast
import json
import sys
import traceback

content_lines = []
warnings = set([])


def read_and_parse(file_name):
    global content_lines
    with open(file_name) as file:
        content = file.read()
        content_lines = content.splitlines()
        warnings.clear()

    return ast.parse(content)


def node_to_dict(node_type, name_to_use, node, include_docstring=True):
    try:
        return {
            "type": node_type,
            "name": name_to_use,
            "content": extract_content(node_type, name_to_use, node),
            "body_only": extract_body_only(node_type, name_to_use, node),
            "doc_string": ast.get_docstring(node) if include_docstring else None,
        }
    except Exception as e:
        raise Exception(f"Error processing {name_to_use} {node_type}") from e


def function_to_dict(func_node):
    return node_to_dict("function", func_node.name, func_node)


def extract_content(node_type, name_to_use, node):
    if not hasattr(node, "end_lineno"):
        warnings.add(f"Skipping content extraction for {name_to_use} {node_type}, only supported with Python 3.8+")
        return None

    global content_lines
    return "\n".join(content_lines[(node.lineno - 1) : node.end_lineno])


def partition_assignment_first_line(assignment_node):
    first_line = content_lines[assignment_node.lineno - 1]
    name, _, first_line_content = first_line.partition("=")
    return name, first_line_content


def extract_assignment_value(assignment_node):
    _, first_line_content = partition_assignment_first_line(assignment_node)

    if assignment_node.lineno == assignment_node.end_lineno:
        return first_line_content.strip()

    return first_line_content.strip() + "\n" + "\n".join(
        content_lines[assignment_node.lineno : assignment_node.end_lineno])


def extract_body_only(node_type, name_to_use, node):
    if not hasattr(node, "end_lineno"):
        warnings.add(f"Skipping content extraction for {name_to_use} {node_type}, only supported with Python 3.8+")
        return None

    if node_type == "assignment":
        return extract_assignment_value(node)

    # skip py doc if present
    start_idx = 1 if is_py_doc(node.body[0]) else 0
    end_idx = len(node.body) - 1

    start_line_idx = node.body[start_idx].lineno - 1
    end_line_idx = node.body[end_idx].end_lineno - 1

    global content_lines
    return "\n".join(content_lines[start_line_idx : (end_line_idx + 1)])


def is_py_doc(node):
    if hasattr(node, "value") and isinstance(node.value, ast.Constant) and not hasattr(node, "targets") and not hasattr(node, "target"):
        return True

    return False


def class_to_list_of_dict(class_node):
    """
    flatten functions from class to put into the flat list alongside class definition
    :param class_node:
    :return:
    """
    return [node_to_dict("class", class_node.name, class_node)] + \
           [node_to_dict("function", class_node.name + "." + node.name, node) for node in
            class_node.body if isinstance(node, ast.FunctionDef)]


def parse_assignment(assignment_node):
    if len(assignment_node.targets) != 1:
        # Currently only support single variable assignment, i.e. no tuples, etc.
        return None

    name, _ = partition_assignment_first_line(assignment_node)
    return node_to_dict("assignment", name.strip(), assignment_node.value, include_docstring=False)


def parse_file(file_to_parse):
    module = read_and_parse(file_to_parse)

    function_definitions = [node for node in module.body if isinstance(node, ast.FunctionDef)]
    class_definitions = [node for node in module.body if isinstance(node, ast.ClassDef)]
    variable_assignments = [node for node in module.body if isinstance(node, ast.Assign)]

    parse_result = [function_to_dict(f) for f in function_definitions]

    for class_node in class_definitions:
        dicts = class_to_list_of_dict(class_node)
        for class_dict in dicts:
            parse_result.append(class_dict)

    for assignment_node in variable_assignments:
        assignment_dict = parse_assignment(assignment_node)
        if assignment_dict is not None:
            parse_result.append(assignment_dict)

    return parse_result


def print_parse_completed():
    print("---parse_completed---", flush=True)


while True:
    line = sys.stdin.readline()

    parse_result = None
    error = None
    try:
        parse_result = parse_file(line.strip())
    except:
        error = traceback.format_exc()

    success = error is None
    result = {
        "success": success,
        "warnings": list(warnings),
        "error": error,
        "result": parse_result
    }
    print(json.dumps(result), flush=True)
    print_parse_completed()
