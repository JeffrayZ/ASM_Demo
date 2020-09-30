package com.test.lib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Package: com.test.asm_demo
 * @ClassName: TrackMethod
 * @Description: 
 * @Author: Jeffray
 * @CreateDate: 2020/9/29 15:11
 * @UpdateUser: 
 * @UpdateDate: 
 * @UpdateRemark: 
 * @Version: 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface TrackMethod {
    String tag();
}
