package org.gty.demo.job;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class DemoJob extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(DemoJob.class);

    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    protected void executeInternal(@Nonnull final JobExecutionContext context) {
        Objects.requireNonNull(context, "context must not be null");

        log.debug("[Quartz] --- {}", info);
    }
}
