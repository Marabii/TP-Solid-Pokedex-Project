package com.pokedex.tp_pokedex.services;

public interface PropertyProviderInterface {
    Integer getIntProperty(String propertyName);
    String getStringProperty(String propertyName);
    void setStringPropertyLocale(String localeCode);
    String getStringPropertyLocale();
}
