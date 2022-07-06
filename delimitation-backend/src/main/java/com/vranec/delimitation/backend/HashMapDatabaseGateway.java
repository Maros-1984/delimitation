package com.vranec.delimitation.backend;

import com.vranec.delimitation.backend.model.FullGameResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Profile("localhost")
class HashMapDatabaseGateway implements DatabaseGateway {

    private Map<String, FullGameResponse> datastore = new HashMap<>();

    public void save(FullGameResponse game) {
        datastore.put(game.getGameId(), copyOf(game));
    }

    private FullGameResponse copyOf(FullGameResponse game) {
        FullGameResponse copy = new FullGameResponse();
        BeanUtils.copyProperties(game, copy);
        return copy;
    }

    public FullGameResponse load(String gameId) {
        return datastore.get(gameId);
    }
}
