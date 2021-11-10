package at.jku.dke.etutor.grading.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class FileResourcesUtils {
    // get a file from the resources folder
    // works everywhere, IDEA, unit test and JAR file.
    public static InputStream getFileFromResourceAsStream(String fileName) {
        fileName = fileName.substring(1);
        // The class loader that loaded the class
        ClassLoader classLoader = FileResourcesUtils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);






        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            try {
                File file = File.createTempFile(UUID.randomUUID().toString(), ".xml");
                file.setWritable(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return inputStream;
        }

    }
}
