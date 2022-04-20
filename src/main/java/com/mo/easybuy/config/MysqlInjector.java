package com.mo.easybuy.config;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.additional.InsertBatchSomeColumn;

import java.util.List;

/**
 * author mozihao
 * create 2022-04-20 14:59
 * Description 新增自定义sql注入器
 */
public class MysqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList() {
        List<AbstractMethod> methodList = super.getMethodList();
        //扩充批量插入的方法
        methodList.add(new InsertBatchSomeColumn());
        return methodList;
    }
}
