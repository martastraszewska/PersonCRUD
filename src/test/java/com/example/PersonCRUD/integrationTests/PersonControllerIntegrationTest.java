package com.example.PersonCRUD.integrationTests;

import com.example.PersonCRUD.dto.PersonDto;
import com.example.PersonCRUD.dto.PersonsDtoList;
import com.example.PersonCRUD.entities.PersonEntity;
import com.example.PersonCRUD.repositories.PersonRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class PersonControllerIntegrationTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void shouldReturnPersonWhenExistsByIdTest() {
        // given - current system state
        PersonEntity personEntity = new PersonEntity();
        personEntity.setId("123");
        personEntity.setFirstName("MARTA");
        personEntity.setLastName("STRASZEWSKA");
        personRepository.save(personEntity);
        //when
        ResponseEntity<PersonDto> result = testRestTemplate.getForEntity("/persons/123", PersonDto.class);
        //then
        assertEquals(200, result.getStatusCode().value());
        assertEquals("123", result.getBody().getId());
        assertEquals("MARTA", result.getBody().getFirstName());
        assertEquals("STRASZEWSKA", result.getBody().getLastName());
    }
    @Test
    public void shouldReturn404WhenPersonNotExistsByIdTest(){
        //when
        ResponseEntity<PersonDto> result = testRestTemplate.getForEntity("/persons/dummy-id", PersonDto.class);
       Assert.assertEquals(404, result.getStatusCode().value());
    }

    @Test
    public void shouldAddPersonWhenNotExistsTest(){
        //when
        PersonEntity p = new PersonEntity();
        p.setId("1");
        p.setFirstName("MARTA");
        p.setLastName("STRASZEWSKA");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<PersonEntity> request = new HttpEntity<>(p, headers);
        ResponseEntity<String> result = this.testRestTemplate.postForEntity("/persons", request, String.class);
        //then
        Assert.assertEquals(201, result.getStatusCode().value());
    }

    @Test
    public void shouldDeleteThePersonWhenExistsTest() {
        // given
        PersonEntity personEntity = new PersonEntity();
        personEntity.setId("dummy-id");
        personRepository.save(personEntity);
        //when
        ResponseEntity<Void> result = testRestTemplate.exchange("/persons/dummy-id", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        //then
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        //and
        assertEquals(0, personRepository.count());
    }
    @Test
    public void shouldReturn404OnDeleteWhenPersonNotExistsTest() {
        //when
        ResponseEntity<Void> result = testRestTemplate.exchange("/persons/dummy-id", HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        //then
        Assert.assertEquals(404, result.getStatusCode().value());
    }



    @Test
    public void shouldUpdatePersonWhenExistsTest(){
        // given
        PersonEntity personEntity = new PersonEntity();
        personEntity.setId("dummy-id");
        personEntity.setFirstName("testFirstName");
        personEntity.setLastName("testLastName");
        personRepository.save(personEntity);
        //when
        HttpHeaders headers = new HttpHeaders();
        PersonDto personDto = new PersonDto();
        personDto.setFirstName("testDummyName");
        personDto.setLastName("testDummyName");
        HttpEntity<PersonDto> request = new HttpEntity<>(personDto, headers);
        ResponseEntity<PersonDto> result = this.testRestTemplate.exchange("/persons/dummy-id",HttpMethod.PUT, request, PersonDto.class);
        //then
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        //and
        List<PersonEntity> allPersons = personRepository.findAll();
        assertEquals(1, allPersons.size());
        PersonEntity firstPerson = allPersons.get(0);
        assertEquals("dummy-id", firstPerson.getId());
        assertEquals("testDummyName", firstPerson.getFirstName());
        assertEquals("testDummyName", firstPerson.getLastName());
    }
    @Test
    public void shouldReturn404OnUpdateWhenPersonNotExistsTest(){
        //when
        HttpHeaders headers = new HttpHeaders();
        PersonDto personDto = new PersonDto();
        personDto.setFirstName("testDummyName");
        personDto.setLastName("testDummyName");
        HttpEntity<PersonDto> request = new HttpEntity<>(personDto, headers);
        ResponseEntity<PersonDto> result = this.testRestTemplate.exchange("/persons/dummy-id", HttpMethod.PUT, request, PersonDto.class);
        //then
        Assert.assertEquals(404, result.getStatusCode().value());
    }
    @Test
    public void shouldReturnListOfPersonsWhenPersonFoundByFirstNameTest(){
        // given
        PersonEntity personEntity1 = new PersonEntity();
        personEntity1.setId("1");
        personEntity1.setFirstName("name");
        personEntity1.setLastName("dummy-name1");
        personRepository.save(personEntity1);
        PersonEntity personEntity2 = new PersonEntity();
        personEntity2.setId("2");
        personEntity2.setFirstName("name");
        personEntity2.setLastName("dummy-name2");
        personRepository.save(personEntity2);
        //when
        ResponseEntity<PersonsDtoList> result = testRestTemplate.getForEntity("/persons?firstName=name", PersonsDtoList.class);
        //then
        assertEquals(200, result.getStatusCode().value());
        assertEquals(2, result.getBody().getPersons().size());
    }
    @Test
    public void shouldReturnEmptyListOfPersonsWhenPersonNotFoundByFirstNameTest(){
        //when
        ResponseEntity<PersonsDtoList> result = testRestTemplate.getForEntity("/persons?firstName=name", PersonsDtoList.class);
        //then
        assertEquals(200, result.getStatusCode().value());
        assertEquals(0, result.getBody().getPersons().size());
    }
}

