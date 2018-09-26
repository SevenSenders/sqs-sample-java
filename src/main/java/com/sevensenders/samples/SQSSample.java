package com.sevensenders.samples;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;

public class SQSSample {

    public static void main(String[] args) {
        Properties cfg = new Properties();
        try {
            cfg.load(new FileInputStream("config.properties"));
        } catch (IOException ioe) {
            System.out.println("Error reading configuration: " + ioe.getMessage());
        }

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(cfg.getProperty("apiKey"), "");
        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withEndpointConfiguration(new EndpointConfiguration(
                        cfg.getProperty("endpoint", "https://analytics-api.7senders.com"), ""))
                .build();

        try {
            System.out.println("Getting Queue URL");
            String queue_url = sqs.getQueueUrl("").getQueueUrl();
            System.out.println("Result: " + queue_url);

            System.out.println("Receiving messages...\n");

            List<Message> messages = sqs.receiveMessage("").getMessages();

            for (Message m : messages) {
                System.out.println(m.getMessageId() + ":");
                System.out.println(m.getBody());
            }

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to SQS Proxy, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with Endpoint, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
