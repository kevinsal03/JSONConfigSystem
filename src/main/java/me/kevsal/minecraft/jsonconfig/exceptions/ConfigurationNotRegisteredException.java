package me.kevsal.minecraft.jsonconfig.exceptions;

import javax.naming.ConfigurationException;

public class ConfigurationNotRegisteredException extends ConfigurationException {
    public ConfigurationNotRegisteredException(String message) {
        super(message);
    }
}
