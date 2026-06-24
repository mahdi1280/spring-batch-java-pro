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
            try {
                if(person.getName().equals("Tara")) {
                    throw new RuntimeException("error");
                }
                personRepository.save(person);
            } catch (Exception ex) {
                break;
            }
        }
    }

    public void changeStatus(Person person) {
        person.setActive(true);
    }
}
