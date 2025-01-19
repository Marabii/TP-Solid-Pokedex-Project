package com.pokedex.tp_pokedex.utilities;

import org.sqlite.SQLiteConfig;

import com.pokedex.tp_pokedex.services.LocalizedPropertyProviderInterface;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Set;

public abstract class AbstractPokedexRunner {

    public enum DataSource {
        POKEAPI, LOCAL_DATABASE
    }

    public Set<String> availableLocales = Set.of("en", "fr");

    private DataSource dataSource;
    private String locale;
    private String databasePath = "";
    public static Scanner scanner = new Scanner(System.in);

    /**
     * Entry point to start the interactive CLI.
     */
    public void start() throws Exception {
        this.prompt();
    }

    /**
     * Core loop for prompting the user for a Pokemon ID or changing options.
     */
    private void prompt() throws Exception {
        this.promptOptions();

        while (true) {
            System.out.println(this.displayCurrentOptions());
            System.out.println("Enter the ID of the pokemon to display (enter 'o' to change the options, 'q' to exit)");

            String userInput = scanner.nextLine().trim();

            if (userInput.equals("o")) {
                this.promptOptions();
            } else if (userInput.equals("q")) {
                break;
            } else {
                try {
                    Integer pokemonId = Integer.parseInt(userInput);
                    this.runPokedex(pokemonId);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please try again.");
                }
            }
        }
    }

    /**
     * Prompt user to choose data source, local DB path (if needed), and locale.
     */
    private void promptOptions() throws Exception {
        System.out.println("\n\n==== Setup Pokedex Options ====");

        // Prompt for preferred data source
        while (true) {
            System.out.println("Choose data source : ");
            System.out.println("[1] Pokeapi (HTTP)");
            System.out.println("[2] Local SQLite database");
            String userInput = scanner.nextLine().trim();

            if (userInput.equals("1")) {
                this.dataSource = DataSource.POKEAPI;
                break;
            } else if (userInput.equals("2")) {
                this.dataSource = DataSource.LOCAL_DATABASE;
                break;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }

        // If database selected, prompt for database path
        if (this.dataSource == DataSource.LOCAL_DATABASE) {
            while (true) {
                if (this.databasePath.isEmpty()) {
                    System.out.println("Enter database path:");
                } else {
                    System.out.printf("Enter database path (current path: '%s'): \n", this.databasePath);
                }

                String dbPath = scanner.nextLine().trim();

                if (dbPath.isEmpty() && !this.databasePath.isEmpty()) {
                    dbPath = this.databasePath;
                }

                String jdbcUrl = "jdbc:sqlite:" + dbPath;
                SQLiteConfig config = new SQLiteConfig();
                config.setEncoding(SQLiteConfig.Encoding.UTF8);
                try {
                    DriverManager.getConnection(jdbcUrl, config.toProperties())
                            .prepareStatement("SELECT id, name, description, height, weight FROM pokemons WHERE id = 1")
                            .executeQuery();
                } catch (SQLException e) {
                    System.out.println("Invalid database path. Please try again.");
                    continue;
                }
                this.databasePath = dbPath;
                break;
            }
        }

        // Prompt for locale
        while (true) {
            System.out.println("Choose locale : 'en' (default), or 'fr':");
            String userInput = scanner.nextLine().trim();

            if (this.availableLocales.contains(userInput)) {
                this.locale = userInput;
                break;
            } else if (userInput.isEmpty()) {
                System.out.println("Using 'en' as default");
                this.locale = "en";
                break;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }

        System.out.println("==== End of options setup ====\n");
        this.onOptionsChange(this.dataSource, this.databasePath);
    }

    /**
     * Returns a text with the current data source and locale.
     */
    private String displayCurrentOptions() {
        String dataSourceStr;
        if (this.dataSource == DataSource.POKEAPI) {
            dataSourceStr = "PokéAPI";
        } else {
            dataSourceStr = String.format("Local database (%s)", this.databasePath);
        }
        return String.format("(Current options, data source: %s, locale: %s)", dataSourceStr, this.locale);
    }

    /**
     * Must be implemented by subclasses. It is called whenever the user
     * enters a Pokemon ID in the CLI loop.
     */
    public abstract void runPokedex(Integer pokemonId) throws Exception;

    /**
     * Called after user changes the dataSource, database path, or locale.
     * Override it in your subclass to re-inject or re-configure services.
     */
    public void onOptionsChange(DataSource dataSource, String dbPath) throws Exception {
    }

    /**
     * Helper method to set the locale in a LocalizedPropertyProviderInterface.
     */
    public void setupServiceLocale(LocalizedPropertyProviderInterface service) {
        service.setStringPropertyLocale(this.locale);
    }

}
