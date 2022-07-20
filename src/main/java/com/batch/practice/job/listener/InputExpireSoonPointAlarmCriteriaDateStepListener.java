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
public class InputExpireSoonPointAlarmCriteriaDateStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        //today jobParameter 를 가져옴
        // 오늘부터 7일 이내에 만료되는 포인트들을 알려주기 위해
        JobParameter jobParameter = stepExecution.getJobParameters().getParameters().get("today");
        if (jobParameter == null) {
            return ;
        }

        LocalDate today = LocalDate.parse((String) jobParameter.getValue());
        ExecutionContext executionContext = stepExecution.getExecutionContext();
        executionContext.put("alarmCriteriaDate", today.plusDays(7).format(DateTimeFormatter.ISO_DATE));
        stepExecution.setExecutionContext(executionContext);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

}
