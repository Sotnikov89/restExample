package ru.job4j.demo_rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.demo_rest.domain.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
}
