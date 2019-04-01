/**
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.webasebee.bo;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EventMetaInfo
 *
 * @Description: EventMetaInfo storages event config data for parsing event data 
 * @author maojiayu
 * @author graysonzhang
 * @data 2018-11-08 22:03:20
 *
 */
@Data
@NoArgsConstructor
@Component
public class EventMetaInfo {
    
    /** @Fields eventName : event name */
    private String eventName;
    
    /** @Fields eventTableCount : for sharding event tables */
    private int eventTableCount;
    
    /** @Fields ignoreParams : when parsing event data, the ignore params will be ignored */
    private List<String> ignoreParams;
    
    /** @Fields contractName : contract name */
    private String contractName;
    
    /** @Fields list : event filed list */
    private List<FieldVO> list;
}
