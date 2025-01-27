/*
 * Copyright 2020 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.znai.extensions.api;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class ApiParameters {
    private final ApiParameter root;

    public ApiParameters(String anchorPrefix) {
        root = new ApiParameter(ApiParametersAnchors.sanitizeAnchorId(anchorPrefix),
                "root", new ApiLinkedText("root"), Collections.emptyList(), "");
    }

    public ApiParameter addInFront(String name, ApiLinkedText type, List<Map<String, Object>> description, String textForSearch) {
        return root.addInFront(name, type, description, textForSearch);
    }

    public ApiParameter add(String name, ApiLinkedText type, List<Map<String, Object>> description, String textForSearch) {
        return root.add(name, type, description, textForSearch);
    }

    public ApiParameter find(String name) {
        return root.find(name);
    }

    public ApiParameter getRoot() {
        return root;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("parameters", root.getChildren().stream().map(ApiParameter::toMap).collect(toList()));

        return result;
    }

    public List<String> collectAllAnchors() {
        return root.getChildren().stream()
                .flatMap(child -> child.collectAllAnchors().stream())
                .collect(toList());
    }

    public String combinedTextForSearch() {
        return root.getChildren().stream()
                .map(ApiParameter::combinedTextForSearch)
                .collect(Collectors.joining(" "));
    }
}
