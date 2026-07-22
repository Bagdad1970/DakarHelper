package io.github.bagdad.findhandler.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FileHandlerConfig {

    private Path saveDir;

}
