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
package com.webank.webasebee.common.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.utils.Numeric;

import lombok.extern.slf4j.Slf4j;

/**
 * FunctionInputDecode
 *
 * @Description: FunctionInputDecode
 * @author graysonzhang
 * @data 2018-12-04 10:54:15
 *
 */
@Slf4j
public class FunctionInputDecode {

    /**
     * decode function input, return List<Type>.
     * 
     * @param rawInput
     * @param inputParameters
     * @return
     * @return List<Type>
     */
    public static List<Type> decode(String rawInput, List<TypeReference<Type>> inputParameters) {
        String input = Numeric.cleanHexPrefix(rawInput);
        log.info("input without Prefix : {}", input);

        if (input == null || input.length() == 0) {
            return Collections.emptyList();
        } else {
            return build(input, inputParameters);
        }
    }

    private static List<Type> build(String input, List<TypeReference<Type>> inputParameters) {
        List<Type> results = new ArrayList<>(inputParameters.size());
        int offset = 0;
        for (TypeReference<?> typeReference : inputParameters) {
            try {
                @SuppressWarnings("unchecked")
                Class<Type> type = (Class<Type>) typeReference.getClassType();
            } catch (ClassNotFoundException e) {
                throw new UnsupportedOperationException("Invalid class reference provided", e);
            }
        }
        return results;
    }
}
