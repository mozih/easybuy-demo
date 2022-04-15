package com.mo.easybuy.utils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * author mozihao
 * create 2022-03-03 14:12
 * Description
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入数据时填充到数据库
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //先获取到comMarknumber的值，再进行判断，如果为空，则进行填充，如果不为空，则不作处理
        Object comMarknumber = getFieldValByName("comMarknumber", metaObject);
        Object comScan = getFieldValByName("comScan", metaObject);

        if (null == comMarknumber){
            setFieldValByName("comMarknumber",0L,metaObject);
        }
        if (null == comScan){
            setFieldValByName("comScan",0L,metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
