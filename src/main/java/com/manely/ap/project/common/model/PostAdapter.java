package com.manely.ap.project.common.model;

import com.google.gson.*;

import java.lang.reflect.Type;

public class PostAdapter implements JsonSerializer<Post>, JsonDeserializer<Post> {
    private static final String CLASS_TYPE = "CLASS_TYPE";

    @Override
    public Post deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObj = jsonElement.getAsJsonObject();
        String className = jsonObj.get(CLASS_TYPE).getAsString();

        try {
            Class<?> clz = Class.forName(className);
            return jsonDeserializationContext.deserialize(jsonElement, clz);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(Post object, Type type, JsonSerializationContext jsonSerializationContext) {

        Gson gson = new Gson();
        gson.toJson(object, object.getClass());
        JsonElement jsonElement = gson.toJsonTree(object);
        jsonElement.getAsJsonObject().addProperty(CLASS_TYPE, object.getClass().getCanonicalName());
        return jsonElement;
    }
}
