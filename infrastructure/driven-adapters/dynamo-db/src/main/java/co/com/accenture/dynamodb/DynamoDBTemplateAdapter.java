package co.com.accenture.dynamodb;

import co.com.accenture.dynamodb.entity.FranchiseEntity;
import co.com.accenture.dynamodb.helper.TemplateAdapterOperations;
import co.com.accenture.model.franchise.Franchise;
import co.com.accenture.model.franchise.gateways.FranchiseRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.Map;


@Repository
public class DynamoDBTemplateAdapter
        extends TemplateAdapterOperations<
        Franchise,
        String,
        FranchiseEntity
        > implements FranchiseRepository {

    public DynamoDBTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(connectionFactory, mapper, entity -> mapper.map(entity, Franchise.class), "franchises");
    }

    public Mono<List<Franchise>> getEntityBySomeKeys(String partitionKey, String sortKey) {
        QueryEnhancedRequest queryExpression = generateQueryExpression(partitionKey, sortKey);
        return query(queryExpression);
    }

    public Mono<List<Franchise>> getEntityBySomeKeysByIndex(String partitionKey, String sortKey) {
        QueryEnhancedRequest queryExpression = generateQueryExpression(partitionKey, sortKey);
        return queryByIndex(queryExpression);
    }

    private QueryEnhancedRequest generateQueryExpression(String partitionKey, String sortKey) {
        Key.Builder keyBuilder = Key.builder().partitionValue(partitionKey);

        if (sortKey != null && !sortKey.isEmpty()) {
            keyBuilder.sortValue(sortKey);
        }

        return QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(keyBuilder.build()))
                .build();
    }

    public Mono<List<Franchise>> getEntityByKeys(String partitionKey, String sortKey) {
        QueryEnhancedRequest queryExpression = generateQueryExpression(partitionKey, sortKey);
        return query(queryExpression);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        QueryEnhancedRequest query = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(Key.builder()
                        .partitionValue(name)
                        .build()))
                .build();

        return queryByIndex(query, "franchise-name-index")
                .flatMapMany(Flux::fromIterable)
                .hasElements();
    }

    @Override
    public Mono<Franchise> findById(String id) {
        QueryEnhancedRequest query = generateQueryExpression(id, null);
        return query(query)
                .flatMapMany(Flux::fromIterable)
                .next();
    }

    @Override
    public Flux<Franchise> findAll() {
        return Flux.from(table.scan().items())
                .map(entity -> mapper.map(entity, Franchise.class));
    }




}
