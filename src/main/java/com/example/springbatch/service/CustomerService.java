package com.example.springbatch.service;

import com.example.springbatch.repository.CustomerRepository;
import com.example.springbatch.model.Customer;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService implements ICustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @Override
    public String getCustomer(Customer customer) {
        List<Customer> cus = customerRepository.findByUsername(customer.getUserName());
        System.out.println(cus.size());
        if (cus != null || cus.size() > 0) {
            if (cus.get(0).getPassword().equals(customer.getPassword())){
                return "Success";
            }
        }
        return "Failed";
    }

    @Override
    public String saveCsvData(){
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("StartAt: ", System.currentTimeMillis())
                    .toJobParameters();
            JobExecution execute = jobLauncher.run(job, jobParameters);
            if (ExitStatus.COMPLETED.equals(execute.getExitStatus())) {
                return "Successfully Data Saved!";
            }
        }  catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                JobParametersInvalidException | JobRestartException e) {
            e.printStackTrace();
        }
        return "Failed";
    }
}
