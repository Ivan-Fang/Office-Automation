package com.ivanfang.code;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/*
    直接從 mybatis plus 上下載即可，不用記
 */

public class CodeGet {

    public static void main(String[] args) {
        // 1、創建程式碼生成器
        AutoGenerator mpg = new AutoGenerator();

        // 2、全域配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir("C:\\Users\\zenbook-14x\\Desktop\\Backend\\project-office-automation\\ivanfang-oa-parent\\service-oa" + "/src/main/java");

        gc.setServiceName("%sService");	// 去掉 Service 介面的首字母 I
        gc.setAuthor("ivanfang");
        gc.setOpen(false);
        mpg.setGlobalConfig(gc);

        // 3、配置資料來源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/ivanfang-oa");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 4、配置套件
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.ivanfang");
        pc.setModuleName("process");         // 模組名
        pc.setController("controller");
        pc.setService("service");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        // 5、配置策略
        StrategyConfig strategy = new StrategyConfig();

        strategy.setInclude("process_record");                                 // 要生成的 table 名稱

        strategy.setNaming(NamingStrategy.underline_to_camel);          // table 與 entity 之間的映射方式

        strategy.setColumnNaming(NamingStrategy.underline_to_camel);    // table column 與 entity field 之間的映射方式
        strategy.setEntityLombokModel(true);                            // lombok 模型 @Accessors(chain = true) setter 鏈式操作

        strategy.setRestControllerStyle(true);                          // restful api 風格控制器
        strategy.setControllerMappingHyphenStyle(true);                 // url 中駝峰轉連字號

        mpg.setStrategy(strategy);

        // 6、執行
        mpg.execute();
    }

}

