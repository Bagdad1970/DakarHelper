package io.github.bagdad.common;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Slf4j
public class JsonConverter {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public static <T> Optional<T> convertToObject(Path filepath, Class<T> modelType) {
        log.info("Converting json file \"{}\" to object", filepath);

        if (!Files.exists(filepath)) {
            log.error("File {} does not exist", filepath);
            return Optional.empty();
        }

        try {
            return Optional.of(OBJECT_MAPPER.readValue(filepath.toFile(), modelType));
        }
        catch (JsonParseException e) {
            log.error("JSON syntax error in file {}: {}", filepath, e.getMessage());
            return Optional.empty();
        }
        catch (UnrecognizedPropertyException e) {
            log.error("JSON contains an unknown field: {}", e.getMessage());
            return Optional.empty();
        }
        catch (JsonMappingException e) {
            log.error("Deserialize exception while converting file to model: {}", e.getMessage());
            return Optional.empty();
        }
        catch (IOException e) {
            log.error("I/O exception while converting file to model: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public static <T> Optional<T> convertToObject(InputStream inputStream, Class<T> targetClass) {
        log.info("Converting input stream from json file to object");

        try {
            return Optional.of(OBJECT_MAPPER.readValue(inputStream, targetClass));
        }
        catch (JsonParseException e) {
            log.error("JSON syntax error in file: {}", e.getMessage());
            return Optional.empty();
        }
        catch (UnrecognizedPropertyException e) {
            log.error("JSON contains an unknown field: {}", e.getMessage());
            return Optional.empty();
        }
        catch (JsonMappingException e) {
            log.error("Deserialize exception while converting file to model: {}", e.getMessage());
            return Optional.empty();
        }
        catch (IOException e) {
            log.error("I/O exception while converting file to model: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public static void convertToFile(Object object, Path filepath) {
        log.info("Converting object to json file \"{}\"", filepath);

        try {
            OBJECT_MAPPER.writeValue(filepath.toFile(), object);
        }
        catch (JsonMappingException e) {
            log.error("Serialize exception while converting model to file: {}", e.getMessage());
        }
        catch (IOException e) {
            log.error("I/O exception while converting model to file: {}", e.getMessage());
        }
    }

}
