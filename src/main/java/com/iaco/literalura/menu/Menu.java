package com.iaco.literalura.menu;

import com.iaco.literalura.model.*;
import com.iaco.literalura.repository.BookRepository;
import com.iaco.literalura.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Menu {
    Scanner sc = new Scanner(System.in);
    private List<Book> books = new ArrayList<>();
    private List<Person> authors = new ArrayList<>();

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    PersonRepository personRepository;

    public Menu(BookRepository bookRepository, PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }


    String splash = """
                   
                   ,gggg,                                        ,ggg,                                       \s
                  d8" "8I        I8                             dP""8I  ,dPYb,                               \s
                  88  ,dP        I8                            dP   88  IP'`Yb                               \s
               8888888P"    gg88888888                        dP    88  I8  8I                               \s
                  88        ""   I8                          ,8'    88  I8  8'                               \s
                  88        gg   I8   ,ggg,   ,gggggg,       d88888888  I8 dP gg      gg  ,gggggg,   ,gggg,gg\s
             ,aa,_88        88   I8  i8" "8i  dP""\""8I __   ,8"     88  I8dP  I8      8I  dP""\""8I  dP"  "Y8I\s
            dP" "88P        88  ,I8, I8, ,8I ,8'    8IdP"  ,8P      Y8  I8P   I8,    ,8I ,8'    8I i8'    ,8I\s
            Yb,_,d88b,,_  _,88,,d88b,`YbadP',dP     Y8Yb,_,dP       `8b,d8b,_,d8b,  ,d8b,dP     Y8,d8,   ,d8b,
             "Y8P"  "Y88888P""Y8P""Y888P"Y888P      `Y8"Y8P"         `Y8P'"Y88P'"Y88P"`Y8P      `YP"Y8888P"`Y8
                                                                                                             \s
            """;

    String firstMenu = """
                1 - Buscar livro pelo título
                2 - Listar livros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos em um determinado ano
                5 - Listar livros em determinado idioma
                6 - Listar Top 10 livros mais baixados
                7 - Buscar autor
                8 - Verificar percentual de livros por idioma
                
                0 - Sair
            """;
    String initial = "https://gutendex.com/books/?search=";


    public void exibirMenu() {
        System.out.println(splash);
        int option = -1;
        while (option != 0) {

            System.out.println(firstMenu);
            option = sc.nextInt();
            sc.nextLine();
            switch (option) {
                case 1:
                    System.out.println("Deseja o nome do livro que deseja pesquisar: ");
                    String url = initial + sc.nextLine().replace(" ", "+");
                    GetBook getter = new GetBook(bookRepository, personRepository);
                    getter.getDataBook(url);
                    break;
                case 2:
                    listarLivros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivosAno();
                    break;
                case 5:
                    listarLivrosIdioma();
                    break;
                case 6:
                    listarTop10Livros();
                    break;
                case 7:
                    buscarAutor();
                    break;
                case 8:
                    listarPercentualIdioma();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }



    private void listarLivros() {
        books = bookRepository.findAll();
        books.stream().forEach(System.out::println);
    }

    private void listarAutores() {
        authors = personRepository.findAll();
        authors.stream().forEach(System.out::println);
    }

    private void listarAutoresVivosAno() {
        authors = personRepository.findAll();
        System.out.println("Digite o ano para verificar os autores vivos:");
        int year = sc.nextInt();
        sc.nextLine();
        personRepository.findLivingAuthorsInYear(year).stream().forEach(System.out::println);
    }

    private void listarLivrosIdioma() {
        System.out.println("Digite o idioma desejado:");
        String idioma = sc.nextLine().toLowerCase();
        Languages lang = Languages.fromIdioma(idioma);

        List<Book> allBooks = bookRepository.findAll();
//        for (Book book : allBooks) {
//            System.out.println("Book: " + book.getTitle() + ", Languages: " + book.getLanguages());
//        }
        List<Book> livrosIdioma = bookRepository.findBooksByLanguage(lang);
        System.out.println("Livros disponíveis no idioma " + idioma);
        livrosIdioma.forEach(System.out::println);
    }

    private void listarTop10Livros() {
        GetBook getter = new GetBook(bookRepository, personRepository);
        getter.getTop(initial);
    }

    private void buscarAutor() {
        System.out.println("Digite APENAS o nome OU sobrenome do autor que deseja buscar informações: ");
        String autor = sc.nextLine().replace(" ", "+"); // Simply obtain the user input
        GetBook getter = new GetBook(bookRepository, personRepository);
        getter.getAuthor(autor); // Pass the user input directly to the getAuthor method
    }

    private void listarPercentualIdioma() {
        books = bookRepository.findAll();
        Map<String, DoubleSummaryStatistics> percentualPorIdioma = books.stream()
                .collect(Collectors.groupingBy(Book::getLanguages, Collectors.summarizingDouble(book -> 1)));

        long totalDeLivros = books.size();
        System.out.println("Percentual de livros no banco de dados em determinados idiomas: ");
        percentualPorIdioma.forEach((language, stats) -> {
            double porcentagem = ((double) stats.getCount() / totalDeLivros) * 100;
            System.out.println(Languages.getIdiomaFromCode(language) + ": " + porcentagem + "%\n");
        });
    }
}





