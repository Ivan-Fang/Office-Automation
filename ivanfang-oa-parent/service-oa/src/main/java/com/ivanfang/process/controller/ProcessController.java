package com.ivanfang.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ivanfang.common.result.Result;
import com.ivanfang.model.process.Process;
import com.ivanfang.process.service.ProcessService;
import com.ivanfang.vo.process.ProcessQueryVo;
import com.ivanfang.vo.process.ProcessVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("審批管理-管理端")
@RestController
@RequestMapping("/admin/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    @ApiOperation("分頁查詢")
    @GetMapping("/get/{page}/{pageSize}")
    public Result getByPage(@PathVariable Integer page,
                            @PathVariable Integer pageSize,
                            ProcessQueryVo processQueryVo) {
        Page<Process> pageParam = new Page<>(page, pageSize);
        Page<ProcessVo> pageModel = processService.selectPage(pageParam, processQueryVo);
        return Result.success(pageModel);
    }

}

