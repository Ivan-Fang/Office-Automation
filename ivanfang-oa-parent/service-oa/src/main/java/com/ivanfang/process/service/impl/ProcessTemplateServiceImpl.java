package com.ivanfang.process.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ivanfang.model.process.ProcessTemplate;
import com.ivanfang.model.process.ProcessType;
import com.ivanfang.process.mapper.ProcessTemplateMapper;
import com.ivanfang.process.service.ProcessService;
import com.ivanfang.process.service.ProcessTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ivanfang.process.service.ProcessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class ProcessTemplateServiceImpl extends ServiceImpl<ProcessTemplateMapper, ProcessTemplate> implements ProcessTemplateService {

    @Autowired
    private ProcessTypeService processTypeService;

    @Autowired
    private ProcessService processService;

    @Override
    public Page<ProcessTemplate> selectPageProcessTemplate(Page<ProcessTemplate> pageParam) {

        // 1. 根據 pageParam 查詢分頁資料
        Page<ProcessTemplate> pages = baseMapper.selectPage(pageParam, null);

        // 2. 取出 pages 中真正存儲資料的部分（records）
        List<ProcessTemplate> processTemplateList = pages.getRecords();

        // 3. 填補 processTypeName 這個欄位
        for (ProcessTemplate processTemplate : processTemplateList) {
            // 根據 processTemplate 中的 processTypeId 查詢 processType，進而查出 processTypeName
            Long processTypeId = processTemplate.getProcessTypeId();
            ProcessType processType = processTypeService.getById(processTypeId);
            if (processType != null) {
                String processTypeName = processType.getName();
                processTemplate.setProcessTypeName(processTypeName);
            }
        }

        return pages;

    }

    @Override
    public void publish(Integer id) {
        // 將 status 設為 1 表示該審批模板已發佈
        ProcessTemplate processTemplate = baseMapper.selectById(id);
        processTemplate.setStatus(1);
        baseMapper.updateById(processTemplate);

        // 完成流程部署
        String path = processTemplate.getProcessDefinitionPath();
        if (!StringUtils.isEmpty(path)) {
            processService.deployByZip(path);
        }
    }

}
