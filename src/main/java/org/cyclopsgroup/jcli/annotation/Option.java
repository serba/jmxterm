package org.cyclopsgroup.jcli.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target( ElementType.FIELD )
@Retention( RetentionPolicy.RUNTIME )
public @interface Option
{
    String defaultValue() default "";

    String description() default "";

    String displayName() default "";

    String longName() default "";

    String name();

    boolean required() default false;
}
