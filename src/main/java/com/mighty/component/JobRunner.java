package com.mighty.component;

import com.mighty.jobs.ExampleJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobRunner {

    private static final String JOB_NAME = "WATCH-CARD-JOB";
    private static final String JOB_GROUP = "MIGHTY-JOBS";

    private static final JobKey JOB_KEY = JobKey.jobKey(JOB_NAME, JOB_GROUP);
    private static final TriggerKey TRIGGER_KEY = TriggerKey.triggerKey(JOB_NAME, JOB_GROUP);
    @Autowired
    private Scheduler scheduler;
    boolean test = true;
    /**
     * For every 1 hour - 60 * 60 * 1000
     */
    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void populateApiUsageForFailedEntries() throws SchedulerException {
        if (!scheduler.checkExists(JOB_KEY)) {
            JobDetail job = buildJobDetail();
            scheduler.scheduleJob(job, buildTrigger(job));
        } else {
            if (test) {
                Trigger t = scheduler.getTrigger(TRIGGER_KEY);
                TriggerBuilder triggerBuilder = t.getTriggerBuilder();
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule("0 0/2 * 1/1 * ? *"));
                scheduler.rescheduleJob(TRIGGER_KEY, triggerBuilder.build());
                test = false;
            }
        }
    }


    private JobDetail buildJobDetail() {
        JobDataMap map = new JobDataMap();
        map.put("key100", "value100");
        return JobBuilder.newJob(ExampleJob.class)
                .withIdentity(JOB_KEY)
                .withDescription("Job, Exports watch job cards to a folder")
                .usingJobData(map)
                .storeDurably()
                .build();
    }

    private Trigger buildTrigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(TRIGGER_KEY)
                /*.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(2, 22))*/
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * 1/1 * ? *"))
                .withDescription("Trigger, Exports watch job cards to a folder")
                .build();
    }
}
