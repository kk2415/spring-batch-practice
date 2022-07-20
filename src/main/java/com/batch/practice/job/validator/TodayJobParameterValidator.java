package com.batch.practice.job.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Component
public class TodayJobParameterValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        if (parameters == null) {
            throw new JobParametersInvalidException("job parameter today is required");
        }

        String today = parameters.getString("today");
        if (today == null) {
            throw new JobParametersInvalidException("job parameter today is required");
        }

        try {
            LocalDate.parse(today);
        } catch (DateTimeParseException e) {
            throw new JobParametersInvalidException("job parameter today format is not valid");
        }
    }
}
