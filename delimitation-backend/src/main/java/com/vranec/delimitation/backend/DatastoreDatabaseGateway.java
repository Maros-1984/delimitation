package com.vranec.delimitation.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.StringValue;
import com.vranec.delimitation.backend.model.FullGameResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!localhost")
class DatastoreDatabaseGateway implements DatabaseGateway {
    @Autowired
    private Datastore datastore;
    @Autowired
    private ObjectMapper objectMapper;

    public void save(FullGameResponse game) {
        Key key = getKey(game.getGameId());
        Entity.Builder entityBuilder = Entity.newBuilder(key);
        try {
            entityBuilder.set("game", StringValue.newBuilder(objectMapper.writeValueAsString(game))
                    .setExcludeFromIndexes(true).build());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
        Entity entity = entityBuilder.build();
        datastore.put(entity);
    }

    private Key getKey(String gameId) {
        return datastore.newKeyFactory().setKind("DelimitationGame").newKey(gameId);
    }

    public FullGameResponse load(String gameId) {
        Entity entity = datastore.get(getKey(gameId));
        String game = entity.getString("game");
        try {
            return objectMapper.readValue(game, FullGameResponse.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
