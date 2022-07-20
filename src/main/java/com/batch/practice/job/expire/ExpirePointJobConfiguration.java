package com.batch.practice.job.expire;

import com.batch.practice.job.validator.TodayJobParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExpirePointJobConfiguration {

    @Bean
    public Job expirePointJob(
            JobBuilderFactory jobBuilderFactory,
            TodayJobParameterValidator validator,
            Step expirePointStep
    ) {
        return jobBuilderFactory.get("expirePointJob")
                .validator(validator) //validator 추가
                //같은 Job을 같은 JobParameter로 돌려도 해당 ID가 계속 증가하여 job 이 중복실행된 것으로 인지하지 않음
                .incrementer(new RunIdIncrementer())
                .start(expirePointStep)
                .build();
    }

}
