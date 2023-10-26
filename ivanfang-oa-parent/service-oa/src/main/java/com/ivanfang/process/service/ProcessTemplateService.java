package com.ivanfang.process.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ivanfang.model.process.ProcessTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ProcessTemplateService extends IService<ProcessTemplate> {

    Page<ProcessTemplate> selectPageProcessTemplate(Page<ProcessTemplate> pages);

    void publish(Integer id);
}
