package com.pokedex.tp_pokedex;

// import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the Spring Boot application.
 * Also instantiates and starts the PokedexRunner for CLI usage.
 */
@SpringBootApplication
public class TpPokedexApplication {

    public static void main(String[] args) {
        // Start the CLI runner
        PokedexRunner runner = new PokedexRunner();
        try {
            runner.start(); // interactive CLI
        } catch (Exception e) {
            System.err.println("Error while running the Pokedex CLI: " + e.getMessage());
        }

        // SpringApplication.run(TpPokedexApplication.class, args);
    }

}
