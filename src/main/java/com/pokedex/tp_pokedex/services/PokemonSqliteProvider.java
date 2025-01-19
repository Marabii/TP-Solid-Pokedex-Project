package com.pokedex.tp_pokedex.services;

import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Retrieves Pokemon data from a local SQLite database file.
 * The language is fixed (whatever is stored in the DB).
 */
public class PokemonSqliteProvider extends AbstractPokemonProviderService {

    private final String dbPath;
    private final Map<String, Object> properties = new HashMap<>();

    public PokemonSqliteProvider(String dbPath) {
        this.dbPath = dbPath;
    }

    @Override
    public void loadPokemonData(int pokemonId) throws Exception {
        properties.clear();

        String jdbcUrl = "jdbc:sqlite:" + dbPath;
        SQLiteConfig config = new SQLiteConfig();
        config.setEncoding(SQLiteConfig.Encoding.UTF8);

        try (Connection connection = DriverManager.getConnection(jdbcUrl, config.toProperties())) {
            String sql = "SELECT id, name, description, height, weight FROM pokemons WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, pokemonId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    properties.put("name", rs.getString("name"));
                    properties.put("description", rs.getString("description"));
                    properties.put("height", rs.getInt("height"));
                    properties.put("weight", rs.getInt("weight"));
                } else {
                    throw new Exception("No record found for pokemon with id " + pokemonId);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error while querying the database: " + e.getMessage(), e);
        }
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

    @Override
    public void setStringPropertyLocale(String localeCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setStringPropertyLocale'");
    }

    @Override
    public String getStringPropertyLocale() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStringPropertyLocale'");
    }
}
