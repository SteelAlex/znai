/*
 * Copyright 2021 znai maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.python;

import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.python.pydoc.ParsedPythonDoc;

public class PythonDocIncludePlugin extends PythonIncludePluginBase {
    @Override
    public String id() {
        return "python-doc";
    }

    @Override
    public IncludePlugin create() {
        return new PythonDocIncludePlugin();
    }

    @Override
    public PythonIncludeResult process(PythonCode parsed) {
        PythonCodeEntry codeEntry = findEntryByName(parsed, entryName);
        ParsedPythonDoc parsedPythonDoc = new ParsedPythonDoc(codeEntry.getDocString());
        return new PythonIncludeResult(
                componentsRegistry.markdownParser().parse(fullPath, parsedPythonDoc.getPyDocDescriptionOnly())
                        .getDocElement().getContent(),
                parsedPythonDoc.getPyDocDescriptionOnly());
    }
}
