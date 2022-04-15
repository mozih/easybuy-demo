package com.mo.easybuy.config;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * author mozihao
 * create 2022-02-25 13:55
 * Description
 */
@Configuration
@MapperScan("com.mo.easybuy.mapper")//设置mapper的扫描包
public class MybatisPlusConfig {

    //配置分页插件
//    @Bean
//    public PaginationInterceptor paginationInterceptor(){
//        return new PaginationInterceptor();
//    }

    //SQL分析插件
    @Bean
    public SqlExplainInterceptor sqlExplainInterceptor(){
        SqlExplainInterceptor sqlExplainInterceptor = new SqlExplainInterceptor();
        List<ISqlParser> list = new ArrayList<>();
        //全表更新、删除的阻断器
        //攻击 SQL 阻断解析器、加入解析链
        list.add(new BlockAttackSqlParser());

        sqlExplainInterceptor.setSqlParserList(list);

        return sqlExplainInterceptor;
    }
}
