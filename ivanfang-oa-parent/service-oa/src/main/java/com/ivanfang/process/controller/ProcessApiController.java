package com.ivanfang.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ivanfang.auth.service.SysUserService;
import com.ivanfang.common.result.Result;
import com.ivanfang.model.process.Process;
import com.ivanfang.model.process.ProcessTemplate;
import com.ivanfang.model.process.ProcessType;
import com.ivanfang.model.system.SysUser;
import com.ivanfang.process.service.ProcessService;
import com.ivanfang.process.service.ProcessTemplateService;
import com.ivanfang.process.service.ProcessTypeService;
import com.ivanfang.security.custom.LoginUserInfoHelper;
import com.ivanfang.vo.process.ApprovalVo;
import com.ivanfang.vo.process.ProcessFormVo;
import com.ivanfang.vo.process.ProcessVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "審批管裡-員工端")
@RestController
@RequestMapping("/admin/process")
@CrossOrigin
public class ProcessApiController {

    @Autowired
    private ProcessService processService;

    @Autowired
    private ProcessTypeService processTypeService;

    @Autowired
    private ProcessTemplateService processTemplateService;

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("查詢所有已發佈的審批類型及其模板")
    @GetMapping("/get/all/type/with/template")
    public Result getAllTypeWithTemplate() {
        List<ProcessType> processTypeList = processTypeService.getAllTypeWithTemplate();
        return Result.success(processTypeList);
    }

    @ApiOperation("根據 id 查詢審批模板")
    @GetMapping("/get/template/{templateId}")
    public Result getTemplateById(@PathVariable Integer templateId) {
        ProcessTemplate processTemplate = processTemplateService.getById(templateId);
        return Result.success(processTemplate);
    }

    @ApiOperation("啟動流程（創建流程實例）")
    @PostMapping("/start/up")
    public Result startUp(@RequestBody ProcessFormVo processFormVo) {
        processService.startUp(processFormVo);
        return Result.success();
    }

    @ApiOperation("查詢目前使用者的待辦任務（分頁查詢）")
    @GetMapping("/get/pending/{page}/{pageSize}")
    public Result getPending(@PathVariable Integer page, @PathVariable Integer pageSize) {
        Page<Process> pageParam = new Page(page, pageSize);
        Page<ProcessVo> pageModel = processService.getPending(pageParam);
        return Result.success(pageModel);
    }

    @ApiOperation("根據 process id 顯示審批詳情資訊")
    @GetMapping("/show/{processId}")
    public Result show(@PathVariable Integer processId) {
        Map<String, Object> processInfoMap = processService.show(processId);
        return Result.success(processInfoMap);
    }

    @ApiOperation("執行任務審批（核准或駁回）")
    @PostMapping("/approve")
    public Result approve(@RequestBody ApprovalVo approvalVo) {
        System.out.println("approvalVo = " + approvalVo);
        processService.approve(approvalVo);
        return Result.success();
    }

    @ApiOperation("查詢目前使用者已處理的任務（分頁查詢）")
    @GetMapping("/get/processed/{page}/{pageSize}")
    public Result getProcessed(@PathVariable Integer page, @PathVariable Integer pageSize) {
        Page<Process> pageParam = new Page<>(page, pageSize);
        Page<ProcessVo> pageModel = processService.getProcessed(pageParam);
        return Result.success(pageModel);
    }

    @ApiOperation("查詢目前使用者已發起的任務（分頁查詢）")
    @GetMapping("/get/started/{page}/{pageSize}")
    public Result getStarted(@PathVariable Integer page, @PathVariable Integer pageSize) {
        Page<Process> pageParam = new Page<>(page, pageSize);
        Page<ProcessVo> pageModel = processService.getStarted(pageParam);
        return Result.success(pageModel);
    }

    @ApiOperation("查詢目前使用者的基本資訊")
    @GetMapping("/get/user/info")
    public Result getUserInfo() {
        Long userId = LoginUserInfoHelper.getUserId();
        SysUser user = sysUserService.getById(userId);
        user.setRoleList(sysUserService.getRoleListById(userId));
        return Result.success(user);
    }

}
