package org.glagan.Adapters;

import org.glagan.Character.Hero;

import com.google.gson.GsonBuilder;

public class GsonCustomBuilder {
    static protected GsonBuilder builder;

    static public GsonBuilder getBuilder() {
        if (builder == null) {
            builder = new GsonBuilder();
            builder.registerTypeAdapter(Hero.class, new HeroSerializer());
            builder.registerTypeAdapter(Hero.class, new HeroDeserializer());
        }
        return builder;
    }
}
