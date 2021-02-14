package com.vranec.delimitation.backend.model;

import lombok.Data;

@Data
public class CreateNewGameRequest {
    private int width = 20;
    private int height = 10;
}
