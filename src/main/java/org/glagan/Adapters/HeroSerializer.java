package org.glagan.Adapters;

import java.lang.reflect.Type;

import org.glagan.Character.Hero;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class HeroSerializer implements JsonSerializer<Hero> {
    @Override
    public JsonElement serialize(Hero src, Type typeOfSrc, JsonSerializationContext context) {
        JsonElement element = src.serialize();
        if (element.isJsonObject()) {
            element.getAsJsonObject().add("type", new JsonPrimitive(src.getClass().getSimpleName()));
            element.getAsJsonObject().remove("level");
            element.getAsJsonObject().remove("baseCaracteristics");
            element.getAsJsonObject().remove("finalCaracteristics");
        }
        return element;
    }
}
