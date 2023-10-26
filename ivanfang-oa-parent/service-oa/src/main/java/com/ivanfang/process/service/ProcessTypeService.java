package com.ivanfang.process.service;

import com.ivanfang.model.process.ProcessType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 審批類型 服务类
 * </p>
 *
 * @author ivanfang
 * @since 2023-10-19
 */
public interface ProcessTypeService extends IService<ProcessType> {

    List<ProcessType> getAllTypeWithTemplate();

}
