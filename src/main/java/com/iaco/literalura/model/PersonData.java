package com.iaco.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record PersonData(@JsonAlias("name") String name,
                         @JsonAlias("birth_year") Integer birthYear,
                         @JsonAlias("death_year") Integer deathYear) {
}