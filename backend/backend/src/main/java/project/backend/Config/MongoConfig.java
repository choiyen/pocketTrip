package project.backend.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories(basePackages = "project.backend.Repository")
public class MongoConfig {

    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(MongoClients.create("mongodb://localhost:27017/travel"), "travel");
    }
}

