package com.iaco.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookData (@JsonAlias("title") String title,
                        @JsonAlias("languages") List<String> languages,
                        @JsonAlias("download_count") Integer downloadCount,
                        @JsonAlias("authors") List<PersonData> authors) {}


