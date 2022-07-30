package me.kevsal.minecraft.jsonconfig;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Builder;
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

    @Getter
    private final Class<?> ownerClass;

    /***
     * Name of this configuration
     */
    @Getter
    private final String configName;

    /***
     * Create a new instance of the JsonConfiguration class, and setup all functionality required
     * @param dataFolder The data/config folder
     * @param configName Name of this configuration
     * @param ownerClass The class that owns this configuration
     */
    @Builder
    private JsonConfiguration(File dataFolder, String configName, Class<?> ownerClass) throws IOException {
        this.ownerClass = ownerClass;
        this.file = new File(dataFolder, configName);

        // Copy the original file from resources to the disk if it doesn't exist
        try {
            // Do not overwrite
            Files.copy(Objects.requireNonNull(ownerClass.getResourceAsStream("/%s".formatted(configName))), file.toPath());
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
        }

        return JsonParser.parseString(content).getAsJsonObject();
    }

    /***
     * Save the JsonObject to the file
     * @param jsonObject The JsonObject to save
     * @throws IOException If the file cannot be written to
     */
    public void save(JsonObject jsonObject) throws IOException {
        Files.write(file.toPath(), jsonObject.toString().getBytes());
    }
}