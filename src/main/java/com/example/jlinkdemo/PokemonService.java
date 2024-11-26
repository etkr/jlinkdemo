package com.example.jlinkdemo;

import org.springframework.stereotype.Service;

@Service
public class PokemonService {

    private PokemonRepository pokemonRepository;
    private PokemonApiClient pokemonApiClient;

    public PokemonService(PokemonApiClient pokemonApiClient, PokemonRepository pokemonRepository) {
        this.pokemonApiClient = pokemonApiClient;
        this.pokemonRepository = pokemonRepository;
    }

    public Pokemon getPokemon(String name) {
        var pokemon = pokemonRepository.findByName(name);
        if (pokemon == null) {
            pokemon = pokemonApiClient.getPokemon(name);
            pokemonRepository.save(pokemon);
        }
        return pokemon;
    }
}
