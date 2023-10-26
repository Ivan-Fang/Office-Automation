package com.ivanfang.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ivanfang.common.result.Result;
import com.ivanfang.model.process.ProcessTemplate;
import com.ivanfang.process.service.ProcessTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "審批模板管理介面")
@RestController
@RequestMapping("/admin/process/processTemplate")
public class ProcessTemplateController {

    @Autowired
    private ProcessTemplateService processTemplateService;

    @ApiOperation("新增審批模板")
//    @PreAuthorize("hasAuthority('btn.processTemplate.add')")
    @PostMapping("/save")
    public Result save(@RequestBody ProcessTemplate processTemplate) {
        processTemplateService.save(processTemplate);
        return Result.success();
    }

    @ApiOperation("根據 id 刪除審批模板")
//    @PreAuthorize("hasAuthority('btn.processTemplate.remove')")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable Integer id) {
        processTemplateService.removeById(id);
        return Result.success();
    }

    @ApiOperation("修改審批模板")
//    @PreAuthorize("hasAuthority('btn.processTemplate.update')")
    @PutMapping("/update")
    public Result update(@RequestBody ProcessTemplate processTemplate) {
        processTemplateService.updateById(processTemplate);
        return Result.success();
    }

    @ApiOperation("根據頁面查詢審批模板")
//    @PreAuthorize("hasAuthority('btn.processTemplate.list')")
    @GetMapping("/get/{page}/{pageSize}")
    public Result getByPage(@PathVariable Integer page, @PathVariable Integer pageSize) {
        Page<ProcessTemplate> pageParam = new Page<>(page, pageSize);
        Page<ProcessTemplate> pages = processTemplateService.selectPageProcessTemplate(pageParam);    // ProcessTemplate 中有一個欄位是資料庫所沒有的，沒有的那個欄位要由自己定義
        return Result.success(pages);
    }

    @ApiOperation("根據 id 查詢審批模板")
//    @PreAuthorize("hasAuthority('btn.processTemplate.get')")
    @GetMapping("/get/{id}")
    public Result getById(@PathVariable Integer id) {
        ProcessTemplate processTemplate = processTemplateService.getById(id);
        return Result.success(processTemplate);
    }

    @ApiOperation("查詢所有審批模板")
//    @PreAuthorize("hasAuthority('btn.processTemplate.get')")
    @GetMapping("/get/all")
    public Result getAll() {
        List<ProcessTemplate> processTemplateList = processTemplateService.list();
        return Result.success(processTemplateList);
    }

    // 將上傳上來的流程圖 zip 檔存到 classpath:/process/ 下
    @ApiOperation("上傳流程定義檔案")
    @PostMapping("/uploadProcessDefinition")
    public Result uploadProcessDefinition(@RequestParam("file") MultipartFile uploadedFile) throws IOException {
        // 獲取 classpath:/process/ 的絕對路徑
        String classpath = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath();
        File savedPath = new File(classpath + "/processes");
        if (!savedPath.exists()) {
            savedPath.mkdirs();
        }

        String filename = uploadedFile.getOriginalFilename();
        File savedFile = new File(savedPath.getPath() + "/" + filename);
        uploadedFile.transferTo(savedFile);

        Map<String, Object> map = new HashMap<>();
        map.put("processDefinitionPath", "processes/" + filename);
        map.put("processDefinitionKey", filename.substring(0, filename.lastIndexOf(".")));

        return Result.success(map);
    }

    @ApiOperation("發佈審批模板（部署流程定義）")
    @GetMapping("/publish/{id}")
    public Result publish(@PathVariable Integer id) {
        processTemplateService.publish(id);
        return Result.success();
    }

}

