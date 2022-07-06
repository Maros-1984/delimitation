package com.vranec.delimitation.backend.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateNewGameRequest {
    private int width = 20;
    private int height = 10;
    private boolean againstComputer = true;
}
