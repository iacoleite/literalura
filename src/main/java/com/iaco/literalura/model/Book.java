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
    private List<String> languages;
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

//    public void setAuthor(List<PersonData> author) {
//        List<Person> authorsPerson = author.stream()
//                .map(Person::new)
//                .collect(Collectors.toList());
//        this.author.add(authorsPerson);
//    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author=" + authors.toString() +
                ", languages='" + languages + '\'' +
                ", downloadCount=" + downloadCount +
                '}';
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
}