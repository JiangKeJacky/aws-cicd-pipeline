package com.example.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Manga
{
    private List<Title> titles;
    private String synopsis;
    private Integer volumes;
    private Double score;
    private String type;
    private Integer members;
    private Integer chapters;
    private Boolean publishing;
}
