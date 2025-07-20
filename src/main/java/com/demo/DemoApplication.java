package com.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);

        try {
            JobLauncher jobLauncher = context.getBean(JobLauncher.class);
            Job myJob = context.getBean("emailSendJob", Job.class);

            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis()) // 중복 실행 허용
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(myJob, params);
            System.out.println("Job Exit Status: " + execution.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }

        context.close();
    }
}
