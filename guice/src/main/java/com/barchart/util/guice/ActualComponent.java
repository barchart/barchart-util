package com.barchart.util.guice;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.google.inject.ScopeAnnotation;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RUNTIME)
@ScopeAnnotation
public @interface ActualComponent {

}

