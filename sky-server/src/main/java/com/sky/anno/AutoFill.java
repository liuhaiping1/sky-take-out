package com.sky.anno;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义公共字段自动填充注解
 */
@Target(ElementType.METHOD)//作用范围
@Retention(RetentionPolicy.RUNTIME)//赋生命周期---什么时候使用
public @interface AutoFill {
    //枚举字段，用来标识新增还是修改
    OperationType value();
}
