package com.ivanfang.process.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ivanfang.model.process.Process;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ivanfang.vo.process.ProcessQueryVo;
import com.ivanfang.vo.process.ProcessVo;
import org.apache.ibatis.annotations.Param;

public interface ProcessMapper extends BaseMapper<Process> {

    Page<ProcessVo> selectPage(Page<Process> pageParam, @Param("vo") ProcessQueryVo processQueryVo);

}
