package com.bs.dbperformancemetrics.service.oracle;

import com.bs.dbperformancemetrics.model.OracleUser;
import com.bs.dbperformancemetrics.service.DatabaseUserJsonUploader;
import com.bs.dbperformancemetrics.service.oracle.jpa.OracleUserJPAServiceImp;
import com.bs.dbperformancemetrics.utils.RandomUserGenerator;
import com.bs.dbperformancemetrics.utils.UserJsonCreator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class OracleUserUploader extends DatabaseUserJsonUploader<Long, OracleUser> {
    private final OracleUserJPAServiceImp oracleUserService;

    public OracleUserUploader(OracleUserJPAServiceImp oracleUserService, RandomUserGenerator<Long, OracleUser> randomUserGenerator,
                              UserJsonCreator<Long, OracleUser> userJsonCreator) {
        super(randomUserGenerator, userJsonCreator);
        this.oracleUserService = oracleUserService;
    }

    @Override
    protected String getUserType() {
        return "OracleUser";
    }

    @Override
    public void uploadUsersFromJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Path filePath = Paths.get(getJsonFilePath());
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("File not found at " + filePath.toString());
        }
        List<OracleUser> users = mapper.readValue(filePath.toFile(), new TypeReference<List<OracleUser>>() {
        });

        oracleUserService.saveAll(users);
    }
}
