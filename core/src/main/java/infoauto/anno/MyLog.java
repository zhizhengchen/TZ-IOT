package infoauto.anno;


import infoauto.enums.LogType;
import infoauto.enums.SaveDirection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyLog {
    LogType type() default LogType.BUSINESS;
    SaveDirection[] directions() default SaveDirection.ELASTICSEARCH;
}
