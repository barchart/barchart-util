package com.barchart.util.guice;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import com.google.inject.TypeLiteral;

@Qualifier
@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {

    /** The name. */
    String value() default "";
    
//	TypeLiteral<?> literal();
}

