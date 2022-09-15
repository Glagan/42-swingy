package org.glagan.World;

import com.github.tomaslanger.chalk.Chalk;

public enum Biome {
    PLAIN,
    FOREST,
    JUNGLE,
    MOUTAIN,
    DESERT,
    TAIGA,
    TUNDRA,
    SWAMP,
    SAVANNAH,
    BADLAND,
    BEACH,
    OCEAN,
    ARENA;

    public static Chalk printText(Biome biome, String text) {
        switch (biome) {
            case PLAIN:
                return Chalk.on(text).green();
            case FOREST:
                return Chalk.on(text).black().bgGreen();
            case JUNGLE:
                return Chalk.on(text).black().bgGreen();
            case MOUTAIN:
                return Chalk.on(text).black().bgWhite();
            case DESERT:
                return Chalk.on(text).black().bgYellow();
            case TAIGA:
                return Chalk.on(text).white().bgYellow();
            case TUNDRA:
                return Chalk.on(text).white().bgYellow();
            case SWAMP:
                return Chalk.on(text).white().bgMagenta();
            case SAVANNAH:
                return Chalk.on(text).green().bgYellow();
            case BADLAND:
                return Chalk.on(text).red().bgYellow();
            case BEACH:
                return Chalk.on(text).blue().bgYellow();
            case OCEAN:
                return Chalk.on(text).white().bgCyan();
            case ARENA:
                return Chalk.on(text).white().bgRed();
        }
        return null;
    }
}
