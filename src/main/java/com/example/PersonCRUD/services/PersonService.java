package com.example.PersonCRUD.services;

import com.example.PersonCRUD.dto.PersonDto;
import com.example.PersonCRUD.dto.PersonsDtoList;
import com.example.PersonCRUD.entities.PersonEntity;
import com.example.PersonCRUD.exceptions.PersonNotFoundException;
import com.example.PersonCRUD.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public void addPerson(PersonDto personDto) {
        personRepository.save(PersonEntity.fromPersonDto(personDto));
    }

    public void deletePerson(String id) {
        boolean existsById = personRepository.existsById(id);
        if (existsById) {
            personRepository.deleteById(id);
        } else {
            throw new PersonNotFoundException("Person not found" + id);
        }
    }

    public void updatePerson(PersonDto dto) {
        boolean existsById = personRepository.existsById((dto.getId()));
        if (existsById) {
            PersonEntity personEntity = PersonEntity.fromPersonDto(dto);
            personRepository.save(personEntity);
        } else {
            throw new PersonNotFoundException("Person not found " + dto.getId());
        }
    }

    public PersonsDtoList getPersonByFirstName(String firstName) {
        List<PersonEntity> persons = personRepository.findPersonByFirstName(firstName);
        List<PersonDto> collect = persons.stream().map(PersonEntity::toPersonDto).collect(Collectors.toList());
        return new PersonsDtoList(collect);
    }

    public PersonDto getPersonById(String id) {
        return personRepository.findById(id)
                .map(PersonEntity::toPersonDto)
                .orElseThrow(() -> new PersonNotFoundException("Person not found " + id));
    }
}

