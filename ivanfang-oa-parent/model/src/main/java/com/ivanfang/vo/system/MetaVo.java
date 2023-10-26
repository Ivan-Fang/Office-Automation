package com.ivanfang.vo.system;

import lombok.Data;

/**
 * 路由顯示資訊
 *
 */
@Data
public class MetaVo
{
    /**
     * 設置該路由在側邊欄和麵包屑中展示的名字
     */
    private String title;

    /**
     * 設置該路由的圖示，對應路徑src/assets/icons/svg
     */
    private String icon;

    public MetaVo()
    {
    }

    public MetaVo(String title, String icon)
    {
        this.title = title;
        this.icon = icon;
    }

}

