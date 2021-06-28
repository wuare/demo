package top.wuare.syntax;

/**
 * parser
 *
 * @author wuare
 * @date 2021/6/28
 */
public class Parser {

    private final Scanner scanner = new Scanner();
    private Scanner.Token curToken;

    public Parser() {
        scanner.init();
        curToken = scanner.next();
    }

    public static void main(String[] args) {
        Parser parser = new Parser();
        parser.parsePackageStatement();
    }

    public void parsePackageStatement() {
        match("package");
        match("org.example.controller");
        match(";");
    }

    public void match(String val) {
        if (val.equals(new String(curToken.getValue()))) {
            curToken = scanner.next();
            return;
        }
        throw new RuntimeException("syntax error");
    }
}
