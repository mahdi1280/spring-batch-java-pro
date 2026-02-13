package ir.javapro.firstspringbatch.controller;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class JobController {

    private final Job job;
    private final JobOperator jobOperator;


    public JobController(Job job, JobOperator jobOperator) {
        this.job = job;
        this.jobOperator = jobOperator;
    }

    @GetMapping
    public void runJob() throws JobInstanceAlreadyCompleteException, InvalidJobParametersException, JobExecutionAlreadyRunningException, JobRestartException {
        jobOperator.start(job, new JobParameters());
    }
}
