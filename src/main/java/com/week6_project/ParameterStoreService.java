package com.week6_project;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

@Service
public class ParameterStoreService {
    private SsmClient ssmClient;



    @Bean
    public String getDatabasePassword() {
        GetParameterRequest request = GetParameterRequest.builder()
                .name("/postgres/password")
                .withDecryption(true)
                .build();

        GetParameterResponse response = ssmClient.getParameter(request);
        return response.parameter().value();
    }
}

