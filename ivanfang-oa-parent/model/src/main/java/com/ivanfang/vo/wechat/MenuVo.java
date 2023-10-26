package com.ivanfang.vo.wechat;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "菜單")
public class MenuVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "id")
    private Long parentId;

    @ApiModelProperty(value = "名稱")
    private String name;

    @ApiModelProperty(value = "類型")
    private String type;

    @ApiModelProperty(value = "url")
    private String url;

    @ApiModelProperty(value = "菜單key")
    private String meunKey;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "下級")
    @TableField(exist = false)
    private List<MenuVo> children;

}