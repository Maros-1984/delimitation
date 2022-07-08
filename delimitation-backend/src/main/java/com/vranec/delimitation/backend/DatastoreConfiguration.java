package com.vranec.delimitation.backend;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class DatastoreConfiguration {
    @Bean
    @Profile("!localhost")
    public Datastore getDatastore() {
        return DatastoreOptions.getDefaultInstance().getService();
    }
}
