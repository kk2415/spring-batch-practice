package com.batch.practice.job.expire;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpirePointJobConfiguration {

    @Bean
    public Job expirePointJob(
            JobBuilderFactory jobBuilderFactory
    ) {
        return jobBuilderFactory.get("expirePointJob")
                .start(step)
                .build();

    }

}
