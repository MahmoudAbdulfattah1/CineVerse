package com.cineverse.cineverse.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilterSection {
    private String title;
    private String key;
    private List<FilterOption> options;
    private boolean multiple;

    public FilterSection(String title, String key, List<FilterOption> options, boolean multiple) {
        this.title = title;
        this.key = key;
        this.options = options;
        this.multiple = multiple;
    }

}
