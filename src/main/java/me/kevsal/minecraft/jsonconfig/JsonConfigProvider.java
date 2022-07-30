package me.kevsal.minecraft.jsonconfig;

import lombok.Getter;
import me.kevsal.minecraft.jsonconfig.exceptions.ConfigurationNotRegisteredException;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The main class for the JsonConfig library.
 * <p>
 * This class is responsible for managing all JsonConfig instances.
 *
 * Only one instance should be created for each owner class. (Likely one per plugin)
 * </p>
 */
public class JsonConfigProvider {

    private final ConcurrentHashMap<String, JsonConfiguration> jsonConfigurationConcurrentHashMap = new ConcurrentHashMap<>();
    @Getter
    private final File dataFolder;
    @Getter
    private final Class<?> ownerClass;

    private JsonConfigProvider (Class<?> ownerClass, File dataFolder) {
        this.dataFolder = dataFolder;
        this.ownerClass = ownerClass;
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    /**
     * Get the JsonConfiguration instance for the given name
     * @param configName name of the configuration
     * @return the JsonConfiguration instance
     * @throws ConfigurationNotRegisteredException if the configuration is not registered
     */
    public JsonConfiguration getJsonConfiguration(String configName) throws ConfigurationNotRegisteredException {
        if (!jsonConfigurationConcurrentHashMap.containsKey(configName)) {
            throw new ConfigurationNotRegisteredException(configName + " is not registered!");
        }
        return jsonConfigurationConcurrentHashMap.get(configName);
    }

    /**
     * Register a new JsonConfiguration
     * @param configName Name of the configuration
     * @throws IOException If the file cannot be created
     */
    public JsonConfiguration createJsonConfiguration(String configName) throws IOException {
        if (jsonConfigurationConcurrentHashMap.containsKey(configName)) {
            throw new IOException("Configuration " + configName + " already exists!");
        }
        JsonConfiguration jsonConfiguration = new JsonConfiguration.JsonConfigurationBuilder()
                .dataFolder(dataFolder)
                .configName(configName + ".json")
                .build();
        jsonConfigurationConcurrentHashMap.put(configName, jsonConfiguration);
        return jsonConfiguration;
    }


    /**
     * Create a new instance of the JsonConfigProvider class, and setup all functionality required
     * ownerClass The class that is using this provider
     * dataFolder The data/config folder
     */
    public static class JsonConfigProviderBuilder {
        private File dataFolder;
        private Class<?> ownerClass;

        /**
         * @param dataFolder The folder that configurations should be stored in
         * @return The builder
         */
        public JsonConfigProviderBuilder dataFolder(File dataFolder) {
            this.dataFolder = dataFolder;
            return this;
        }

        /**
         * @param clazz The class that is using this provider
         * @return The builder
         */
        public JsonConfigProviderBuilder ownerClass(Class<?> clazz) {
            this.ownerClass = clazz;
            return this;
        }

        /**
         * @return The JsonConfigProvider instance
         */
        public JsonConfigProvider build() throws IOException {
            return new JsonConfigProvider(ownerClass, dataFolder);
        }
    }

}
