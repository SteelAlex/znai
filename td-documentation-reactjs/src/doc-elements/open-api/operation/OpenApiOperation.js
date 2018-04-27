import React from 'react'
import OpenApiResponses from '../response/OpenApiResponses'
import OpenApiParameters from '../parameter/OpenApiParameters'

import OpenApiBodyParameter from '../parameter/OpenApiBodyParameter'

import './OpenApiOperation.css'

function OpenApiOperation({elementsLibrary, operation}) {
    const parameters = operation.parameters || []

    const pathParameters = parameters.filter(p => p.in === 'path')
    const queryParameters = parameters.filter(p => p.in === 'query')

    const bodyParams = parameters.filter(p => p.in === 'body')
    const bodyParameter = bodyParams.length ? bodyParams[0] : null

    return (
        <div className="open-api-operation content-block">
            <div className="url-and-summary">
                <div className="method">{operation.method}</div>
                <div className="path"><Path path={operation.path}/></div>
            </div>
            <div className="description">
                <elementsLibrary.DocElement content={operation.description} elementsLibrary={elementsLibrary}/>
            </div>

            <OpenApiParameters label="Path parameters" parameters={pathParameters} elementsLibrary={elementsLibrary}/>
            <OpenApiParameters label="Query parameters" parameters={queryParameters} elementsLibrary={elementsLibrary}/>
            <OpenApiBodyParameter parameter={bodyParameter} elementsLibrary={elementsLibrary}/>
            <OpenApiResponses responses={operation.responses} elementsLibrary={elementsLibrary}/>
        </div>
    )
}

function Path({path}) {
    const parts = path.split("/")

    return parts
        .filter(part => part.length)
        .map((part, idx) => {
            const className = part.indexOf('{') === 0 ?
                'path-part-parameter' : 'path-part'

            return (
                <React.Fragment key={idx}>
                    <div className="path-part-delimiter">/</div>
                    <div className={className}>{part}</div>
                </React.Fragment>)
        })
}

export default OpenApiOperation
