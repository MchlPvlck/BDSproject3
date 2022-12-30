package org.but.feec.cinema.services;


import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.but.feec.cinema.api.person.PersonAuthView;
import org.but.feec.cinema.data.PersonRepository;
import org.but.feec.cinema.exceptions.ResourceNotFoundException;


public class AuthService {

    private PersonRepository personRepository;

    public AuthService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private PersonAuthView findPersonByUsername(String username) {
        return personRepository.findPersonByUsername(username);
    }

    public boolean authenticate(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        PersonAuthView personAuthView = findPersonByUsername(username);
        if (personAuthView == null) {
            throw new ResourceNotFoundException("Provided username is not found.");
        }
        return argon2.verify(personAuthView.getPassword(),password.toCharArray());
    }

    final private static Argon2 argon2 = Argon2Factory.create(
            Argon2Factory.Argon2Types.ARGON2id,16,64
    );

}
