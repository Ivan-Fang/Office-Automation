package com.ivanfang.model.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class BaseEntity implements Serializable {

    @TableId(type = IdType.AUTO)    // type: increment method of the primary key, IdType.AUTO: auto_increment
    private Long id;                // mybatis-plus will set this field for java objects after selecting

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    @TableLogic                     // @TableLogic: logic delete (delete the data, but the data still exists, it just cannot be queried)
    @TableField("is_deleted")       // 0: is not deleted, 1: is deleted
    private Integer isDeleted;

    @TableField(exist = false)      // "exist = false": the table doesn't need to have this field
    private Map<String,Object> param = new HashMap<>();
}
