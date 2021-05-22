package ru.job4j.demo_rest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.demo_rest.domain.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
}
