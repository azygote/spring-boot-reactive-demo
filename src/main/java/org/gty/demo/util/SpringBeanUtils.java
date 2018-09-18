package org.gty.demo.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Objects;

@Component
public final class SpringBeanUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext ctx) throws BeansException {
        applicationContext = Objects.requireNonNull(ctx, "ctx must not be null");
    }

    @Nonnull
    public static <T> T getBean(@Nonnull Class<T> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");

        return applicationContext.getBean(clazz);
    }

    @Nonnull
    public static <T> T getBean(@Nonnull String name, @Nonnull Class<T> clazz) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(clazz, "clazz must not be null");

        return applicationContext.getBean(name, clazz);
    }
}
