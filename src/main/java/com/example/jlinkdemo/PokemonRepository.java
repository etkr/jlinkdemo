package com.example.jlinkdemo;

import org.springframework.data.repository.CrudRepository;

public interface PokemonRepository extends CrudRepository<Pokemon, Integer> {

    Pokemon findByName(String name);

}

  
