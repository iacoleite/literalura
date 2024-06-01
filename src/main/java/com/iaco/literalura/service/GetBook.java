package com.iaco.literalura.service;

import com.iaco.literalura.model.*;
import com.iaco.literalura.repository.BookRepository;
import com.iaco.literalura.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
                System.out.println("Livro já pesquisado, recuperando dados do Banco de dados:"+ "\n");
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
            System.out.println("Livro não encontrado"+ "\n");

        }
    }

    public void getTop(String url) {
        String json = apiInvoker.getData(url);
        Data data = converter.getData(json, Data.class);
        for (int i = 0; i < 10; i++) {
            try {
                BookData dadosLivro = data.books().get(i);
                Book book = new Book(dadosLivro);
                if (bookRepository.findByTitle(book.getTitle()) != null) {
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
                System.out.println("Livro não encontrado" + "\n");

            }
        }
    }

    public void getAuthor(String search) {
        String json = apiInvoker.getData("https://gutendex.com/books/?search=" + search);
        Data data = converter.getData(json, Data.class);
        try {
            BookData dadosLivro = data.books().get(0);
            Book book = new Book(dadosLivro);
            String normalizedSearch = search.toLowerCase();
            // busca no banco
            List<Person> matchingAuthors = personRepository.findByNameContainingIgnoreCase(normalizedSearch);
            if (!matchingAuthors.isEmpty()) {
                System.out.println("Encontrado no banco de dados: " +"\n"+ matchingAuthors.get(0));
            } else {
                // se não encontrou no db o autor, busca pelo livro, isso depende da input do usuário na busca
                System.out.println("Autor não encontrado no banco de dados.");
                Book existingBook = bookRepository.findByTitle(book.getTitle());
                if (existingBook != null) {
                    List<Person> existingAuthors = existingBook.getAuthor().stream().filter(a -> a.getName().contains(normalizedSearch)).collect(Collectors.toList());
                    System.out.println(existingAuthors);
//                    System.out.println("Possível livro do autor encontrado: " + existingAuthors);
                    for (Person author : existingAuthors) {
                        String normalizedAuthorName = author.getName().toLowerCase().trim();
                        if (normalizedAuthorName.contains(normalizedSearch)) {
                            System.out.println("Found author in existing book: " + author);
                            return;
                        }
                    }
                } else {
                    //se não achar MESMO no banco de dados, aí busca um livro do autor
                    System.out.println("Autor não encontrado no banco de dados.");
                    // busca um livro do autor e insere no banco de dados.
                    List<Person> authors = new ArrayList<>();
                    for (PersonData authordata : dadosLivro.authors()) {
                        Person author = new Person(authordata);
                        List<Person> existingAuthorsList = personRepository.findByName(author.getName());
                        if (existingAuthorsList.isEmpty()) {
                            author = personRepository.save(author); // Save the new author to the database
                        } else {
                            author = existingAuthorsList.get(0); // Use the existing author from the database
                        }
                        authors.add(author);
                    }
                    book.setAuthors(authors);
                    bookRepository.save(book); // Save the book with the list of authors
                    System.out.println("Novo livro inserido no banco de dados: \n" + book);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Autor não encontrado.\n");
        }
    }
}


