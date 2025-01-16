package com.example.PersonCRUD.entities;

import com.example.PersonCRUD.dto.PersonDto;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "persons")
public class PersonEntity {
    @Id
    private String id;

    private String firstName;

    private String lastName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public static PersonEntity fromPersonDto(PersonDto personDto) {
        PersonEntity personEntity = new PersonEntity();
        personEntity.firstName = personDto.getFirstName();
        personEntity.lastName = personDto.getLastName();
        personEntity.id = (personDto.getId() == null) ? UUID.randomUUID().toString() : personDto.getId();
        return personEntity;
    }

    public PersonDto toPersonDto() {
        PersonDto personDto = new PersonDto();
        personDto.setId(this.id);
        personDto.setFirstName(this.firstName);
        personDto.setLastName(this.lastName);
        return personDto;
    }
}
