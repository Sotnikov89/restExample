package ru.job4j.demo_rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.demo_rest.domain.Employee;
import ru.job4j.demo_rest.domain.Person;
import ru.job4j.demo_rest.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private static final String API = "http://localhost:8080/person/";
    private static final String API_ID = "http://localhost:8080/person/{id}";

    private final RestTemplate restTemplate;
    private final EmployeeRepository employeeRepository;

    public EmployeeController(RestTemplate restTemplate, EmployeeRepository employeeRepository) {
        this.restTemplate = restTemplate;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<Employee>> findAll() {
        return new ResponseEntity<>(employeeRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/{id}/account/")
    public ResponseEntity<Employee> newAccount(@PathVariable int id, @RequestBody Person person) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            Person newPerson = restTemplate.postForObject(API, person, Person.class);
            employee.get().addAccount(newPerson);
            employee = Optional.of(employeeRepository.save(employee.get()));
        }
        return new ResponseEntity<>(
                employee.orElse(null),
                employee.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PutMapping("/{id}/account/")
    public ResponseEntity<Void> updateAccount(@PathVariable int id, @RequestBody Person person) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            restTemplate.put(API, person, Person.class);
            employee.get().getAccounts().removeIf(p -> p.getId()==person.getId());
            employee.get().addAccount(person);
        }
        return new ResponseEntity<>(employee.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{empId}/account/{accId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable int empId, @PathVariable int accId) {
        Optional<Employee> employee = employeeRepository.findById(empId);
        if (employee.isPresent()) {
            employee.get().getAccounts().removeIf(p -> p.getId()==accId);
            employeeRepository.save(employee.get());
            restTemplate.delete(API_ID, accId);
        }
        return new ResponseEntity<>(
                employee.isPresent()? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }
}
