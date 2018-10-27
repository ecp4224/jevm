package io.edkek.ethereum.jevm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Event {
    boolean isAnonymous() default false;
    String[] parameterNames() default {};
    String name() default "";
}
