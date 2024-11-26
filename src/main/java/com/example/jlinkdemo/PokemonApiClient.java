package com.example.jlinkdemo;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PokemonApiClient {

    private final RestClient restClient;

    public PokemonApiClient(RestClient.Builder restClientBuilder) {
        restClient = restClientBuilder.baseUrl("https://pokeapi.co/api/v2").build();
    }

    public Pokemon getPokemon(String name) {
        var pokemonJson = restClient
                .get()
                .uri("/pokemon/{name}", name)
                .retrieve()
                .body(Pokemon.class);
        return pokemonJson;
    }

}