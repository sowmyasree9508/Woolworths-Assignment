package util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;

public class OutputFileWriter {
    private static final Logger LOG = LogManager.getLogger(MyTestListener.class);

    public OutputFileWriter(String fileName, String fileExtension, String fileContent) {
        try {
            FileWriter myWriter = new FileWriter("target/"+fileName+"."+fileExtension);
            myWriter.write(fileContent);
            myWriter.close();
            LOG.info("Successfully wrote to the file.");
        } catch (IOException e) {
            LOG.error("An error occurred in file write operation");
            e.printStackTrace();
        }
    }
}
