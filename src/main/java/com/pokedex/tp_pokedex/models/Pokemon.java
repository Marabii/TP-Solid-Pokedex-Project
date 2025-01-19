package com.pokedex.tp_pokedex.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple model class to hold the essential Pokemon data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pokemon {
    private int id;
    private String name;
    private String description;
    private int height;
    private int weight;
}
