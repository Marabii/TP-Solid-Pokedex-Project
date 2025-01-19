package com.pokedex.tp_pokedex;

import com.pokedex.tp_pokedex.utilities.AbstractPokedexRunner;
import com.pokedex.tp_pokedex.utilities.AbstractPokedexRunner.DataSource;
import com.pokedex.tp_pokedex.services.AbstractPokemonProviderService;
import com.pokedex.tp_pokedex.services.LocalizedPropertyProviderInterface;
import com.pokedex.tp_pokedex.services.PokemonHttpProvider;
import com.pokedex.tp_pokedex.services.PokemonSqliteProvider;
import com.pokedex.tp_pokedex.controllers.PokemonController;
import com.pokedex.tp_pokedex.models.Pokemon;
import com.pokedex.tp_pokedex.views.PokemonView;

public class PokedexRunner extends AbstractPokedexRunner  {

    private AbstractPokemonProviderService providerService;
    private PokemonController controller;
    private final PokemonView view;

    public PokedexRunner() {
        super();
        this.view = new PokemonView();
    }

    /**
     * Called by AbstractPokedexRunner whenever user changes data source or locale options.
     * We create a new provider service according to the user's choice, then recreate the controller.
     */
    @Override
    public void onOptionsChange(DataSource dataSource, String dbPath) throws Exception {
        // Decide which provider to use
        switch (dataSource) {
            case POKEAPI:
                // HTTP-based provider, implements localized interface
                this.providerService = new PokemonHttpProvider();
                // set up the locale if it supports localization
                if (this.providerService instanceof LocalizedPropertyProviderInterface) {
                    // use the runner's helper method
                    setupServiceLocale((LocalizedPropertyProviderInterface) this.providerService);
                }
                break;
            case LOCAL_DATABASE:
                // local DB provider (not localized)
                this.providerService = new PokemonSqliteProvider(dbPath);
                break;
        }

        // Recreate the controller with this new provider
        this.controller = new PokemonController(this.providerService);
    }

    /**
     * Called by AbstractPokedexRunner when the user requests a given Pokemon ID.
     */
    @Override
    public void runPokedex(Integer pokemonId) throws Exception {
        Pokemon p = this.controller.getPokemonById(pokemonId);
        this.view.displayPokemon(p);
    }
}
