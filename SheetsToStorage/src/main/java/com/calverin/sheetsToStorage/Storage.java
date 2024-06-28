package com.calverin.sheetsToStorage;

import java.net.URL;

public class Storage {
    private URL url;
    private String storage;
    private int x;
    private int y;

    private String lastValue;

    public Storage(URL url, String storage, int x, int y) {
        this.url = url;
        this.storage = storage;
        this.x = x;
        this.y = y;

        this.lastValue = "";
    }

    public URL getUrl() {
        return url;
    }

    public String getStorage() {
        return storage;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getLastValue() {
        return lastValue;
    }

    public void setLastValue(String lastValue) {
        this.lastValue = lastValue;
    }

    public String toString() {
        return "§6§n" + storage + "§r §atracking: §7§o" + url + ";\n  - §r§aLast data: §7§o" + lastValue.substring(0, Math.min(40, lastValue.length())) + (lastValue.length() > 40 ? "...};" : ";");
    }
}
