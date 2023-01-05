package br.com.wesquel.restwithspringbootandjavaerudion.controllers;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.print.attribute.standard.Media;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wesquel.restwithspringbootandjavaerudion.model.Person;
import br.com.wesquel.restwithspringbootandjavaerudion.services.PersonServices;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonServices personServices;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> findAll() {
        return personServices.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Person findById(@PathVariable("id") String id) throws Exception{
        return personServices.findById(id);
    }

    @PostMapping(
        produces = MediaType.APPLICATION_JSON_VALUE, 
        consumes = MediaType.APPLICATION_JSON_VALUE
        )
    public Person create(@RequestBody Person person) throws Exception{
        return personServices.create(person);
    }

    @PutMapping(
        produces = MediaType.APPLICATION_JSON_VALUE, 
        consumes = MediaType.APPLICATION_JSON_VALUE
        )
    public Person update(@RequestBody Person person) throws Exception{
        return personServices.update(person);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") String id){
        personServices.delete(id);
    }
}
