package com.iaco.literalura.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Person> authors;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_languages", joinColumns = @JoinColumn(name = "book_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private List<Languages> languages;
    private Integer downloadCount;

    public Book(BookData bookData) {
        this.title = bookData.title();
        this.languages = bookData.languages();
        this.downloadCount = bookData.downloadCount();
    }

    public Book() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Person> getAuthor() {
        return authors;
    }


    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }


    public void addAuthor(Person person) {
        this.authors.add(person);
        if (!person.getBooks().contains(this)) {
            person.getBooks().add(this);
        }
    }

    public void setAuthors(List<Person> authors) {
        this.authors = authors;
        for (Person author : authors) {
            if (!author.getBooks().contains(this)) {
                author.getBooks().add(this);
            }
        }
    }

    public String getLanguages() {
        if (languages == null || languages.isEmpty()) {
            return "";
        } else {
            return String.valueOf(languages.get(0));
        }
    }

    @Override
    public String toString() {
        return "Título: " + title + '\n' +
                "Autor(es): " + "\n"  + authors.stream().map(author -> author.toString()).collect(Collectors.joining("\n")) + '\n' +
                "Idioma(s): " + languages.stream().map(Languages::getIdioma).collect(Collectors.joining(", ")) + '\n' +
                "Downloads: " + downloadCount+ "\n";
    }
}