package com.pokedex.tp_pokedex.controllers;

import com.pokedex.tp_pokedex.models.Pokemon;
import com.pokedex.tp_pokedex.services.AbstractPokemonProviderService;

/**
 * Controller that orchestrates data retrieval and transforms it
 * into our Pokemon model.
 */
public class PokemonController {

    private final AbstractPokemonProviderService service;

    public PokemonController(AbstractPokemonProviderService service) {
        this.service = service;
    }

    /**
     * Retrieves a Pokemon by its ID, returning a populated Pokemon model.
     *
     * @param pokemonId The numeric ID of the Pokemon
     * @return A Pokemon object containing the relevant data
     * @throws Exception if there's an error retrieving or parsing data
     */
    public Pokemon getPokemonById(int pokemonId) throws Exception {
        // Let the service load from whichever data source is configured
        service.loadPokemonData(pokemonId);

        // Then build and return the model
        Pokemon p = new Pokemon();
        p.setId(pokemonId);
        p.setName(service.getStringProperty("name"));
        p.setDescription(service.getStringProperty("description"));

        Integer height = service.getIntProperty("height");
        p.setHeight(height != null ? height : 0);

        Integer weight = service.getIntProperty("weight");
        p.setWeight(weight != null ? weight : 0);

        return p;
    }
}
