package com.s2k.CryptoManager.database;

import android.util.Log;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

class DatabaseUtility {
    private final File databaseDir;

    DatabaseUtility(File databaseDir) {
        this.databaseDir = databaseDir;
    }

    private File getFile(String filename) {
        File file = new File(databaseDir, filename);
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

    Scanner getScanner(String filename) {
        File file = getFile(filename);
        try {
            return new Scanner(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    PrintWriter getPrintWriter (boolean append, String filename) {
        File file = getFile(filename);
        try {
            FileWriter fileWriter = new FileWriter(file, append);
            return new PrintWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}