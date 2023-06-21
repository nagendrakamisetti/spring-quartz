package com.mighty.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
public class ExampleJob implements Job {

    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("Start ExampleJob {}", LocalDateTime.now());
        try {
            //Thread.sleep(3000);
            Thread.sleep(2);
        } catch (InterruptedException e) {
            log.info("InterruptedException ExampleJob");
            e.printStackTrace();
        }
        log.info("End ExampleJob {}", LocalDateTime.now());
    }
}

