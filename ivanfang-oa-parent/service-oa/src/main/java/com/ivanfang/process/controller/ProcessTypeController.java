package com.ivanfang.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ivanfang.common.result.Result;
import com.ivanfang.model.process.ProcessType;
import com.ivanfang.process.service.ProcessTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "審批類型管理介面")
@RestController
@RequestMapping("/admin/process/processType")
public class ProcessTypeController {

    @Autowired
    private ProcessTypeService processTypeService;

    // 新增
//    @PreAuthorize("hasAuthority('btn.processType.add')")
    @ApiOperation("新增審批類型")
    @PostMapping("/save")
    public Result save(@RequestBody ProcessType processType) {
        processTypeService.save(processType);
        return Result.success();
    }

    // 刪除
//    @PreAuthorize("hasAuthority('btn.processType.remove')")
    @ApiOperation("根據 id 刪除審批類型")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Integer id) {
        processTypeService.removeById(id);
        return Result.success();
    }

    // 修改
//    @PreAuthorize("hasAuthority('btn.processType.update')")
    @ApiOperation("修改審批類型")
    @PutMapping("/update")
    public Result update(@RequestBody ProcessType processType) {
        processTypeService.updateById(processType);
        return Result.success();
    }

    // 根據頁碼進行查詢
//    @PreAuthorize("hasAuthority('btn.processType.list')")
    @ApiOperation("根據頁碼查詢審批類型")
    @GetMapping("/get/{page}/{pageSize}")
    public Result getByPage(@PathVariable Integer page, @PathVariable Integer pageSize) {
        Page<ProcessType> pages = new Page<>(page, pageSize);
        processTypeService.page(pages);
        return Result.success(pages);
    }

    // 根據 id 進行查詢
//    @PreAuthorize("hasAuthority('btn.processType.list')")
    @ApiOperation("根據 id 查詢審批類型")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Integer id) {
        ProcessType processType = processTypeService.getById(id);
        return Result.success(processType);
    }

    @ApiOperation("查詢所有審批類型")
    @GetMapping("/get/all")
    public Result getAll() {
        List<ProcessType> processTypeList = processTypeService.list();
        return Result.success(processTypeList);
    }

}

