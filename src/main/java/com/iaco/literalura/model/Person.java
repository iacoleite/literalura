package com.iaco.literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")

public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer birthYear;
    private Integer deathYear;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
    private List<Book> books = new ArrayList<>();

    public Person(PersonData personData) {
        this.name = personData.name();
        this.setBirthYear(personData.birthYear());
        this.setDeathYear(personData.deathYear());
        this.books = new ArrayList<>();
    }

    public Person() {}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        if (birthYear == null) {
            this.birthYear = 0;
        } else {
            this.birthYear = birthYear;
        }
    }

    public int getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        if (deathYear == null) {
            this.deathYear = 0;
        } else {
            this.deathYear = deathYear;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
    @Override
    public String toString() {
        return "Nome: " + name + "\n" +
                "Nascido em: " + birthYear + "\n" +
                "Morto em: " + deathYear + "\n";
    }


}

