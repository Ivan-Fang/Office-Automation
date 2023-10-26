package com.ivanfang.process.service.impl;

import com.ivanfang.auth.service.SysUserService;
import com.ivanfang.model.process.ProcessRecord;
import com.ivanfang.process.mapper.ProcessRecordMapper;
import com.ivanfang.process.service.ProcessRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ivanfang.security.custom.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessRecordServiceImpl extends ServiceImpl<ProcessRecordMapper, ProcessRecord> implements ProcessRecordService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public void record(Long processId, String description, Integer status) {
        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessId(processId);
        processRecord.setDescription(description);
        processRecord.setStatus(status);

        String username = LoginUserInfoHelper.getUsername();
        Long userId = LoginUserInfoHelper.getUserId();
        processRecord.setOperateUser(username);
        processRecord.setOperateUserId(userId);

        baseMapper.insert(processRecord);
    }

}
