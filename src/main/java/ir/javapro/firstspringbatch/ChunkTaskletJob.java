package ir.javapro.firstspringbatch;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ChunkTaskletJob {

    @Bean
    public Job sampleJob(JobRepository jobRepository,
                         @Qualifier("taskletStep") Step taskletStep,
                         @Qualifier("seccondeChunkStep") Step seccondeChunkStep) {
        return new JobBuilder(jobRepository)
                .start(taskletStep)
                .next(seccondeChunkStep)
                .build();
    }

    @Bean
    public Step taskletStep(JobRepository jobRepository) {
        return new StepBuilder(jobRepository)
                .tasklet((t1, t2) -> {
                    System.out.println("this is tasklet");
                    return RepeatStatus.FINISHED;
                }).build();

    }

    @Bean
    public Step seccondeChunkStep(JobRepository jobRepository) {
        return new StepBuilder(jobRepository)
                .<String, String>chunk(2)
                .reader(itemReader())
                .writer(itemWriter())
                .processor(itemProcessor())
                .build();
    }

    @Bean
    public ItemReader<String> itemReader() {
        return new ListItemReader<String>(
                Arrays.asList("Mahdi", "Amir", "Nika", "Fatemeh")
        );
    }


    @Bean
    public ItemProcessor<String, String> itemProcessor() {
        return item -> item.toUpperCase();
    }

    @Bean
    public ItemWriter<String> itemWriter() {
        return item -> item.forEach(System.out::println);
    }


}
