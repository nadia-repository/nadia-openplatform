package com.nadia.openplatform.common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD})
@Retention(RUNTIME)
public @interface PropertyMapping {

    Source[] value();

    @interface Source {

        Class<?> clazz();

        String name();
    }
}
