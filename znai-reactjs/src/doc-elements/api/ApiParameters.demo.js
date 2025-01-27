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

import React from "react";
import ApiParameters from "./ApiParameters";
import { elementsLibrary } from "../DefaultElementsLibrary";
import { Paragraph } from "../paragraph/Paragraph";

const personParameters = [
    {anchorId: 'prefix_firstName', name: 'firstName', type: 'string', description: [{"text": "first name", "type": "SimpleText"}]},
    {anchorId: 'prefix_lastName',name: 'lastName', type: 'string', description: [{"text": "last name", "type": "SimpleText"}]},
    {anchorId: 'prefix_score',name: 'score', type: 'integer', description: [{"text": "score accumulated over last year", "type": "SimpleText"}]},
]

const personParametersWithTypeRef = [
    {anchorId: 'prefix_firstName', name: 'firstName', type: [
            {text: "list of ", refId: ""},
            {text: "tokens", refId: "lib_string_tokens"}],
        description: [{"text": "first name", "type": "SimpleText"}]},
    {anchorId: 'prefix_lastName',name: 'lastName', type: 'string', description: [{"text": "last name", "type": "SimpleText"}]},
    {anchorId: 'prefix_score',name: 'score', type: 'integer', description: [{"text": "score accumulated over last year", "type": "SimpleText"}]},
]

const paragraph = {"type": "Paragraph", "content": [{"text": "first name", "type": "SimpleText"}]}
const code = {"type": "Snippet", "snippet": "println 'hello world'"}

const multipleParagraph = [paragraph, paragraph, paragraph]
const codeBeforeParagraph = [code, paragraph, code, paragraph, paragraph]

const personLongTypeParameters = [
    {name: 'firstName', type: 'string or gstring or some union and some other long description', description: multipleParagraph},
    {name: 'lastName', type: 'string', description: multipleParagraph},
    {name: 'score', type: 'integer', description: multipleParagraph},
]

const personLongDescriptionParameters = [
    {name: 'firstName', type: 'string', description: multipleParagraph},
    {name: 'lastName', type: 'string', description: multipleParagraph},
    {name: 'score', type: 'integer', description: multipleParagraph},
]

const mailBoxParameters = [
    {name: 'zipCode', type: 'string', description: [{"text": "zip code", "type": "SimpleText"}]},
    {name: 'isPersonal', type: 'boolean', description: [{"text": "does it belong to a org or a person", "type": "SimpleText"}]},
]

const addressParameters = [
    {name: 'street', type: 'string', description: [{"text": "street name", "type": "SimpleText"}]},
    {name: 'mailBox', type: 'object', children: mailBoxParameters,  description: [{"text": "mail box", "type": "SimpleText"}]},
    {name: 'simple', type: 'string', description: [{"text": "simple parameter after a complex one", "type": "SimpleText"}]},
]

const nestedParameters = [
    {name: 'primaryResidence', type: 'object', children: addressParameters, description: [{"text": "primary residence", "type": "SimpleText"}]},
    {name: 'secondaryPerson', type: 'object', children: createPersonParameters('nested'), description: [{"text": "secondary person", "type": "SimpleText"}]},
    {name: 'short', type: 'object', children: createPersonParameters('nested'), description: [{"text": "secondary person", "type": "SimpleText"}]},
    {name: 'ids', type: 'array of objects', children: createPersonParameters('nested'), description: [{"text": "secondary person", "type": "SimpleText"}]},
]

const withCodeSnippetFirst = [
    {name: 'paramOne', type: 'string', description: codeBeforeParagraph}
]

export function apiParametersDemo(registry) {
    registry
        .add('flat parameters', () => (
            <ApiParameters elementsLibrary={elementsLibrary} parameters={personParameters}/>
        ))
        .add('flat parameters with global refs', () => (
            <ApiParameters elementsLibrary={elementsLibrary} parameters={personParametersWithTypeRef}/>
        ))
        .add('flat parameters small size', () => (
            <ApiParameters elementsLibrary={elementsLibrary} parameters={personParameters} small={true}/>
        ))
        .add('flat parameters long name and type', () => (
            <ApiParameters elementsLibrary={elementsLibrary} parameters={personLongTypeParameters}/>
        ))
        .add('flat parameters with title', () => (
            <ApiParameters elementsLibrary={elementsLibrary} parameters={personParameters} title="Person definition"/>
        ))
        .add('flat parameters with long description', () => (
            <ApiParameters elementsLibrary={elementsLibrary} parameters={personLongDescriptionParameters}/>
        ))
        .add('flat parameters with references', () => (
            <ApiParameters elementsLibrary={elementsLibrary}
                           parameters={personParameters}
                           references={paramsReferences()}/>
        ))
        .add('nested parameters', () => (
            <ApiParameters elementsLibrary={elementsLibrary} parameters={nestedParameters}/>
        ))
        .add('with text around', () => (
            <div className="content-block">
                <React.Fragment>
                    <ParagraphText/>
                    <ApiParameters elementsLibrary={elementsLibrary} parameters={nestedParameters}/>
                    <ParagraphText/>
                </React.Fragment>
            </div>
        ))
        .add('with text around small size', () => (
            <div className="content-block">
                <React.Fragment>
                    <ParagraphText/>
                    <ApiParameters elementsLibrary={elementsLibrary} parameters={nestedParameters} small={true}/>
                    <ParagraphText/>
                </React.Fragment>
            </div>
        ))
        .add('with text around and title', () => (
            <div className="content-block">
                <ParagraphText/>
                <ApiParameters elementsLibrary={elementsLibrary} parameters={nestedParameters} title="Person definition"/>
                <ParagraphText/>
            </div>
        ))
        .add('with code first in description', () => (
            <div className="content-block">
                <ParagraphText/>
                <ApiParameters elementsLibrary={elementsLibrary} parameters={withCodeSnippetFirst}/>
                <ParagraphText/>
            </div>
        ))
}

function ParagraphText() {
    return (
        <Paragraph content={simpleText()} elementsLibrary={elementsLibrary}/>
    )
}

function simpleText() {
    return [
        {type: 'SimpleText', text: 'simple paragraph with text. simple paragraph with text. simple paragraph with text.'}
    ]
}

function paramsReferences() {
    return {
        'firstName': {
            pageUrl: '#firstname'
        },
        'integer': {
            pageUrl: '#integer'
        }
    }
}

function createPersonParameters(anchorPrefix) {
    return [
        {anchorId: anchorPrefix + '_firstName', name: 'firstName', type: 'string', description: [{"text": "first name", "type": "SimpleText"}]},
        {anchorId: anchorPrefix + '_lastName',name: 'lastName', type: 'string', description: [{"text": "last name", "type": "SimpleText"}]},
        {anchorId: anchorPrefix + '_score',name: 'score', type: 'integer', description: [{"text": "score accumulated over last year", "type": "SimpleText"}]},
    ]
}
