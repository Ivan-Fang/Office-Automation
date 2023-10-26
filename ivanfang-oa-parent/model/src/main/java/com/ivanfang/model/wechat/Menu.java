package com.ivanfang.model.wechat;

import com.ivanfang.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "菜單")
@TableName("wechat_menu")
public class Menu extends BaseEntity {

    @ApiModelProperty(value = "id")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty(value = "名稱")
    private String name;

    @ApiModelProperty(value = "類型")
    private String type;

    @ApiModelProperty(value = "網頁 連結，使用者點擊功能表可打開連結")
    private String url;

    @ApiModelProperty(value = "功能表KEY值，用於消息介面推送")
    @TableField("menu_key")
    private String menuKey;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
