package org.glagan.Adapters;

import org.glagan.Character.Hero;

import com.google.gson.GsonBuilder;

public class GsonCustomBuilder {
    static protected GsonCustomBuilder instance = new GsonCustomBuilder();

    static protected GsonBuilder builder;

    public GsonCustomBuilder() {
        builder = new GsonBuilder();
        builder.registerTypeAdapter(Hero.class, new HeroSerializer());
        builder.registerTypeAdapter(Hero.class, new HeroDeserializer());
    }

    static public GsonBuilder getBuilder() {
        return GsonCustomBuilder.builder;
    }
}
