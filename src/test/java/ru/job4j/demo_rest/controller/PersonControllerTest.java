package ru.job4j.demo_rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.job4j.demo_rest.DemoRestApplication;
import ru.job4j.demo_rest.domain.Person;
import ru.job4j.demo_rest.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = DemoRestApplication.class)
@AutoConfigureMockMvc
class PersonControllerTest {

    @MockBean
    private PersonRepository personRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAll() throws Exception {
        when(personRepository.findAll()).thenReturn(List.of(new Person(1,"Asd","Dsa")));

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/person/"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].login", Matchers.is("Asd")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].password", Matchers.is("Dsa")));

        verify(personRepository).findAll();
    }

    @Test
    void findById() throws Exception {
        when(personRepository.findById(anyInt())).thenReturn(Optional.of(new Person(1, "Asd", "Dsa")));

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/person/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login", Matchers.is("Asd")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", Matchers.is("Dsa")));
        verify(personRepository).findById(anyInt());
    }

    @Test
    void create() throws Exception {
        Person person = new Person("Asd", "Dsa");
        Person savedPerson = new Person(1,"Asd", "Dsa");
        when(personRepository.save(any())).thenReturn(savedPerson);

        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(savedPerson)));

        verify(personRepository).save(any(Person.class));
    }

    @Test
    void update() throws Exception {
        Person person = new Person(1,"Asd", "Dsa");
        when(personRepository.save(any())).thenReturn(person);

        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        verify(personRepository).save(any(Person.class));
    }

    @Test
    void delete() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/person/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        verify(personRepository).delete(any(Person.class));
    }
}