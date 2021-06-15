package ru.hse.rekoder.repositories.mongodb;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.MongoCredential;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;


import javax.validation.Valid;
import java.util.Collections;

@Configuration
@PropertySource("classpath:application.properties")
@EnableMongoRepositories(basePackages = "ru.hse.rekoder.repositories")
public class MongoConfiguration extends AbstractMongoClientConfiguration {
    private final String user;
    private final String password;
    private final String databaseName;
    private final String host;

    public MongoConfiguration(@Value("${database.user}") String user,
                              @Value("${database.password}") String password,
                              @Value("${database.database_name}") String databaseName,
                              @Value("${database.host}") String host) {
        this.user = user;
        this.password = password;
        this.databaseName = databaseName;
        this.host = host;
    }

    @Override
    public MongoClient mongoClient() {
        MongoClient mongoClient = MongoClients.create(
                "mongodb+srv://"+ user + ":" + password +
                        "@" + host + "?retryWrites=true&w=majority");
        return mongoClient;
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