package com.nadia.openplatform.common.enumerate;

import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

public interface EnumCode {

   String getCode();

   static <T extends EnumCode> T codeOf(Class<?> enumType, String code){
       if(ClassUtils.isAssignable(EnumCode.class,enumType)) {
           Method method = ReflectionUtils.findMethod(enumType, "values");
           ReflectionUtils.makeAccessible(method);
           T[] values = (T[]) ReflectionUtils.invokeMethod(method, null);
           for (T each : values) {
               if (each.getCode().equals(code)) {
                   return each;
               }
           }
       }
       return null;
   }
}
