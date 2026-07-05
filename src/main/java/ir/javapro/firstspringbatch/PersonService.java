package ir.javapro.firstspringbatch;

import jakarta.transaction.Transactional;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void save(Chunk<Person> personList) {
        for (Person person : personList) {
            personRepository.save(person);
        }
    }

    public void changeStatus(Person person) {
        person.setActive(true);
    }
}
