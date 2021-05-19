package ru.hse.rekoder.repositories.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.MongoCredential;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;


import java.util.Arrays;

@Configuration
@PropertySource("classpath:application.properties")
@EnableMongoRepositories(basePackages = "ru.hse.rekoder.repositories")
public class MongoConfiguration {
    @Value("${database_password}")
    private String password;

    @Bean
    public MongoClient mongo() throws Exception {
        // final ConnectionString connectionString = new ConnectionString("mongodb+srv://Admin:Admin@cluster0.q43vi.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
        String user = "root"; // the user name
        String database = "admin"; // the name of the database in which the user is defined
        char[] password = this.password.toCharArray(); // the password as a character array

        MongoCredential credential = MongoCredential.createCredential(user, database, password);

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToSslSettings(builder -> builder.enabled(false))
                .applyToClusterSettings(builder ->
                        builder.hosts(Arrays.asList(new ServerAddress("34.89.122.71", 27017))))
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), "admin");
    }
}