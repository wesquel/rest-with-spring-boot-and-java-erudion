package br.com.wesquel.restwithspringbootandjavaerudion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.wesquel.restwithspringbootandjavaerudion.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    
}
