package br.com.wesquel.restwithspringbootandjavaerudion.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.wesquel.restwithspringbootandjavaerudion.data.vo.v1.PersonVO;
import br.com.wesquel.restwithspringbootandjavaerudion.exceptions.ResourceNotFoundException;
import br.com.wesquel.restwithspringbootandjavaerudion.mapper.DozerMapper;
import br.com.wesquel.restwithspringbootandjavaerudion.model.Person;
import br.com.wesquel.restwithspringbootandjavaerudion.repository.PersonRepository;

@Service
public class PersonServices {

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository personRepository;

    public List<PersonVO> findAll(){
        logger.info("Finding all people!");
        return DozerMapper.parseListObjects(personRepository.findAll(), PersonVO.class);
    }

    public PersonVO findById(Long id){
        logger.info("Finding one person!");
        var entity = personRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        return DozerMapper.parseObject(entity, PersonVO.class);
    }

    public PersonVO create(PersonVO personVO) {
        logger.info("Creating one person!");
        var entity = DozerMapper.parseObject(personVO, Person.class);    
        var vo = DozerMapper.parseObject(personRepository.save(entity), PersonVO.class);
        return vo;
    }

    public PersonVO update(PersonVO personVo) {
        logger.info("Updating one person!");
        var entity = personRepository.findById(personVo.getId())
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!")); 

        entity.setFirstName(personVo.getFirstName());
        entity.setLastName(personVo.getLastName());
        entity.setAddress(personVo.getAddress());
        entity.setGender(personVo.getGender());
    
        var vo = DozerMapper.parseObject(personRepository.save(entity), PersonVO.class);
        return vo;
    }

    public void delete(Long id)
    {
        var entity = personRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!")); 
        logger.info("Deleting one person!");
        personRepository.delete(entity);
    }

}
