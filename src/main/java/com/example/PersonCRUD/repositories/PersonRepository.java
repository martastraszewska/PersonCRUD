package com.example.PersonCRUD.repositories;

import com.example.PersonCRUD.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<PersonEntity, String> {


    public List<PersonEntity> findPersonByFirstName (String firstName);





}
