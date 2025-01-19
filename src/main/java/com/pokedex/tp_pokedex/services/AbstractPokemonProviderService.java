package com.pokedex.tp_pokedex.services;

/**
 * Abstract class representing a Pokemon data provider service.
 * It implements the base PropertyProviderInterface, so that the
 * controller can read integer/string properties from it.
 *
 * The data retrieval logic (HTTP or DB) is implemented in concrete subclasses.
 */
public abstract class AbstractPokemonProviderService implements PropertyProviderInterface {

    /**
     * Loads the relevant Pokemon data into some internal storage (e.g. a map),
     * so that subsequent getIntProperty(...) and getStringProperty(...) calls
     * will return the correct values.
     *
     * @param pokemonId the ID of the Pokemon to fetch from the data source
     * @throws Exception if any data retrieval or parsing error occurs
     */
    public abstract void loadPokemonData(int pokemonId) throws Exception;
}
