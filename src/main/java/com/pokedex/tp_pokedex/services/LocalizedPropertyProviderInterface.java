package com.pokedex.tp_pokedex.services;

/**
 * Interface for services that can be localized (i.e., support multiple locales).
 * Extends the basic property provider interface.
 */
public interface LocalizedPropertyProviderInterface extends PropertyProviderInterface {

    /**
     * Sets the locale to use for string properties provided by the service.
     *
     * @param localeCode the locale code (e.g., "en", "fr")
     */
    void setStringPropertyLocale(String localeCode);

    /**
     * Gets the locale that is currently used by the service.
     *
     * @return the current locale code
     */
    String getStringPropertyLocale();
}
