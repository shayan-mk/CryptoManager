package com.s2k.CryptoManager.database;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.s2k.CryptoManager.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

class DatabaseUtilities {
    private static final String DATABASE_DIR = "/src/main/res/database/";
    private static final String TAG = "DatabaseUtilities";
    
    private static File getFile(String filename) {
        File file = new File(DATABASE_DIR + filename);

        Log.d(TAG, "getFile: " + file.getPath());
        Log.d(TAG, "getFile: " + file.exists());
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

    static Scanner getScanner(String filename) {
        File file = getFile(filename);
        try {
            return new Scanner(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    static PrintWriter getPrintWriter (boolean append, String filename) {
        File file = getFile(filename);
        try {
            FileWriter fileWriter = new FileWriter(file, append);
            return new PrintWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static Gson getGson() {
//        GsonBuilder builder = new GsonBuilder();
//        builder.registerTypeAdapter(CryptoData.class, new Adapter<CryptoData>());
//        gson = builder.create();
        return new Gson();
    }

//    private static class Adapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
//        private static final String CLASSNAME = "CLASSNAME";
//        private static final String INSTANCE = "INSTANCE";
//
//        @Override
//        public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
//            JsonObject retValue = new JsonObject();
//            String className = src.getClass().getName();
//            retValue.addProperty(CLASSNAME, className);
//            JsonElement elem = context.serialize(src);
//            retValue.add(INSTANCE, elem);
//            return retValue;
//        }
//
//        @Override
//        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
//                throws JsonParseException {
//            JsonObject jsonObject = json.getAsJsonObject();
//            JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
//            String className = prim.getAsString();
//
//            Class<?> classType;
//            try {
//                classType = Class.forName(className);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//                throw new JsonParseException(e.getMessage());
//            }
//            return context.deserialize(jsonObject.get(INSTANCE), classType);
//        }
//    }
}