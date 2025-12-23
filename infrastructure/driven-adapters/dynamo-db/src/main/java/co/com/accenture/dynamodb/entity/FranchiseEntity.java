package co.com.accenture.dynamodb.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.util.List;

/* Enhanced DynamoDB annotations are incompatible with Lombok #1932
         https://github.com/aws/aws-sdk-java-v2/issues/1932*/
@DynamoDbBean
public class FranchiseEntity {

    private String id;
    private String name;
    private List<SubsidiaryEntity> subsidiaries;

    public FranchiseEntity() {
    }

    public FranchiseEntity(String id, String name, List<SubsidiaryEntity> subsidiaries) {
        this.id = id;
        this.name = name;
        this.subsidiaries = subsidiaries;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "franchise-name-index")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDbAttribute("subsidiaries")
    public List<SubsidiaryEntity> getSubsidiaries() {
        return subsidiaries;
    }
    public void setSubsidiaries(List<SubsidiaryEntity> subsidiaries) {
        this.subsidiaries = subsidiaries;
    }
}
