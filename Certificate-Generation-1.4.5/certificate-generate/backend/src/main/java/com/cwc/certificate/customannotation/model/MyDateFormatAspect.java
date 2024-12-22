package com.cwc.certificate.customannotation.model;

import com.cwc.certificate.customannotation.annotations.MyDateFormat;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/02/14
 */

@Aspect
@Component
public class MyDateFormatAspect {
    @Before("execution(* *(..)) && @annotation(myDateFormat)")
    public void formatDate(JoinPoint joinPoint, MyDateFormat myDateFormat) throws Throwable {
        Object target = joinPoint.getTarget();
        if (target != null) {
            for (Field field : target.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(MyDateFormat.class) && field.getType().equals(Date.class)) {
                    field.setAccessible(true);
                    Date fieldValue = (Date) field.get(target);
                    if (fieldValue != null) {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                        String formattedDate = formatter.format(fieldValue);
                        field.set(target, formattedDate);
                    }
                }
            }
        }
    }
}
