package com.sevensenders.samples;

import java.util.List;
import java.util.ArrayList;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequest;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry;

public class SQSSample {

    public static void main(String[] args) {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials("sevensenders-api-key", "");
        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withEndpointConfiguration(new EndpointConfiguration("https://analytics-api.7senders.com/queue.xml", ""))
                .build();

        try {
            //Example for receiving messages
            System.out.println("Getting Queue URL");
            String queueUrl = sqs.getQueueUrl("").getQueueUrl();
            System.out.println("Result: " + queueUrl);

            System.out.println("Receiving messages...\n");

            List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();

            for (Message m : messages) {
                System.out.println(m.getMessageId() + ":");
                System.out.println(m.getBody());

                // Process the message...

                // Example for deleting messages one by one
                sqs.deleteMessage(queueUrl, m.getReceiptHandle());
                System.out.println("Message deleted: " + m.getMessageId());
            }

            // Example for deleting messages in batch
            List<DeleteMessageBatchRequestEntry> deleteEntries = new ArrayList<>();
            for (Message m : messages) {
                deleteEntries.add(new DeleteMessageBatchRequestEntry(m.getMessageId(), m.getReceiptHandle()));
            }

            if (!deleteEntries.isEmpty()) {
                DeleteMessageBatchRequest deleteRequest = new DeleteMessageBatchRequest(queueUrl, deleteEntries);
                sqs.deleteMessageBatch(deleteRequest);
                System.out.println("Messages deleted in batch");
            }

        } catch (AmazonServiceException ase) {
            System.out.println("AmazonServiceException: Your request made it to SQS Proxy, but was rejected");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("AmazonClientException: Request failed.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
