package com.iaco.literalura;

import com.iaco.literalura.menu.Menu;
import com.iaco.literalura.repository.BookRepository;
import com.iaco.literalura.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {
	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private PersonRepository personRepository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Menu menu = new Menu(bookRepository, personRepository);
		menu.exibirMenu();
	}
}
