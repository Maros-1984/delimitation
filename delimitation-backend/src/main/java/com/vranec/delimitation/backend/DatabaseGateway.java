package com.vranec.delimitation.backend;

import com.vranec.delimitation.backend.model.FullGameResponse;

interface DatabaseGateway {
    void save(FullGameResponse game);

    FullGameResponse load(String gameId);
}
