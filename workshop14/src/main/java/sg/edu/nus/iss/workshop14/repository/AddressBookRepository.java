package sg.edu.nus.iss.workshop14.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.workshop14.model.Contact;

@Repository
public class AddressBookRepository {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private static final String CONTACT_LIST = "contactList";
    private static final String CONTACT_LIST_MAP = "contactList_Map";

    public Contact save(final Contact contact) {
        redisTemplate.opsForList().leftPush(CONTACT_LIST, contact.getId());
        redisTemplate.opsForHash().put(CONTACT_LIST_MAP, contact.getId(), contact);
        return findById(contact.getId());
    }

    public Contact findById(final String contactId) {
        Contact contact = (Contact) redisTemplate.opsForHash().get(CONTACT_LIST_MAP, contactId);
        return contact;
    }

    public List<Contact> findAll(int startIndex) {
        // Retrieve List of Object from redisTemplate.opsForList()
        List<Object> fromContactList = redisTemplate.opsForList().range(CONTACT_LIST, startIndex, -1);

        // method 1 to get all Values from redisTempalte.opsForHash()
        List<Contact> contactList = redisTemplate.opsForHash()
                .multiGet(CONTACT_LIST_MAP, fromContactList)
                .stream()
                .filter(Contact.class::isInstance)
                .map(Contact.class::cast)
                .toList();

        // method 2 to get all Values from redisTempalte.opsForHash()
        List<Contact> newContactList = new ArrayList<>();
        if (fromContactList != null && fromContactList.size() > 0) {
            for (Object contactId : fromContactList) {
                Contact newContact = (Contact) redisTemplate.opsForHash().get(CONTACT_LIST_MAP, contactId);
                if (newContact instanceof Contact) {
                    newContactList.add(newContact);
                }
            }
        }
        // System.out.println(contactList);
        // System.out.println(newContactList);

        // method 3 to get all Values from redisTempalte.opsForHash()
        List<Object> hashMapList = redisTemplate.opsForHash().values(CONTACT_LIST_MAP);
        List<Contact> newNewContactList = new ArrayList<>();
        for (Object contact : hashMapList) {
            Contact newContact = (Contact) contact;
            newNewContactList.add(newContact);
        }

        return newNewContactList;
    }
}
