package com.webank.webasebee.db.repository;

import com.webank.webasebee.db.entity.TxReceiptRawData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author wesleywang
 * @Description:
 * @date 2020/10/23
 */
@Repository
public interface TxReceiptRawDataRepository extends JpaRepository<TxReceiptRawData, Long>, JpaSpecificationExecutor<TxReceiptRawData> {

}
