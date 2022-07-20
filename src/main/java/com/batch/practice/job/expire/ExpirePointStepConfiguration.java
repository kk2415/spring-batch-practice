package com.batch.practice.job.expire;

import com.batch.practice.point.Point;
import com.batch.practice.point.PointRepository;
import com.batch.practice.point.wallet.PointWallet;
import com.batch.practice.point.wallet.PointWalletRepository;
import com.batch.practice.reader.ReverseJpaPagingItemReader;
import com.batch.practice.reader.ReverseJpaPagingItemReaderBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.Map;

@Configuration
public class ExpirePointStepConfiguration {

    @Bean
    @JobScope //Job에서 스탭을 실행할 때 Lazy하게 생성함
    public Step expirePointStep(
        StepBuilderFactory stepBuilderFactory,
        PlatformTransactionManager platformTransactionManager,
        ReverseJpaPagingItemReader<Point> expirePointItemReader,
        ItemProcessor<Point, Point> expirePointItemProcessor,
        ItemWriter<Point> expirePointItemWriter
    ) {
        return stepBuilderFactory
                .get("expirePointStep") //Step 이름
                .allowStartIfComplete(true) //Step 중복 실행 가능
                .transactionManager(platformTransactionManager)
                .<Point, Point>chunk(1000) //1000번씩 끊어서 작업
                .reader(expirePointItemReader)
                .processor(expirePointItemProcessor)
                .writer(expirePointItemWriter)
                .build();
    }


    @Bean
    @StepScope
    public ReverseJpaPagingItemReader<Point> expirePointItemReader(
            PointRepository pointRepository,
            @Value("#{T(java.time.LocalDate).parse(jobParameters[today])}") LocalDate today
    ) {
        return new ReverseJpaPagingItemReaderBuilder<Point>()
                .name("messageExpireSoonPointItemReader")
                .query(
                        pageable -> pointRepository.findPointToExpire(today, pageable)
                )
                .pageSize(1000)
                .sort(Sort.by(Sort.Direction.ASC, "id"))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Point, Point> expirePointItemProcessor() {
        return point -> {
            point.setExpired(true);
            PointWallet wallet = point.getPointWallet();
            wallet.setAmount(wallet.getAmount() - point.getAmount());
            return point;
        };
    }

    @Bean
    @StepScope
    public ItemWriter<Point> expirePointItemWriter(
            PointRepository pointRepository,
            PointWalletRepository pointWalletRepository
    ) {
        return points -> {
            for (Point point : points) {
                if (point.isExpired()) {
                    pointRepository.save(point);
                    pointWalletRepository.save(point.getPointWallet());
                }
            }
        };
    }

}
