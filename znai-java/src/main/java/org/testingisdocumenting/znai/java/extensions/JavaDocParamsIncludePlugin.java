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

package org.testingisdocumenting.znai.java.extensions;

import org.testingisdocumenting.znai.extensions.PluginResult;
import org.testingisdocumenting.znai.extensions.api.ApiLinkedText;
import org.testingisdocumenting.znai.extensions.api.ApiParameters;
import org.testingisdocumenting.znai.extensions.include.IncludePlugin;
import org.testingisdocumenting.znai.java.parser.JavaCode;
import org.testingisdocumenting.znai.java.parser.JavaMethod;
import org.testingisdocumenting.znai.java.parser.JavaMethodReturn;
import org.testingisdocumenting.znai.parser.docelement.DocElement;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class JavaDocParamsIncludePlugin extends JavaIncludePluginBase {
    @Override
    public String id() {
        return "java-doc-params";
    }

    @Override
    public IncludePlugin create() {
        return new JavaDocParamsIncludePlugin();
    }

    @Override
    public JavaIncludeResult process(JavaCode javaCode) {
        JavaMethod javaMethod = javaCode.findMethod(entry);

        ApiParameters apiParameters = new ApiParameters(javaMethod.getAnchorPrefix());

        addReturn(apiParameters, javaMethod);
        javaMethod.getParams().forEach(param -> {
            JavaDocElementsMapsAndSearchText docElementsMapsAndSearchText =
                    javaDocTextToDocElements(param.getJavaDocText());
            apiParameters.add(param.getName(), new ApiLinkedText(param.getType()),
                    docElementsMapsAndSearchText.docElementsMaps, docElementsMapsAndSearchText.searchText);
        });

        Map<String, Object> props = apiParameters.toMap();
        codeReferencesFeature.updateProps(props);
        props.putAll(pluginParams.getOpts().toMap());

        List<DocElement> docElements =
                PluginResult.docElement("ApiParameters", props).getDocElements();

        return new JavaIncludeResult(docElements, extractText(javaMethod));
    }

    private String extractText(JavaMethod javaMethod) {
        JavaMethodReturn methodReturn = javaMethod.getJavaMethodReturn();

        String returnPart = methodReturn != null ?
                "return " + methodReturn.getType() + " " + methodReturn.getJavaDocText():
                "";
        String paramsPart = javaMethod.getParams().stream()
                .map(p -> p.getName() + " " + p.getType() + " " + p.getJavaDocText())
                .collect(joining(" "));

        return paramsPart +
                (returnPart.isEmpty() ? "" : " ") +
                returnPart;
    }

    private void addReturn(ApiParameters apiParameters, JavaMethod javaMethod) {
        JavaMethodReturn methodReturn = javaMethod.getJavaMethodReturn();
        if (methodReturn == null) {
            return;
        }

        JavaDocElementsMapsAndSearchText elementsMapsAndSearchText =
                javaDocTextToDocElements(methodReturn.getJavaDocText());

        apiParameters.add("return", new ApiLinkedText(methodReturn.getType()),
                elementsMapsAndSearchText.docElementsMaps, elementsMapsAndSearchText.searchText);
    }
}
