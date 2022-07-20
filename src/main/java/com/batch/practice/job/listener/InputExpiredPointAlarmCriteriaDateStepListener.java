package com.batch.practice.job.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class InputExpiredPointAlarmCriteriaDateStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        //today jobParameter 를 가져옴
        JobParameter jobParameter = stepExecution.getJobParameters().getParameters().get("today");
        if (jobParameter == null) {
            return ;
        }

        LocalDate today = LocalDate.parse((String) jobParameter.getValue());
        ExecutionContext executionContext = stepExecution.getExecutionContext();
        executionContext.put("alarmCriteriaDate", today.minusDays(1).format(DateTimeFormatter.ISO_DATE));
        stepExecution.setExecutionContext(executionContext);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

}
