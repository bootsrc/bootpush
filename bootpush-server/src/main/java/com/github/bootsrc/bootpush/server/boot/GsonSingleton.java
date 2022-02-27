package com.github.bootsrc.bootpush.server.boot;

import com.google.gson.Gson;

public class GsonSingleton {
    private final static Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }
}
