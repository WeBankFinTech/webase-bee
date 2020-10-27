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
package com.webank.webasebee.core.parser;

import com.google.common.collect.Lists;
import com.webank.webasebee.common.bo.contract.ContractMapsInfo;
import com.webank.webasebee.common.bo.contract.ContractMethodInfo;
import com.webank.webasebee.common.bo.contract.MethodMetaInfo;
import com.webank.webasebee.common.bo.data.ContractInfoBO;
import com.webank.webasebee.common.constants.AbiTypeConstants;
import com.webank.webasebee.common.tools.ClazzScanUtils;
import com.webank.webasebee.common.tools.MethodUtils;
import com.webank.webasebee.core.config.SystemEnvironmentConfig;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.abi.wrapper.ABIDefinition;
import org.fisco.bcos.sdk.abi.wrapper.ABIDefinition.NamedType;
import org.fisco.bcos.sdk.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * ContractParser using for getting contract java code file info, that will be used to parse transaction data.
 *
 * @Description: ContractParser
 * @author graysonzhang
 * @author maojiayu
 * @data 2018-12-17 15:06:51
 *
 */
@Configuration
@Slf4j
@DependsOn("cryptoKeyPair")
public class ContractParser {

    /** @Fields monitorGeneratedConfig : monitor config params start with monitor in application.properties file */
    @Autowired
    private SystemEnvironmentConfig systemEnvironmentConfig;
    @Autowired
    private Client client;

    /**
     * Parsing all contract java code files from contract path, and storage contract info into ContractMethodInfo
     * object, and return ContractMethodInfo object list.
     * 
     * @return List<ContractMethodInfo>
     */
    public List<ContractMethodInfo> initContractMethodInfo() throws Exception {
        List<ContractMethodInfo> contractMethodInfos = Lists.newArrayList();
        Set<Class<?>> clazzs = ClazzScanUtils.scan(systemEnvironmentConfig.getContractPath(),
                systemEnvironmentConfig.getContractPackName());
        for (Class<?> clazz : clazzs) {
            contractMethodInfos.add(parse(clazz));
        }
        return contractMethodInfos;
    }

    /**
     * Parsing single class object of contract java code file, and storage contract info into ContractMethodInfo object,
     * firstly, remove event function, query function and functions that param's null, compute methodId and save method
     * info into ContractMethodInfo object.
     * 
     * @param clazz: class object of contract java code file.
     * @return ContractMethodInfo
     */
    public ContractMethodInfo parse(Class<?> clazz) throws Exception {
        List<ABIDefinition> abiDefinitions = MethodUtils.getContractAbiList(clazz);
        if (CollectionUtils.isEmpty(abiDefinitions)) {
            return null;
        }
        String className = clazz.getSimpleName();
        ContractMethodInfo contractMethodInfo = new ContractMethodInfo();
        ContractInfoBO contractInfoBO = new ContractInfoBO();
        contractInfoBO.setContractName(className);
        contractInfoBO.setContractBinary(MethodUtils.getClassField(clazz, "BINARY"));
        contractInfoBO.setContractABI(MethodUtils.getClassField(clazz, "ABI"));
        contractInfoBO.setAbiHash(contractInfoBO.getContractABI().hashCode());
        contractMethodInfo.setContractInfoBO(contractInfoBO);
        List<MethodMetaInfo> methodIdList = Lists.newArrayListWithExpectedSize(abiDefinitions.size());
        contractMethodInfo.setMethodMetaInfos(methodIdList);

        for (ABIDefinition abiDefinition : abiDefinitions) {
            String abiType = abiDefinition.getType();
            // remove event function and query function
            if (abiType.equals(AbiTypeConstants.ABI_EVENT_TYPE) || abiDefinition.isConstant()) {
                continue;
            }
            // remove functions that input'params is null
            List<NamedType> inputs = abiDefinition.getInputs();
            if (inputs == null || inputs.isEmpty()) {
                continue;
            }
            String methodName = abiDefinition.getName();
            if (abiType.equals(AbiTypeConstants.ABI_CONSTRUCTOR_TYPE)) {
                methodName = "constructor";
            }
            // compute method id by method name and method input's params.
            String methodId = abiDefinition.getMethodId(client.getCryptoSuite());
            log.debug("methodId {} , methodName {}", methodId, methodName);
            MethodMetaInfo metaInfo = new MethodMetaInfo();
            metaInfo.setMethodId(methodId);
            metaInfo.setMethodName(methodName);
            metaInfo.setFieldsList(inputs);
            metaInfo.setOutputFieldsList(abiDefinition.getOutputs());
            methodIdList.add(metaInfo);
        }
        return contractMethodInfo;
    }

    /**
     * Translate all contract info of ContractMethodInfo's objects to methodIdMap and contractBinaryMap.
     * 
     * @param contractMethodInfos: contractMethodInfos contains methodIdMap and contractBinaryMap.
     * @return ContractMapsInfo
     */
    @Bean
    public ContractMapsInfo transContractMethodInfo2ContractMapsInfo() throws Exception {
        List<ContractMethodInfo> contractMethodInfos = initContractMethodInfo();
        ContractMapsInfo contractMapsInfo = new ContractMapsInfo();
        Map<String, MethodMetaInfo> methodIdMap = new HashMap<>();
        Map<String, ContractMethodInfo> contractBinaryMap = new HashMap<>();
        for (ContractMethodInfo contractMethodInfo : contractMethodInfos) {
            for (MethodMetaInfo methodMetaInfo : contractMethodInfo.getMethodMetaInfos()) {
                methodIdMap.put(methodMetaInfo.getMethodId(), methodMetaInfo);
                contractBinaryMap.put(contractMethodInfo.getContractInfoBO().getContractBinary(), contractMethodInfo);
            }
        }
        log.info("Init sync block: find {} contract constructors.", contractBinaryMap.size());
        contractMapsInfo.setContractBinaryMap(contractBinaryMap);
        contractMapsInfo.setMethodIdMap(methodIdMap);
        return contractMapsInfo;
    }
}
