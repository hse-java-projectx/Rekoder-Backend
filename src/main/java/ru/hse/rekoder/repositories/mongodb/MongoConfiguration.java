package ru.hse.rekoder.repositories.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.MongoCredential;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;


import java.util.Collections;

@Configuration
@PropertySource("classpath:application.properties")
@EnableMongoRepositories(basePackages = "ru.hse.rekoder.repositories")
public class MongoConfiguration extends AbstractMongoClientConfiguration {
    @Value("${database_password}")
    private String password;
    private final String databaseName = "admin";

    @Override
    public MongoClient mongoClient() {
        String user = "root"; // the user name
        char[] password = this.password.toCharArray(); // the password as a character array

        MongoCredential credential = MongoCredential.createCredential(user, databaseName, password);

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToSslSettings(builder -> builder.enabled(false))
                .applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(new ServerAddress("34.89.122.71", 27017))))
                .build();

        return MongoClients.create(settings);
    }

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }
}