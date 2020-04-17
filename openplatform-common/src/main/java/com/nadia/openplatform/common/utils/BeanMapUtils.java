package com.nadia.openplatform.common.utils;

import com.nadia.openplatform.common.annotation.PropertyMapping;
import com.nadia.openplatform.common.enumerate.EnumCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.springframework.util.ReflectionUtils.findField;

@Slf4j
public class BeanMapUtils {

    public static <T,E> E map(T source, Class<E> targetClass){

        try {
            if(source==null){
                log.info("source is null,targetClass:{}", targetClass);
                return null;
            }
            E target = BeanUtils.instantiate(targetClass);
            copyProperties(source, target);
            return target;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;

    }

    public static <T,E> void mixin(T source,E target){
        copyProperties(source, target);
    }


    public static <T,E> List<E> mapList(List<T> sources, Class<E> targetClass){
        if(CollectionUtils.isEmpty(sources)){
            return Collections.emptyList();
        }

        try {
            List<E> result= new LinkedList<>();
            for(T each: sources) {
                E target = BeanUtils.instantiate(targetClass);
                copyProperties(each, target);
                result.add(target);
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return Collections.emptyList();

    }

    private static void copyProperties(Object source, Object target){

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();

        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null) {

                Map<Class<?>,String> mapping= (Map<Class<?>,String>)targetPd.getValue("propertyMapping");
                if(mapping== null) {
                    synchronized (actualEditable) {
                        if (targetPd.getValue("propertyMapping") == null){
                            String propertyName = targetPd.getName();
                            Field f = findField(actualEditable, propertyName);
                            mapping= Collections.EMPTY_MAP;
                            if (f!= null && f.isAnnotationPresent(PropertyMapping.class)) {
                                PropertyMapping propertyMapping= f.getAnnotation(PropertyMapping.class);
                                mapping= new LinkedHashMap<>();
                                for(PropertyMapping.Source each: propertyMapping.value()){
                                    mapping.put(each.clazz(),each.name());
                                }
                            }
                            targetPd.setValue("propertyMapping", mapping);
                        }
                        else{
                            mapping= (Map<Class<?>,String>)targetPd.getValue("propertyMapping");
                        }
                    }
                }
                Class<?> sourceClass= source.getClass();
                String sourcePropertyName= mapping.containsKey(sourceClass)? mapping.get(sourceClass): targetPd.getName();
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(sourceClass, sourcePropertyName);
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null){
                        Class<?> targetType= writeMethod.getParameterTypes()[0];
                        Class<?> sourceType= readMethod.getReturnType();
                        try {
                            if (ClassUtils.isAssignable(targetType,sourceType)) {
                                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                    readMethod.setAccessible(true);
                                }
                                Object value = readMethod.invoke(source);
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                writeMethod.invoke(target, value);
                            }
                            else if(ClassUtils.isAssignable(String.class,sourceType)
                                    && ClassUtils.isAssignable(EnumCode.class,targetType)){
                                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                    readMethod.setAccessible(true);
                                }
                                String code = (String)readMethod.invoke(source);
                                Object value= EnumCode.codeOf(targetType,code);
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                writeMethod.invoke(target, value);
                            }
                            else if(ClassUtils.isAssignable(String.class,targetType)
                                    && ClassUtils.isAssignable(EnumCode.class,sourceType)){
                                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                    readMethod.setAccessible(true);
                                }
                                EnumCode enumCode= ((EnumCode)readMethod.invoke(source));
                                Object value = enumCode!= null?enumCode.getCode(): null;
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                writeMethod.invoke(target, value);
                            }
                        } catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }
}



