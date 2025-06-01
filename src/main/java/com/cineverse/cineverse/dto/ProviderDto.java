package com.cineverse.cineverse.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProviderDto {
    private String name;
    private String logo;

    public ProviderDto(String name, String logo) {
        this.name = name;
        this.logo = logo;
    }
}
