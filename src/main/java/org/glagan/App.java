package org.glagan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Set;
import java.util.logging.Level;

import org.glagan.Core.Database;
import org.glagan.Core.Game;
import org.glagan.Core.Save;
import org.glagan.Core.Swingy;
import org.glagan.Display.CurrentDisplay;
import org.glagan.Display.Mode;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class App {
    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        Options options = new Options();

        Option displayModeOption = new Option("d", "display", true, "display mode");
        displayModeOption.setRequired(false);
        options.addOption(displayModeOption);

        Option databasePortOption = new Option("p", "port", true, "port of the database");
        databasePortOption.setType(Number.class);
        databasePortOption.setRequired(false);
        options.addOption(databasePortOption);

        Option databaseHostOption = new Option("h", "host", true, "host of the database");
        databaseHostOption.setRequired(false);
        options.addOption(databaseHostOption);

        Option databaseUserOption = new Option("u", "user", true, "user of the database");
        databaseUserOption.setRequired(false);
        options.addOption(databaseUserOption);

        Option databaseNameOption = new Option("n", "name", true, "name of the database");
        databaseNameOption.setRequired(false);
        options.addOption(databaseNameOption);

        Option databasePasswordOption = new Option("q", "password", true, "password of the database");
        databasePasswordOption.setRequired(false);
        options.addOption(databasePasswordOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        Mode displayMode = Mode.CONSOLE;
        String databaseHost = "localhost";
        int databasePort = 5442;
        String databaseName = "swingy";
        String databaseUser = "admin";
        String databasePassword = "admin";
        try {
            cmd = parser.parse(options, args);

            // Display option
            String stringDisplayMode = cmd.getOptionValue("display", "console");
            switch (stringDisplayMode) {
                case "console":
                    displayMode = Mode.CONSOLE;
                    break;
                case "gui":
                    displayMode = Mode.GUI;
                    break;
            }
            CurrentDisplay.setMode(displayMode);

            // Database options
            databaseHost = cmd.getOptionValue("host", "localhost");
            if (cmd.hasOption("port")) {
                databasePort = ((Number) cmd.getParsedOptionValue("port")).intValue();
                if (databasePort < 0 || databasePort > 65535) {
                    System.out.println("\u001B[31mInvalid database port \u001B[0m" + databasePort);
                    System.exit(1);
                }
            }
            databaseName = cmd.getOptionValue("name", "swingy");
            databaseUser = cmd.getOptionValue("user", "admin");
            databasePassword = cmd.getOptionValue("password", "admin");
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("swingy", options);
            System.exit(1);
        }

        // Create the Database and run the blocking queue in a thread
        Database database = new Database();
        database.connect(databaseHost, databasePort, databaseUser, databasePassword, databaseName);
        Swingy.getInstance().setDatabase(database);

        // Cleanup and export saves
        if (database.isConnected()) {
            System.out.println("Exporting saves...");
            String[] files = Save.listSaveFiles();
            for (String path : files) {
                try {
                    // Only export valid saves
                    File file = new File(path);
                    Reader reader = new FileReader(file);
                    Game game = Game.deserialize(reader);
                    game.setSavePath(path);
                    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
                    Set<ConstraintViolation<Game>> constraintViolations = validator.validate(game);
                    if (constraintViolations.size() > 0) {
                        continue;
                    } else if (game.getMap() != null) {
                        if (!game.getMap().validateLocations()) {
                            continue;
                        } else if (game.getHero().getPosition() == null) {
                            continue;
                        } else if (game.getHero().getPosition().getX() >= game.getMap().getSize()
                                || game.getHero().getPosition().getY() >= game.getMap().getSize()) {
                            continue;
                        }
                    }

                    // Export to the database
                    if (!game.save(true)) {
                        System.out.println("\u001B[31mAborting save export...\u001B[0m");
                        break;
                    }

                    // Delete save file -- it's exported to the database
                    // file.delete();
                } catch (FileNotFoundException | JsonSyntaxException | JsonIOException e) {
                    // Ignore errors -- the saves will fallback to "file" saves and display an error
                }
            }
        }

        // Ensure the saves directory exists
        if (!Save.ensureSavesDirectoryExists()) {
            return;
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

                if (CurrentDisplay.getMode().equals(Mode.GUI)) {
                    Swingy.getInstance().getUi().show();
                    Swingy.getInstance().runOnce();
                } else {
                    Swingy.getInstance().consoleLoop();
                }
            }
        });
    }
}
