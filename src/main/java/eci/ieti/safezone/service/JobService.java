package eci.ieti.safezone.service;

import eci.ieti.safezone.model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<Job> jobTable;

    @Autowired
    public JobService(DynamoDbClient dynamoDbClient) {
        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
        this.jobTable = enhancedClient.table("jobs", TableSchema.fromBean(Job.class));
    }

    public void save(Job job) {
        jobTable.putItem(job);
    }

    public Optional<Job> findById(String id) {
        return Optional.ofNullable(jobTable.getItem(r -> r.key(k -> k.partitionValue(id))));
    }

    public List<Job> findAll() {
        List<Job> jobs = new ArrayList<>();
        jobTable.scan(ScanEnhancedRequest.builder().build())
                .items()
                .forEach(jobs::add);
        return jobs;
    }
}
