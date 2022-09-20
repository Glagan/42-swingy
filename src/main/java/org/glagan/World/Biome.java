package org.glagan.World;

import javax.swing.JLabel;
import javax.swing.plaf.ColorUIResource;

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

    public static void setColors(Biome biome, JLabel label) {
        switch (biome) {
            case PLAIN:
                label.setForeground(new ColorUIResource(255, 255, 255));
                label.setBackground(new ColorUIResource(60, 200, 60));
                break;
            case FOREST:
                label.setForeground(new ColorUIResource(0, 0, 0));
                label.setBackground(new ColorUIResource(60, 200, 60));
                break;
            case JUNGLE:
                label.setForeground(new ColorUIResource(0, 0, 0));
                label.setBackground(new ColorUIResource(60, 200, 60));
                break;
            case MOUTAIN:
                label.setForeground(new ColorUIResource(24, 24, 24));
                label.setBackground(new ColorUIResource(200, 200, 200));
                break;
            case DESERT:
                label.setForeground(new ColorUIResource(0, 0, 0));
                label.setBackground(new ColorUIResource(249, 229, 134));
                break;
            case TAIGA:
                label.setForeground(new ColorUIResource(0, 0, 0));
                label.setBackground(new ColorUIResource(250, 173, 30));
                break;
            case TUNDRA:
                label.setForeground(new ColorUIResource(0, 0, 0));
                label.setBackground(new ColorUIResource(250, 173, 30));
                break;
            case SWAMP:
                label.setForeground(new ColorUIResource(255, 255, 255));
                label.setBackground(new ColorUIResource(34, 115, 52));
                break;
            case SAVANNAH:
                label.setForeground(new ColorUIResource(60, 200, 60));
                label.setBackground(new ColorUIResource(133, 106, 32));
                break;
            case BADLAND:
                label.setForeground(new ColorUIResource(200, 60, 60));
                label.setBackground(new ColorUIResource(94, 67, 15));
                break;
            case BEACH:
                label.setForeground(new ColorUIResource(60, 60, 200));
                label.setBackground(new ColorUIResource(181, 158, 11));
                break;
            case OCEAN:
                label.setForeground(new ColorUIResource(255, 255, 255));
                label.setBackground(new ColorUIResource(32, 78, 158));
                break;
            case ARENA:
                label.setForeground(new ColorUIResource(255, 255, 255));
                label.setBackground(new ColorUIResource(82, 10, 19));
                break;
        }
    }

    public static Chalk printText(Biome biome, String text) {
        switch (biome) {
            case PLAIN:
                return Chalk.on(text).green().bgWhite();
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
