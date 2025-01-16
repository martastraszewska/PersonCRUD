package com.example.PersonCRUD.dto;

import java.util.List;

public class PersonsDtoList {

   private List<PersonDto> persons;

   public List<PersonDto> getPersons() {
      return persons;
   }

   public void setPersons(List<PersonDto> persons) {
      this.persons = persons;
   }

   public PersonsDtoList() {
   }

   public PersonsDtoList(List<PersonDto> persons) {
      this.persons = persons;
   }
}
