package com.bs.dbperformancemetrics.utils;

import com.bs.dbperformancemetrics.model.IUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
public class UserJsonCreator<ID, T extends IUser<ID>> {

    public void createJsonFromUsers(List<T> users, String jsonFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(users);
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(jsonFilePath), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(json);
        }
    }
}
