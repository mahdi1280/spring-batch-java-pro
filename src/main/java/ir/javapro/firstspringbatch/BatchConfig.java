package ir.javapro.firstspringbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    //job
    //step

    @Bean
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("print", jobRepository)
                .start(step)
//                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step printStep(JobRepository jobRepository) {
        return new StepBuilder(jobRepository)
                .tasklet((t1, t2) -> {
                    System.out.println("Hello World");
                    return null;
                }).build();
    }
}
