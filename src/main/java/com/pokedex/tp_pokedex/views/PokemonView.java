package com.pokedex.tp_pokedex.views;

import com.pokedex.tp_pokedex.models.Pokemon;

/**
 * View class to display a Pokemon in a user-friendly way (text).
 */
public class PokemonView {

    /**
     * Displays the given Pokemon's data on standard output.
     *
     * @param pokemon the Pokemon to display
     */
    public void displayPokemon(Pokemon pokemon) {
        System.out.println("=============================");
        System.out.println("Pokémon # " + pokemon.getId());
        System.out.println("Name : " + pokemon.getName());
        System.out.println("Description : " + pokemon.getDescription());
        System.out.println("Height : " + pokemon.getHeight());
        System.out.println("Weight : " + pokemon.getWeight());
        System.out.println("=============================");
    }
}
