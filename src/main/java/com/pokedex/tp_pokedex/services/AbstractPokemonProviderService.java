package com.pokedex.tp_pokedex.services;

public abstract class AbstractPokemonProviderService implements PropertyProviderInterface {
    public abstract void loadPokemonData(int pokemonId) throws Exception;
}
