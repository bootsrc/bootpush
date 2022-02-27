package com.github.bootsrc.bootpush.client.constant;

import com.google.gson.Gson;

public class GsonSingleton {
    private final static Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }
}
