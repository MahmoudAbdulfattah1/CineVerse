package com.cineverse.cineverse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterOption {
    private String label;
    private String value;

    public FilterOption(String label, String value) {
        this.label = label;
        this.value = value;
    }

}
