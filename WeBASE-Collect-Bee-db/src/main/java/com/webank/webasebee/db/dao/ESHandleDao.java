package com.webank.webasebee.db.dao;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.webank.webasebee.common.bo.data.BlockInfoBO;
import com.webank.webasebee.common.bo.data.BlockTxDetailInfoBO;
import com.webank.webasebee.common.bo.data.ContractInfoBO;
import com.webank.webasebee.common.bo.data.DeployedAccountInfoBO;
import com.webank.webasebee.common.bo.data.EventBO;
import com.webank.webasebee.common.bo.data.MethodBO;
import com.webank.webasebee.common.bo.data.TxRawDataBO;
import com.webank.webasebee.common.bo.data.TxReceiptRawDataBO;
import com.webank.webasebee.db.config.ESBeanConfig;
import com.webank.webasebee.db.entity.DeployedAccountInfo;
import com.webank.webasebee.db.service.ESService;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author wesleywang
 * @Description:
 * @date 2020/10/26
 */
@Component
public class ESHandleDao {

    @Autowired
    private ESService esService;
    @Autowired
    private ESBeanConfig esBeanConfig;

    public static final String BLOCK_DETAIL = "blockdetailinfo";

    public static final String BLOCK_RAW_DATA = "blockrawdatab";

    public static final String TX_RAW_DATA = "txrawdata";

    public static final String DEPLOY_ACCOUNT = "deployaccountinfo";

    public static final String CONTRACT_INFO = "contractinfo";

    public static final String TX_RECEIPT_RAW_DATA = "txreceiptrawdata";

    public static final String BLOCK_TX_DETAIL = "blocktxdetailinfo";

    public static final String EVENT = "event";

    public static final String METHOD = "method";

    @PostConstruct
    public void initIndex() throws Exception {
        TransportClient client = esBeanConfig.getClient();
        if (!esService.indexExists(client,BLOCK_DETAIL)){
            esService.createIndex(client,BLOCK_DETAIL);
        }
        if (!esService.indexExists(client,BLOCK_RAW_DATA)){
            esService.createIndex(client,BLOCK_RAW_DATA);
        }
        if (!esService.indexExists(client,TX_RAW_DATA)){
            esService.createIndex(client,TX_RAW_DATA);
        }
        if (!esService.indexExists(client,DEPLOY_ACCOUNT)){
            esService.createIndex(client,DEPLOY_ACCOUNT);
        }
        if (!esService.indexExists(client,CONTRACT_INFO)){
            esService.createIndex(client,CONTRACT_INFO);
        }
        if (!esService.indexExists(client,TX_RECEIPT_RAW_DATA)){
            esService.createIndex(client,TX_RECEIPT_RAW_DATA);
        }
        if (!esService.indexExists(client,BLOCK_TX_DETAIL)){
            esService.createIndex(client,BLOCK_TX_DETAIL);
        }
        if (!esService.indexExists(client,EVENT)){
            esService.createIndex(client,EVENT);
        }
        if (!esService.indexExists(client,METHOD)){
            esService.createIndex(client,METHOD);
        }
    }

    public void saveBlockInfo(BlockInfoBO blockInfoBO) throws Exception {
        if (!esBeanConfig.isEsEnabled()) {
            return;
        }
        esService.createDocument(esBeanConfig.getClient(),
                BLOCK_DETAIL, "_doc", String.valueOf(blockInfoBO.getBlockDetailInfo().getBlockHeight()),
                blockInfoBO.getBlockDetailInfo());

        esService.createDocument(esBeanConfig.getClient(),
                BLOCK_RAW_DATA,"_doc", String.valueOf(blockInfoBO.getBlockRawDataBO().getBlockHeight()),
                blockInfoBO.getBlockRawDataBO());

        for (TxRawDataBO txRawDataBO : blockInfoBO.getTxRawDataBOList()) {
            esService.createDocument(esBeanConfig.getClient(),
                    TX_RAW_DATA,"_doc",
                    txRawDataBO.getTxHash(), txRawDataBO);
        }

        for (DeployedAccountInfoBO deployedAccountInfoBO : blockInfoBO.getDeployedAccountInfoBOS()) {
            DeployedAccountInfo deployedAccountInfo = new DeployedAccountInfo();
            BeanUtil.copyProperties(deployedAccountInfoBO, deployedAccountInfo, true);
            esService.createDocument(esBeanConfig.getClient(),
                    DEPLOY_ACCOUNT,"_doc",
                    deployedAccountInfoBO.getContractAddress(),
                    deployedAccountInfo);
        }

        for (TxReceiptRawDataBO txReceiptRawDataBO : blockInfoBO.getTxReceiptRawDataBOList()) {
            esService.createDocument(esBeanConfig.getClient(),
                    TX_RECEIPT_RAW_DATA,"_doc",
                    txReceiptRawDataBO.getTxHash(),
                    txReceiptRawDataBO);
        }

        for (BlockTxDetailInfoBO blockTxDetailInfoBO : blockInfoBO.getBlockTxDetailInfoList()) {
            esService.createDocument(esBeanConfig.getClient(),
                    BLOCK_TX_DETAIL,"_doc",
                    blockTxDetailInfoBO.getTxHash(),
                    blockTxDetailInfoBO);
        }

        for (EventBO eventBO : blockInfoBO.getEventInfoList()) {
            esService.createDocument(esBeanConfig.getClient(),
                    EVENT, "_doc", eventBO.getTxHash(), eventBO);
        }

        for (MethodBO methodBO : blockInfoBO.getMethodInfoList()) {
            esService.createDocument(esBeanConfig.getClient(),
                    METHOD, "_doc", methodBO.getTxHash(), methodBO);
        }
    }

    public void saveContractInfo(ContractInfoBO contractInfoBO) throws Exception {
        if (!esBeanConfig.isEsEnabled()) {
            return;
        }
        esService.createDocument(esBeanConfig.getClient(),
                CONTRACT_INFO, "_doc",contractInfoBO);
    }


}
