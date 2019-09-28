/*
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

package com.twosigma.znai.java.parser;

public class EnumEntry {
    private String name;
    private String javaDocText;
    private boolean isDeprecated;

    public EnumEntry(String name, String javaDocText, boolean isDeprecated) {
        this.name = name;
        this.javaDocText = javaDocText;
        this.isDeprecated = isDeprecated;
    }

    public String getName() {
        return name;
    }

    public String getJavaDocText() {
        return javaDocText;
    }

    public boolean isDeprecated() {
        return isDeprecated;
    }

    @Override
    public String toString() {
        return "EnumEntry{" +
                "name='" + name + '\'' +
                ", javaDocText='" + javaDocText + '\'' +
                ", isDeprecated=" + isDeprecated +
                '}';
    }
}