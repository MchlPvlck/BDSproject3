package org.but.feec.cinema.services;


import org.but.feec.cinema.api.person.PersonBasicView;
import org.but.feec.cinema.data.PersonRepository;

import java.util.List;

public class PersonService {
    private PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonBasicView> getPersonsBasicView() {
        return personRepository.getPersonsBasicView();
    }



}
