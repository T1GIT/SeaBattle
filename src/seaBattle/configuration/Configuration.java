package seaBattle.configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Allows reading low-level settings from the file.
 * Isn't finished, because I understood that it is
 * uncomfortable to use
 */
public class Configuration {
    private final String PATH = "config.txt";
    private final HashMap<String, Object> conf;

    public Configuration() {
        this.conf = new HashMap<>();
        try {
            List<String> text = new ArrayList<>(Files.readAllLines(Path.of(PATH), StandardCharsets.UTF_8));
            text.forEach((row) -> conf.put(
                    row.split(":")[0].strip().toLowerCase(),
                    row.split(":")[1].strip()
            ));
        } catch (IOException e) { e.printStackTrace(); }
    }

    public String getString(String key) { return (String) conf.get(key); }

    public int getInt(String key) { return Integer.parseInt((String) conf.get(key)); }

    public double getDouble(String key) { return Double.parseDouble((String) conf.get(key)); }

    public void set(String key, Object value) {
        conf.put(key, value);
        try {
            List<String> text = new ArrayList<>(Files.readAllLines(Path.of(PATH), StandardCharsets.UTF_8));
            for (int i = 0; i < text.size(); i++) {
                String row = text.get(i);
                if (row.split(":")[0].strip().equals(key)) {
                    text.set(i, key + ":" + value);
                    break;
                }
            }
            Files.write(Path.of(PATH), text, StandardCharsets.UTF_8);
        } catch (IOException e) { e.printStackTrace(); }
    }
}
