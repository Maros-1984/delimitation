package com.vranec.delimitation.backend;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.testing.LocalDatastoreHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
public class TestDatastoreConfiguration {
    private LocalDatastoreHelper localDatastoreHelper;

    @Bean
    @Primary
    public Datastore getDatastore() {
        localDatastoreHelper = LocalDatastoreHelper.create();
        try {
            localDatastoreHelper.start();
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
        System.setProperty("DATASTORE_EMULATOR_HOST", "localhost:" + localDatastoreHelper.getPort());
        return DatastoreOptions.getDefaultInstance().getService();
    }

    @PreDestroy
    public void stopLocalDatastore() {
        try {
            localDatastoreHelper.stop();
        } catch (IOException | InterruptedException | TimeoutException e) {
            throw new IllegalStateException(e);
        }
    }
}
