package Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    private static Database instance = null;
    private HashMap<String, Table> tables;
    Database() {
        tables = new HashMap<String, Table>();
        tables.put("&&User", new Table("./Data/Users.txt"));
        tables.put("&&Post", new Table("./Data/Posts.txt"));
        tables.put("&&subReddit", new Table("./Data/subReddits.txt"));
        this.inilialize();
    }
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }
    public void addTable(String name, String path) {
        tables.put(name, new Table(path));
    }
    public Table getTable(String name) {
        return tables.get(name);
    }
    public void inilialize() {
        try {
            ArrayList<String> fileList = tables.get("&&User").get();
            for (String line : fileList) {
                tables.put(line, new Table("./Data/" + line + ".txt"));
            }
        } catch (IOException e) {}
    }
    public void changeTable(String name, String newName, String newPath) {
        tables.get(name).delete();
        tables.remove(name);
        tables.put(newName, new Table(newPath));
    }
}
