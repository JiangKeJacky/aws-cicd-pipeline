package com.example.model;

import java.util.List;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class MangaResult
{
    private Pagination pagination;
    private List<Manga> data;
}
