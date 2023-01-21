package br.com.wesquel.restwithspringbootandjavaerudion.mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.wesquel.restwithspringbootandjavaerudion.data.vo.v1.PersonVO;
import br.com.wesquel.restwithspringbootandjavaerudion.data.vo.v2.PersonVOV2;
import br.com.wesquel.restwithspringbootandjavaerudion.model.Person;

@Service
public class PersonMapper {
    public PersonVOV2 convertEntityToVo(Person person) {
        PersonVOV2 vo = new PersonVOV2();
        vo.setId(person.getId());
        vo.setFirstName(person.getFirstName());
        vo.setLastName(person.getLastName());
        vo.setAddress(person.getAddress());
        vo.setBirthDay(new Date());
        vo.setGender(person.getGender());
        return vo;
    }

    public Person convertVoToEntity(PersonVOV2 personVOV2) {
        Person person = new Person();
        person.setId(personVOV2.getId());
        person.setFirstName(personVOV2.getFirstName());
        person.setLastName(personVOV2.getLastName());
        person.setAddress(personVOV2.getAddress());
        //person.setBirthDay(new Date());
        person.setGender(personVOV2.getGender());
        return person;
    }
}
