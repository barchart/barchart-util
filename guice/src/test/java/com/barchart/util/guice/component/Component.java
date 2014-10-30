package com.barchart.util.guice.component;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Qualifier
@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {

    /** The name. */
    String value() default "";
}

