package sg.edu.nus.iss.workshop14.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import sg.edu.nus.iss.workshop14.model.Contact;
import sg.edu.nus.iss.workshop14.service.AddressBookService;

@Controller
@RequestMapping(path="/contact")
public class AddressBookController {

    @Autowired
    private AddressBookService adrbkSvc;
    
    @GetMapping
    public String showAddressBookForm(Model model){
        model.addAttribute("contact", new Contact());
        return "addressbook";
    }

    @PostMapping
    public String saveContact(@Valid Contact contact, BindingResult binding , Model model){
        if(binding.hasErrors()){
            return "addressbook";
        }
        adrbkSvc.save(contact);
        return "showContact";
    }

    // http://localhost:8080/contact/26f41279
    @GetMapping(path="{contactId}")
    public String getContactId(Model model, @PathVariable String contactId){
        Contact ctc = adrbkSvc.findById(contactId);
        model.addAttribute("contact", ctc);
        return "showContact";
    }

    // using query string
    //http://localhost:8080/contact/list?startIndex=0
    @GetMapping(path="/list")
    public String getAllContacts(Model model, @RequestParam(defaultValue = "0") String startIndex){
        Integer startIndexInt = Integer.parseInt(startIndex);
        List<Contact> ctcs = adrbkSvc.findAll(startIndexInt);
        model.addAttribute("contacts", ctcs);
        return "contacts";
    }

    // using path variable
    // http://localhost:8080/contact/list/0
    @GetMapping(path="/list/{startIndex}")
    public String getAllContactsByPath(Model model, @PathVariable Integer startIndex){
        List<Contact> ctcs = adrbkSvc.findAll(startIndex);
        model.addAttribute("contacts", ctcs);
        return "contacts";
    }
}