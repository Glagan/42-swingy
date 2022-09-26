package org.glagan.Controller;

import org.glagan.Character.Hero;
import org.glagan.Character.HeroFactory;
import org.glagan.Core.Game;
import org.glagan.Core.Save;
import org.glagan.View.HeroCreation;
import org.glagan.View.SaveIndex;

public class SaveController extends Controller {
    protected int selected;
    protected Save[] saves;

    // Hero creation logic
    protected String heroName;
    protected String heroClass;

    public SaveController(org.glagan.Core.Swingy swingy) {
        super(swingy);
        this.reset();
        this.saves = null;
    }

    public Save[] reloadSaves() {
        this.saves = Save.all();
        return saves;
    }

    public Save[] getSaves() {
        return saves;
    }

    @Override
    public void reset() {
        this.selected = -1;
        this.heroName = null;
        this.heroClass = null;
    }

    protected void selectSave(int id) {
        if (id > 0 && id - 1 < saves.length) {
            Save save = saves[id - 1];
            if (save.isCorrupted()) {
                System.out.println("The selected save is corrupted and can't be played");
            } else {
                System.out.println("Playing as " + save.getGame().getHero().getName());
                swingy.setGame(save.getGame());
                swingy.useGameController();
            }
        } else {
            System.out.println("The selected save doesn't exists");
        }
    }

    @Override
    public boolean handle(String event) {
        if (handleGlobalCommand(event)) {
            return true;
        }
        if (event.equalsIgnoreCase("c") || event.equalsIgnoreCase("create")) {
            new HeroCreation(this).render();
            return true;
        } else if (event.startsWith("s ") || event.startsWith("select ")) {
            String[] parts = event.split(" ");
            if (parts.length != 2) {
                System.out.println("Invalid command `" + event + "`");
            } else {
                try {
                    int id = Integer.parseInt(parts[1]);
                    if (id <= 0) {
                        System.out.println("Invalid selected hero `" + id + "`");
                    } else {
                        selectSave(id);
                        return true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid selected hero `" + parts[1] + "`");
                }
            }
        } else if (event.equalsIgnoreCase("l") || event.equalsIgnoreCase("list")) {
            return true;
        } else if (event.startsWith("d ") || event.startsWith("delete ")) {
            String[] parts = event.split(" ");
            if (parts.length != 2) {
                System.out.println("Invalid command `" + event + "`");
            } else {
                try {
                    int id = Integer.parseInt(parts[1]);
                    if (id <= 0) {
                        System.out.println("Invalid selected hero `" + id + "`");
                    } else if (id <= 0 || id > saves.length) {
                        System.out.println("The selected save doesn't exists");
                    } else {
                        Save save = saves[id - 1];
                        System.out.println("Deleting " + save.getPath());
                        save.delete();
                        reloadSaves();
                        return true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid selected hero `" + parts[1] + "`");
                }
            }
        } else if (event.startsWith("set-name ")) {
            String[] split = event.split(" ");
            if (split.length != 2) {
                return false;
            }
            if (split[1].length() < 1 || split[1].length() > 20) {
                System.out.println("Invalid name, must be between 1 and 20 characters");
                return false;
            }
            heroName = split[1];
            return true;
        } else if (event.startsWith("set-class ")) {
            String[] split = event.split(" ");
            if (split.length != 2) {
                System.out.println("Invalid class, expected Magician, Paladin or Warrior");
                return false;
            }
            heroClass = split[1];
            if (heroClass.equalsIgnoreCase("magician") || heroClass.equalsIgnoreCase("warrior")
                    || heroClass.equalsIgnoreCase("paladin")) {
                return true;
            } else {
                System.out.println("Invalid class");
            }
        } else if (event.equalsIgnoreCase("finalize-hero")) {
            Hero hero = HeroFactory.newHero(heroClass, heroName);
            hero.calculateFinalCaracteristics();
            Game game = new Game(hero, null, null, null);
            game.generateSavePath();
            game.save();
            swingy.setGame(game);
            swingy.useGameController();
            return true;
        } else {
            System.out.println("Invalid command `" + event + "`");
        }
        return false;
    }

    @Override
    public void run() {
        Save[] saves = this.getSaves();
        new SaveIndex(this, saves).render();
    }
}
