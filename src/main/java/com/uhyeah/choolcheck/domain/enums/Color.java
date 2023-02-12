package com.uhyeah.choolcheck.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Color {

    RED("#FFADAD"),
    ORANGE("#FFD6A5"),
    YELLOW("#FDFFB6"),
    GREEN("#CAFFBF"),
    LIGHT_BLUE("#9BF6FF"),
    BLUE("#A0C4FF"),
    PURPLE("#BDB2FF"),
    PINK("#FFC6FF"),
    GRAY("#DEDEDE");

    private final String code;
}
