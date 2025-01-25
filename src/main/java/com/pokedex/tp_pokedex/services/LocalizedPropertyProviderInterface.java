package com.pokedex.tp_pokedex.services;


public interface LocalizedPropertyProviderInterface extends PropertyProviderInterface {
    void setStringPropertyLocale(String localeCode);
    String getStringPropertyLocale();
}
