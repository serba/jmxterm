package org.cyclopsgroup.jcli.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target( ElementType.FIELD )
@Retention( RetentionPolicy.RUNTIME )
public @interface Argument
{
    String description() default "";

    String displayName() default "";

    int requires() default 0;
}
