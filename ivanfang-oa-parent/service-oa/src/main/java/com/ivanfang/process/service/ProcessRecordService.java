package com.ivanfang.process.service;

import com.ivanfang.model.process.ProcessRecord;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ProcessRecordService extends IService<ProcessRecord> {

    void record(Long processId, String description, Integer status);

}
