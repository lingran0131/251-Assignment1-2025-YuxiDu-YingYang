package nz.zc.massey.texteditor.config;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.Map;

public class ConfigManager {
    private Map<String, Object> config;

    @SuppressWarnings("unchecked")
    public ConfigManager() {
        Yaml yaml = new Yaml();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.yaml")) {
            if (in == null) throw new RuntimeException("config.yaml not found!");
            this.config = yaml.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.yaml", e);
        }
    }

    public String getFontName() {
        return (String) getConfigValue("editor.font", "Monospaced");
    }

    public int getFontSize() {
        return (int) getConfigValue("editor.size", 14);
    }

    public boolean isLineWrap() {
        return (boolean) getConfigValue("editor.lineWrap", true);
    }

    public boolean isSyntaxHighlightingEnabled() {
        return (boolean) getConfigValue("features.syntax_highlighting.enabled", true);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getLanguageConfig(String extension) {
        var langs = (java.util.List<Map<String, Object>>) getConfigValue(
                "features.syntax_highlighting.languages", java.util.Collections.emptyList());
        for (var lang : langs) {
            var ext = (String) lang.get("extension");
            if (extension.equalsIgnoreCase(ext)) return lang;
        }
        return null;
    }

    private Object getConfigValue(String path, Object defaultValue) {
        String[] keys = path.split("\\.");
        Map<String, Object> current = config;
        try {
            for (int i = 0; i < keys.length - 1; i++) {
                current = (Map<String, Object>) current.get(keys[i]);
            }
            Object value = current.get(keys[keys.length - 1]);
            return value != null ? value : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
