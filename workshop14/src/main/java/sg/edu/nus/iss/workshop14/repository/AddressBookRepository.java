package sg.edu.nus.iss.workshop14.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.workshop14.model.Contact;

@Repository
public class AddressBookRepository {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    private static final String CONTACT_LIST = "contactList";

    public Contact save(final Contact contact){
        redisTemplate.opsForList().leftPush(CONTACT_LIST, contact.getId());
        redisTemplate.opsForHash().put(CONTACT_LIST + "_Map", contact.getId(),contact);
        return findById(contact.getId());
    }

    public Contact findById(final String contactId){
        Contact contact = (Contact) redisTemplate.opsForHash().get(CONTACT_LIST + "_Map",contactId);
        return contact;
    }

    public List<Contact> findAll(int startIndex){
        List<Object> fromContactList = redisTemplate.opsForList().range(CONTACT_LIST, startIndex, 10);

        List<Contact> contactList = redisTemplate.opsForHash()
            .multiGet(CONTACT_LIST + "_Map", fromContactList)
            .stream()
            .filter(Contact.class::isInstance)
            .map(Contact.class::cast)
            .toList();
        return contactList;
    }
}
