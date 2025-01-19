package com.pokedex.tp_pokedex.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Retrieves Pokemon data from the public PokéAPI using HTTP.
 * Uses WebClient from Spring to make the HTTP requests.
 */
public class PokemonHttpProvider extends AbstractPokemonProviderService implements LocalizedPropertyProviderInterface {

    private String currentLocale = "en";
    private final Map<String, Object> properties = new HashMap<>();

    @Override
    public void setStringPropertyLocale(String localeCode) {
        this.currentLocale = localeCode;
    }

    @Override
    public String getStringPropertyLocale() {
        return this.currentLocale;
    }

    private final WebClient webClient = WebClient.builder()
            .exchangeStrategies(ExchangeStrategies.builder()
                    .codecs(configurer -> configurer.defaultCodecs()
                            .maxInMemorySize(16 * 1024 * 1024)) // Set to 16 MB
                    .build())
            .build();

    @Override
    public void loadPokemonData(int pokemonId) throws Exception {
        // clear old properties
        properties.clear();

        // 1) Get height/weight from /pokemon/:id
        String pokemonUrl = "https://pokeapi.co/api/v2/pokemon/" + pokemonId;

        String pokemonResponse = webClient.get()
                .uri(pokemonUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // blocking for simplicity in a CLI environment

        JSONObject pokemonJson = (JSONObject) new JSONParser().parse(pokemonResponse);
        Long height = (Long) pokemonJson.get("height");
        Long weight = (Long) pokemonJson.get("weight");

        properties.put("height", height.intValue());
        properties.put("weight", weight.intValue());

        // 2) Get name/description from /pokemon-species/:id
        String speciesUrl = "https://pokeapi.co/api/v2/pokemon-species/" + pokemonId;
        String speciesResponse = webClient.get()
                .uri(speciesUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONObject speciesJson = (JSONObject) new JSONParser().parse(speciesResponse);
        JSONArray names = (JSONArray) speciesJson.get("names");
        JSONArray flavorTexts = (JSONArray) speciesJson.get("flavor_text_entries");

        // find correct name in the specified locale
        String chosenName = "";
        if (names != null) {
            for (Object o : names) {
                JSONObject nameObj = (JSONObject) o;
                JSONObject langObj = (JSONObject) nameObj.get("language");
                String lang = (String) langObj.get("name");
                if (lang.equals(this.currentLocale)) {
                    chosenName = (String) nameObj.get("name");
                    break;
                }
            }
        }
        // fallback if not found
        if (chosenName.isEmpty() && names != null && !names.isEmpty()) {
            // just pick the first
            JSONObject firstNameObj = (JSONObject) names.get(0);
            chosenName = (String) firstNameObj.get("name");
        }

        // find correct flavor text in the specified locale
        String chosenDescription = "";
        if (flavorTexts != null) {
            for (Object o : flavorTexts) {
                JSONObject ftObj = (JSONObject) o;
                JSONObject langObj = (JSONObject) ftObj.get("language");
                String lang = (String) langObj.get("name");
                if (lang.equals(this.currentLocale)) {
                    chosenDescription = (String) ftObj.get("flavor_text");
                    // remove line breaks or weird characters
                    chosenDescription = chosenDescription
                            .replace("\n", " ")
                            .replace("\f", " ");
                    break;
                }
            }
        }
        // fallback if not found
        if (chosenDescription.isEmpty() && flavorTexts != null && !flavorTexts.isEmpty()) {
            JSONObject firstFtObj = (JSONObject) flavorTexts.get(0);
            chosenDescription = (String) firstFtObj.get("flavor_text");
            chosenDescription = chosenDescription
                    .replace("\n", " ")
                    .replace("\f", " ");
        }

        properties.put("name", chosenName);
        properties.put("description", chosenDescription);
    }

    @Override
    public Integer getIntProperty(String propertyName) {
        Object val = properties.get(propertyName);
        if (val instanceof Integer) {
            return (Integer) val;
        }
        return null;
    }

    @Override
    public String getStringProperty(String propertyName) {
        Object val = properties.get(propertyName);
        if (val instanceof String) {
            return (String) val;
        }
        return null;
    }
}
