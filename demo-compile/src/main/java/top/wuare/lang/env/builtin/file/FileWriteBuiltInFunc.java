package top.wuare.lang.env.builtin.file;

import top.wuare.lang.env.Console;
import top.wuare.lang.env.builtin.BuiltInFunc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class FileWriteBuiltInFunc implements BuiltInFunc {

    private final Logger logger = Logger.getLogger(FileWriteBuiltInFunc.class.getName());

    @Override
    public Object execute(List<Object> args, Console console) {
        try {
            Files.write(Paths.get((String) args.get(0)), ((String) args.get(1)).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            logger.severe(e.getMessage());
            console.write("ERR: " + e.getMessage() + "\n");
            return false;
        }
        return true;
    }

    @Override
    public int args() {
        return 2;
    }
}
