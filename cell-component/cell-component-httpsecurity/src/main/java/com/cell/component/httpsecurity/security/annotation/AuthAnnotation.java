package com.cell.component.httpsecurity.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthAnnotation
{
    String[] allowRole() default "";

    String[] allowPermissions() default "";

    int PermissionType() default 1 << 0;

    int RoleType() default 1 << 0;
}
