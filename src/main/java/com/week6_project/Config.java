package com.week6_project;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClient;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

import static software.amazon.awssdk.regions.Region.US_EAST_2;


@Configuration
public class Config {

    @Bean
    @Primary
    public DataSource dataSource() {
        try {
            AWSSecretsManager client = AWSSecretsManagerClient.builder()
                    .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                    .withRegion(String.valueOf(US_EAST_2))
                    .build();

            GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest();
            String secretName = "Project1Part2DBCredentials";
            getSecretValueRequest.setSecretId(secretName);
            GetSecretValueResult getSecretValueResult = client.getSecretValue(getSecretValueRequest);
            String secretValue = getSecretValueResult.getSecretString();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode secretJson = mapper.readTree(secretValue);

            String host = secretJson.get("host").asText();
            String port = secretJson.get("port").asText();
            String dbname = secretJson.get("dbname").asText();
            String username = secretJson.get("username").asText();
            String password = secretJson.get("password").asText();

            String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;

            return DataSourceBuilder.create()
                    .url(jdbcUrl)
                    .username(username)
                    .password(password)
                    .driverClassName("org.postgresql.Driver")
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve database credentials from Secrets Manager", e);
        }
    }

    @Bean
    public AmazonS3 s3Client(){
        return AmazonS3Client
                .builder()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .withRegion(Regions.US_EAST_2)
                .build();
    }
}
