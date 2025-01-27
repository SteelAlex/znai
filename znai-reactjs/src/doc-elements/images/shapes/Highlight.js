/*
 * Copyright 2021 znai maintainers
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

import React from 'react'
import {styleByName} from './styleByName';

import rectangle from './Rectangle'

const HighlightBody = ({x, y, width, height, color, scale, ...props}) => {
    const styleScheme = styleByName(color)

    const scaledX = x * scale;
    const scaledY = y * scale;

    return (
        <g>
            <rect x={scaledX} y={scaledY} width={width} fill={styleScheme.line}  height={height} strokeWidth="0" fillOpacity={0.6} {...props} />
        </g>
    );
}

const highlight = {
    ...rectangle,
    body: HighlightBody
}

export default highlight