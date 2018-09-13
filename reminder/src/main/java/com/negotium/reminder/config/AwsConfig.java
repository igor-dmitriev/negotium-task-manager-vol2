package com.negotium.reminder.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.negotium.reminder.helper.AwsUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

  @Value("${aws.dynamodb.endpoint:#{null}}")
  private String endpoint;

  @Bean
  public AmazonDynamoDB dynamoDb() {
    AmazonDynamoDBClientBuilder client = AmazonDynamoDBClientBuilder.standard();
    if (StringUtils.isNotBlank(endpoint)) {
      client.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, AwsUtils.DEFAULT_REGION))
          .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("test_access_key", "test_secret_key")));
    } else {
      client.withRegion(AwsUtils.DEFAULT_REGION);
    }
    return client.build();
  }

  @Bean
  public DynamoDBMapper mapper() {
    return new DynamoDBMapper(dynamoDb());
  }
}
