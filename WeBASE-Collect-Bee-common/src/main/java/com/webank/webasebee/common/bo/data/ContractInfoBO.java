package com.webank.webasebee.common.bo.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wesleywang
 * @Description:
 * @date 2020/10/26
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContractInfoBO {

    private String contractName;

    private String contractBinary;

    private String contractABI;

    private int version;

    private long abiHash;


}
