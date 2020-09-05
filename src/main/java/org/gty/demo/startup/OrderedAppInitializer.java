package org.gty.demo.startup;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Qualifier
public @interface OrderedAppInitializer {

    int ORDER_WAIT_FIVE_SECONDS = 1;
    int ORDER_FLUSH_REDIS = 2;
    int ORDER_NEW_FEATURE = 3;
}
