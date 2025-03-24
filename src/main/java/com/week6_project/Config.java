package com.week6_project;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class Config {

    private ParameterStoreService parameterStoreService;

    @Primary
    @Bean
    public String databasePassword(ParameterStoreService parameterStoreService) {
        return parameterStoreService.getDatabasePassword();
    }


    @Bean
    public AmazonS3 s3client(){
        return AmazonS3Client
                .builder()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .withRegion(Regions.US_EAST_1)
                .build();
    }
}
