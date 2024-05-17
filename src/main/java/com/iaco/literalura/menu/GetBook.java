package com.iaco.literalura.menu;

import com.iaco.literalura.model.*;
import com.iaco.literalura.repository.BookRepository;
import com.iaco.literalura.repository.PersonRepository;
import com.iaco.literalura.service.ApiInvoker;
import com.iaco.literalura.service.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GetBook {
    ApiInvoker apiInvoker = new ApiInvoker();
    JsonConverter converter = new JsonConverter();

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    PersonRepository personRepository;

    public GetBook(BookRepository bookRepository, PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }
    @Transactional
    public void getDataBook(String livro) {
        String json = apiInvoker.getData(livro);
        Data data = converter.getData(json, Data.class);
        try {
            BookData dadosLivro = data.books().get(0);

            Book book = new Book(dadosLivro);
            if (bookRepository.findByTitle(book.getTitle()) != null) {
                System.out.println("Livro Já pesquisado, recuperando dados do Banco de dados:");
                System.out.println(bookRepository.findByTitle(book.getTitle()).toString());
            } else {
                List<Person> authors = new ArrayList<>();
                for (PersonData authordata : dadosLivro.authors()) {
                    Person author = new Person(authordata);
                    List<Person> existingAuthors = personRepository.findByName(author.getName());
                    if (existingAuthors.isEmpty()) {
                        author = personRepository.save(author); // Save the new author to the database
                    } else {
                        author = existingAuthors.get(0); // Use the existing author from the database
                    }
                    authors.add(author);
                }
                book.setAuthors(authors);
                bookRepository.save(book); // Save the book with the list of authors
                System.out.println(book);
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Livro não encontrado");

        }
    }
}


