package br.com.wesquel.restwithspringbootandjavaerudion.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.wesquel.restwithspringbootandjavaerudion.data.vo.v1.PersonVO;
import br.com.wesquel.restwithspringbootandjavaerudion.services.PersonServices;
import br.com.wesquel.restwithspringbootandjavaerudion.util.MediaType;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonServices personServices;

    @GetMapping(
        produces = {
            MediaType.APPLICATION_JSON, 
            MediaType.APPLICATION_XML, 
            MediaType.APPLICATION_YML
        }
    )
    public List<PersonVO> findAll() {
        return personServices.findAll();
    }

    @GetMapping(
        value = "/{id}", 
        produces = {
            MediaType.APPLICATION_JSON, 
            MediaType.APPLICATION_XML, 
            MediaType.APPLICATION_YML
        }
    )
    public PersonVO findById(@PathVariable(value = "id") Long id) {
        return personServices.findById(id);
    }

    @PostMapping(
        produces = {
            MediaType.APPLICATION_JSON, 
            MediaType.APPLICATION_XML, 
            MediaType.APPLICATION_YML
        }, 
        consumes = {
            MediaType.APPLICATION_JSON, 
            MediaType.APPLICATION_XML, 
            MediaType.APPLICATION_YML
        }
    )
    public PersonVO create(@RequestBody PersonVO person) throws Exception{
        return personServices.create(person);
    }

    @PutMapping(
        produces = {
            MediaType.APPLICATION_JSON, 
            MediaType.APPLICATION_XML, 
            MediaType.APPLICATION_YML
        }, 
        consumes = {
            MediaType.APPLICATION_JSON, 
            MediaType.APPLICATION_XML, 
            MediaType.APPLICATION_YML
        }
    )
    public PersonVO update(@RequestBody PersonVO person) throws Exception{
        return personServices.update(person);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        personServices.delete(id);
        return ResponseEntity.noContent().build();
    }
}
