package infrastructure.adapter.processing;

import domain.model.JsonMessage;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SparkProcessor {

    private final SparkSession sparkSession;

    public Mono<String> processJson(JsonMessage json1, JsonMessage json2) {
        Dataset<Row> dataset1 = sparkSession.read().json(sparkSession.createDataset(
                java.util.List.of(json1.getContent()), org.apache.spark.sql.Encoders.STRING()));
        Dataset<Row> dataset2 = sparkSession.read().json(sparkSession.createDataset(
                java.util.List.of(json2.getContent()), org.apache.spark.sql.Encoders.STRING()));

        Dataset<Row> result = dataset1.union(dataset2);

        return Mono.just(result.toJSON().collectAsList().toString());
    }
}