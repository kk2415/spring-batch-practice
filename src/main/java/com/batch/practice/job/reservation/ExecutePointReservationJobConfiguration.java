package com.batch.practice.job.reservation;

import com.batch.practice.job.validator.TodayJobParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutePointReservationJobConfiguration {

    @Bean
    public Job executePointReservationJob(
            JobBuilderFactory jobBuilderFactory,
            TodayJobParameterValidator validator,
            Step executePointReservationStep
    ) {
        return jobBuilderFactory
                .get("executePointReservationJob") //job 이름
                .validator(validator)
                .incrementer(new RunIdIncrementer())
                .start(executePointReservationStep)
                .build();
    }

}
