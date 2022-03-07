package top.wuare.lang.env.buildin;

import top.wuare.lang.env.Console;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class FileReadBuildInFunc implements BuildInFunc {

    private final Logger logger = Logger.getLogger(FileReadBuildInFunc.class.getName());

    @Override
    public Object execute(List<Object> args, Console console) {
        try {
            return new String(Files.readAllBytes(Paths.get((String) args.get(0))), StandardCharsets.UTF_8);
        } catch (NoSuchFileException e) {
            console.write("ERR: no such file " + e.getMessage() + "\n");
        } catch (IOException e) {
            logger.severe(e.getMessage() + "\n");
            console.write("ERR: " + e.getMessage());
        }
        return null;
    }

    @Override
    public int args() {
        return 1;
    }
}
