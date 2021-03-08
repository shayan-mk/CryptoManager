package com.s2k.CryptoManager.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.s2k.CryptoManager.CryptoData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Scanner;

class DatabaseUtilities {
    private static final String DATABASE_DIR = "/";

    private static File getFile(String fileName) {
        File file = new File(DATABASE_DIR + fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    static Scanner getScanner(String fileName) {
        File file = getFile(fileName);
        try {
            return new Scanner(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    static PrintWriter getPrintWriter(String fileName, boolean append) {
        File file = getFile(fileName);
        try {
            FileWriter fileWriter = new FileWriter(file, append);
            return new PrintWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(CryptoData.class, new Adapter<CryptoData>());
        return builder.create();
    }

    private static class Adapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
        private static final String CLASSNAME = "CLASSNAME";
        private static final String INSTANCE = "INSTANCE";

        @Override
        public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject retValue = new JsonObject();
            String className = src.getClass().getName();
            retValue.addProperty(CLASSNAME, className);
            JsonElement elem = context.serialize(src);
            retValue.add(INSTANCE, elem);
            return retValue;
        }

        @Override
        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
            String className = prim.getAsString();

            Class<?> classType;
            try {
                classType = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new JsonParseException(e.getMessage());
            }
            return context.deserialize(jsonObject.get(INSTANCE), classType);
        }
    }
}