package com.demo.mapper;

import com.demo.dto.EmailRecipient;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmlMapper {
    List<Long> selectAllMasterIds();

    List<EmailRecipient> selectRecipientsByMasterId(@Param("masterId") Long masterId);
    List<Long> selectTest1();
    List<Long> selectTest2();
}
