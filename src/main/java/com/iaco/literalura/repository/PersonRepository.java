package com.iaco.literalura.repository;

import com.iaco.literalura.model.Book;
import com.iaco.literalura.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByName(String name);

    @Query("SELECT p FROM Person p WHERE p.birthYear IS NOT NULL AND (p.deathYear IS NULL OR :year <= p.deathYear) AND :year >= p.birthYear")
    List<Person> findLivingAuthorsInYear(@Param("year") int year);

    @Query("SELECT p FROM Person p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Person> findByNameContainingIgnoreCase(@Param("name") String name);

}
