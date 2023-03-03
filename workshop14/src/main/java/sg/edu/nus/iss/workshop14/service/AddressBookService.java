package sg.edu.nus.iss.workshop14.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.workshop14.model.Contact;
import sg.edu.nus.iss.workshop14.repository.AddressBookRepository;

@Service
public class AddressBookService {
    @Autowired
    private AddressBookRepository addBookRepo;

    public void save(final Contact contact){
        addBookRepo.save(contact);;
    }

    public Contact findById(final String contactId){
        return addBookRepo.findById(contactId);
    }

    public List<Contact> findAll(int startIndex){
        return addBookRepo.findAll(startIndex);
    }
}
