package com.example.PersonCRUD.controllers;

import com.example.PersonCRUD.dto.PersonDto;
import com.example.PersonCRUD.dto.PersonsDtoList;
import com.example.PersonCRUD.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/{id}")
    public PersonDto getPersonById(@PathVariable String id){
        return personService.getPersonById(id);
    }

    @GetMapping
    public PersonsDtoList getPersonByFirstName(@RequestParam String firstName){
        return personService.getPersonByFirstName(firstName);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addPerson(@RequestBody PersonDto p){
        personService.addPerson(p);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable String id) {
        personService.deletePerson(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePerson(@PathVariable String id, @RequestBody PersonDto dto) {
       dto.setId(id);
        personService.updatePerson(dto);
    }

}
