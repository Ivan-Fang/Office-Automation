package com.ivanfang.vo.system;

import lombok.Data;

import java.util.List;

/**
 * 路由配置資訊
 *
 */
@Data
public class RouterVo
{
    /**
     * 路由名字
     */
    //private String name;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 是否隱藏路由，當設置 true 的時候該路由不會再側邊欄出現
     */
    private boolean hidden;

    /**
     * 組件位址
     */
    private String component;

    /**
     * 當你一個路由下面的 children 聲明的路由大於1個時，自動會變成嵌套的模式--如元件頁面
     */
    private Boolean alwaysShow;

    /**
     * 其他元素
     */
    private MetaVo meta;

    /**
     * 子路由
     */
    private List<RouterVo> children;


}
