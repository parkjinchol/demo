package com.demo.job;

import com.demo.dto.EmailRecipient;
import com.demo.mapper.EmlMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
@MapperScan("com.demo.mapper")
public class BatchConfig {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;




    @Bean
    public Job emailSendJob() {
        return new JobBuilder("emailSendJob", jobRepository)
                .start(emailSendStep(null))
                .build();
    }

    @Bean
    public Step emailSendStep(EmlMapper emlMapper) {
        return new StepBuilder("emailSendStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    List<Long> masterIds = emlMapper.selectAllMasterIds();
                    System.out.println(">> 총 masterId 수: " + masterIds.size());

                    for (Long masterId : masterIds) {
                        System.out.println(">> masterId 처리: " + masterId);

                        MyBatisCursorItemReader<EmailRecipient> reader = new MyBatisCursorItemReader<>();
                        reader.setSqlSessionFactory(sqlSessionFactory);
                        reader.setQueryId("com.demo.mapper.EmlMapper.selectRecipientsByMasterId");
                        reader.setParameterValues(Map.of("masterId", masterId));
                        reader.afterPropertiesSet();

                        reader.open(new ExecutionContext());

                        List<String> currentBatch = new ArrayList<>();
                        EmailRecipient recipient;
                        while ((recipient = reader.read()) != null) {
                            if (isValidEmail(recipient.getEmail())) {
                                currentBatch.add(recipient.getEmail());
                            }

                            if (currentBatch.size() == 200) {
                                sendEmail(masterId, currentBatch);
                                currentBatch.clear();
                            }
                        }

                        // 나머지 수신자 발송
                        if (!currentBatch.isEmpty()) {
                            sendEmail(masterId, currentBatch);
                        }

                        reader.close();
                    }

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public void sendEmail(Long masterId, List<String> emails) {
        System.out.printf(">> [masterId: %d] %d건 이메일 발송: %s%n",
                masterId, emails.size(), String.join(", ", emails));
        // 실제 이메일 발송 로직...
    }
}
