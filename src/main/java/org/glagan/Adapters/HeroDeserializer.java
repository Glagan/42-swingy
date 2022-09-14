package org.glagan.Adapters;

import java.lang.reflect.Type;

import org.glagan.Character.Hero;
import org.glagan.Character.HeroFactory;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class HeroDeserializer implements JsonDeserializer<Hero> {
    @Override
    public Hero deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (json.isJsonObject()) {
            JsonElement typeAsJson = json.getAsJsonObject().get("type");
            if (typeAsJson.isJsonPrimitive()) {
                String type = typeAsJson.getAsString();
                Class<?> heroClass = HeroFactory.classFromString(type);
                if (heroClass != null) {
                    return (Hero) new Gson().fromJson(json, heroClass);
                }
            }
        }
        return null;
    }

}
