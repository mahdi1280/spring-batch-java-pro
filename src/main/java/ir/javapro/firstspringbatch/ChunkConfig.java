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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ChunkConfig {

    @Bean
    public ItemReader<Integer> reader() {
        return new ListItemReader<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

    @Bean
    public ItemProcessor<Integer, String> processor() {
        return item -> "Number: " + item;
    }

    @Bean
    public ItemWriter<String> writer() {
        return item -> item.forEach(System.out::println);
    }

    @Bean
    public Step chunkStep(JobRepository jobRepository) {
        return new StepBuilder("chunkStep", jobRepository)
                .<Integer, String>chunk(2)
                .reader(this.reader())
                .processor(this.processor())
                .writer(this.writer())
                .build();
    }

    @Bean
    public Job chunkJob(JobRepository jobRepository, @Qualifier("chunkStep") Step chunkStep) {
        return new JobBuilder("chunkJob", jobRepository)
                .start(chunkStep)
                .build();
    }
}
