package com.ivanfang.process.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ivanfang.model.process.Process;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ivanfang.model.process.ProcessType;
import com.ivanfang.vo.process.ApprovalVo;
import com.ivanfang.vo.process.ProcessFormVo;
import com.ivanfang.vo.process.ProcessQueryVo;
import com.ivanfang.vo.process.ProcessVo;

import java.util.List;
import java.util.Map;

public interface ProcessService extends IService<Process> {

    Page<ProcessVo> selectPage(Page<Process> pageParam, ProcessQueryVo processQueryVo);

    void deployByZip(String filepath);

    void startUp(ProcessFormVo processFormVo);

    Page<ProcessVo> getPending(Page<Process> pageParam);

    Map<String, Object> show(Integer processId);

    void approve(ApprovalVo approvalVo);

    Page<ProcessVo> getProcessed(Page<Process> pageParam);

    Page<ProcessVo> getStarted(Page<Process> pageParam);
}
