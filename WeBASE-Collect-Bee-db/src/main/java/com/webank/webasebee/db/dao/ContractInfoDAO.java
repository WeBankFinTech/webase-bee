package com.webank.webasebee.db.dao;

import cn.hutool.core.bean.BeanUtil;
import com.webank.webasebee.common.bo.data.ContractInfoBO;
import com.webank.webasebee.db.entity.ContractInfo;
import com.webank.webasebee.db.repository.ContractInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wesleywang
 * @Description:
 * @date 2020/10/26
 */
@Component
public class ContractInfoDAO implements SaveInterface<ContractInfoBO>{

    @Autowired
    private ContractInfoRepository contractInfoRepository;

    public void save(ContractInfo contractInfo) {
        BaseDAO.saveWithTimeLog(contractInfoRepository, contractInfo);
    }

    @Override
    public void save(ContractInfoBO contractInfoBO) {
        ContractInfo contractInfo = new ContractInfo();
        BeanUtil.copyProperties(contractInfoBO, contractInfo, true);
        save(contractInfo);
    }

    public long saveAndObtainId(ContractInfoBO contractInfoBO) {
        ContractInfo contractInfo = new ContractInfo();
        BeanUtil.copyProperties(contractInfoBO, contractInfo, true);
        contractInfoRepository.save(contractInfo);
        return contractInfo.getContractId();
    }
}
