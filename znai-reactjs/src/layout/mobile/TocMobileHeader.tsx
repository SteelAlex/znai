/*
 * Copyright 2020 znai maintainers
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

import { DocMeta } from '../../structure/docMeta';
import React from 'react';

import { Icon } from '../../doc-elements/icons/Icon';

import './TocMobileHeader.css';

interface Props {
  docMeta: DocMeta;

  onHeaderClick(): void;
  onMenuClick(): void;
}

export function TocMobileHeader({docMeta, onHeaderClick, onMenuClick}: Props) {
  return (
    <div className="znai-mobile-header">
      <div className="znai-mobile-header-logo-title">
        <div className="znai-documentation-logo mobile"/>
        <div className="znai-mobile-header-title"
             onClick={onHeaderClick}>
          {docMeta.title + " " + docMeta.type}
        </div>
      </div>
      <div className="znai-mobile-header-burger">
        <Icon id="menu" onClick={onMenuClick}/>
      </div>
    </div>
  )
}