package me.kevsal.minecraft;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Logger;

/***
 * Generic Json configuration class
 */
public class JsonConfiguration {

    /***
     * The configuration on the disk
     */
    @Getter
    private final File file;

    /***
     * The parsed Gson Json object
     */
    @Getter
    private final JsonObject jsonObject;

    /***
     * Name of this configuration
     */
    @Getter
    private final String configName;

    /***
     * Create a new instance of the JsonConfiguration class, and setup all functionality required
     * @param dataFolder The data/config folder
     * @param configName Name of this configuration
     */
    public JsonConfiguration(File dataFolder, String configName) throws IOException {
        this.file = new File(dataFolder, configName);

        // Copy the original file from resources to the disk if it doesn't exist
        try {
            // Do not overwrite
            Files.copy(Objects.requireNonNull(getClass().getResourceAsStream("/%s".formatted(configName))), file.toPath());
        } catch (FileAlreadyExistsException ignored) {
            // File already exists, do nothing
        }
        this.jsonObject = getJsonFromFile(this.file);
        this.configName = configName;
    }

    /***
     * Get the JsonObject from the file
     * @param file The file to get the JsonObject from
     * @return The JsonObject from the file
     */
    private JsonObject getJsonFromFile(File file) {
        Path path = Paths.get(file.toString());
        String content;
        try {
            content = Files.readString(path);
        } catch (IOException e) {
            content = "{}";
            e.printStackTrace();
            Logger.getGlobal().warning("[GBMCCore - JsonConfiguration] [%s] Failed to read file %s".formatted(configName, file.toString()));
        }

        return JsonParser.parseString(content).getAsJsonObject();
    }

}