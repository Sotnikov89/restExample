package ru.job4j.demo_rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.demo_rest.domain.Employee;
import ru.job4j.demo_rest.repository.EmployeeRepository;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private RestTemplate restTemplate;

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/")
    public ResponseEntity<List<Employee>> findAll() {
        List<Employee> employees = (List<Employee>) employeeRepository.findAll();
        return new ResponseEntity<>(
                employees,
                employees.size() != 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }
}
