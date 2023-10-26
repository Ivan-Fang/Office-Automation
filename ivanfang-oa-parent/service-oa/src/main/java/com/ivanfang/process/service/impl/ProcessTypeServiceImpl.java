package com.ivanfang.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ivanfang.model.process.ProcessTemplate;
import com.ivanfang.model.process.ProcessType;
import com.ivanfang.process.mapper.ProcessTypeMapper;
import com.ivanfang.process.service.ProcessTemplateService;
import com.ivanfang.process.service.ProcessTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessTypeServiceImpl extends ServiceImpl<ProcessTypeMapper, ProcessType> implements ProcessTypeService {

    @Autowired
    private ProcessTemplateService processTemplateService;

    // 查詢所有已發佈的審批類型及其模板
    @Override
    public List<ProcessType> getAllTypeWithTemplate() {
        List<ProcessType> processTypeList = baseMapper.selectList(null);
        for (ProcessType processType : processTypeList) {
            // 查詢某類型下的所有模板
            Long typeId = processType.getId();

            LambdaQueryWrapper<ProcessTemplate> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProcessTemplate::getProcessTypeId, typeId);
            wrapper.eq(ProcessTemplate::getStatus, 1);          // 必須是已發佈的

            List<ProcessTemplate> processTemplateList = processTemplateService.list(wrapper);
            processType.setProcessTemplateList(processTemplateList);
        }
        return processTypeList;
    }

}
