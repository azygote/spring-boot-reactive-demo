package org.gty.demo.job.config;

import org.gty.demo.constant.SystemConstants;
import org.gty.demo.job.DemoJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class DemoJobConfig {

    @Bean
    public JobDetail demoJobDetail() {
        return JobBuilder.newJob(DemoJob.class)
                .withIdentity("demoJob")
                .usingJobData("info", "job demonstration.")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger demoJobTrigger(JobDetail demoJobDetail) {
        var scheduleBuilder
                = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(15)
                .repeatForever();

        var cronScheduleBuilder = CronScheduleBuilder
                .dailyAtHourAndMinute(1, 0)
                .inTimeZone(TimeZone.getTimeZone(SystemConstants.defaultTimeZone))
                .build();

        return TriggerBuilder.newTrigger()
                .forJob(demoJobDetail)
                .withIdentity("demoJobTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
