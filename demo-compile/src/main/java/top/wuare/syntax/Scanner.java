package top.wuare.syntax;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Scanner of java source file
 *
 * @author wuare
 */
public class Scanner {
    private InputStream in;
    private final byte[] buf = new byte[2048];
    private int pos;
    private int limit;

    private int ch;

    public void init() {
        this.pos = 0;
        try {
            String javaFilePath = System.getProperty("javaFilePath", "E:/A0.java");
            File file = new File(javaFilePath);
            if (!file.exists()) {
                throw new RuntimeException("[" + javaFilePath + "] File Not Found...");
            }
            in = new BufferedInputStream(new FileInputStream(file));
            this.limit = in.read(buf);
            ch = nextCh();
        } catch (IOException e) {
            closeInputStream();
            throw new RuntimeException("Read File Error...");
        }
    }

    public void closeInputStream() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ignored) {
            }
        }
    }

    public Token next() {
        if (ch == -1) {
            return null;
        }
        // skip space character
        while (Character.isWhitespace(ch)) {
            ch = nextCh();
        }
        if (Character.isLetter(ch)) {
            StringBuilder builder = new StringBuilder();
            builder.append((char) ch);
            for (; ; ) {
                ch = nextCh();
                if (Character.isLetterOrDigit(ch)) {
                    builder.append((char) ch);
                    continue;
                }
                if (ch == '_' || ch == '.') {
                    builder.append((char) ch);
                    continue;
                }
                break;
            }
            return new Token(1, builder.toString().getBytes());
        }
        if (Character.isDigit(ch)) {
            StringBuilder builder = new StringBuilder();
            builder.append((char) ch);
            for (; ; ) {
                ch = nextCh();
                if (Character.isDigit(ch)) {
                    builder.append((char) ch);
                    continue;
                }
                if (ch == '.') {
                    builder.append((char) ch);
                    continue;
                }
                break;
            }
            return new Token(2, builder.toString().getBytes());
        }
        switch (ch) {
            case -1:
                return null;
            case '(':
                return singleToken(3, ch);
            case ')':
                return singleToken(4, ch);
            case '[':
                return singleToken(5, ch);
            case ']':
                return singleToken(6, ch);
            case '{':
                return singleToken(7, ch);
            case '}':
                return singleToken(8, ch);
            case ',':
                return singleToken(9, ch);
            case ';':
                return specToken(10, ";");
            case '\n':
                return specToken(11, "");
            case '\r':
                return specToken(12, "");
            case '/':
                StringBuilder builder = new StringBuilder();
                builder.append((char) ch);
                ch = nextCh();
                builder.append((char) ch);
                if (ch == '/') {
                    for (; ; ) {
                        ch = nextCh();
                        if (ch != '\n') {
                            // TODO check ch == -1
                            builder.append((char) ch);
                            continue;
                        }
                        break;
                    }
                }
                if (ch == '*') {
                    for (; ; ) {
                        int last = ch;
                        ch = nextCh();
                        builder.append((char) ch);
                        // TODO check ch == -1
                        if (last == '*' && ch == '/') {
                            ch = nextCh(); // eat
                            break;
                        }
                    }
                }
                return new Token(13, builder.toString().getBytes());
            case '"':
                // string
                StringBuilder strB = new StringBuilder();
                strB.append((char) ch);
                for (; ; ) {
                    ch = nextCh();
                    if (ch == -1) {
                        throw new RuntimeException("read error, EOF when parse string ");
                    }
                    strB.append((char) ch);
                    if (ch == '"') {
                        ch = nextCh();
                        break;
                    }
                }
                return new Token(14, strB.toString().getBytes());
            case '+':
                // +
                // ++
                // +=
                ch = nextCh();
                if (ch == '+') {
                    return specToken(15, "++");
                }
                if (ch == '=') {
                    return specToken(16, "+=");
                }
                return plainToken(17, "+");
            case '-':
                // -
                // --
                // -=
                ch = nextCh();
                if (ch == '-') {
                    return specToken(18, "--");
                }
                if (ch == '=') {
                    return specToken(19, "-=");
                }
                return plainToken(20, "-");
            case '*':
                // *
                // *=
                ch = nextCh();
                if (ch == '=') {
                    return specToken(21, "*=");
                }
                return plainToken(22, "*");
            case '=':
                // =
                // ==
                ch = nextCh();
                if (ch == '=') {
                    return specToken(23, "==");
                }
                return plainToken(24, "=");
            case '&':
                // & &&
                ch = nextCh();
                if (ch == '&') {
                    return specToken(25, "&&");
                }
                return plainToken(26, "&");
            case '|':
                // | ||
                ch = nextCh();
                if (ch == '|') {
                    return specToken(27, "||");
                }
                return plainToken(28, "|");
            case '\'':
                StringBuilder cB = new StringBuilder();
                cB.append((char) ch);
                ch = nextCh();
                cB.append((char) ch);
                if (ch == '\\') {
                    ch = nextCh();
                    cB.append((char) ch);
                }
                ch = nextCh();
                cB.append((char) ch);
                return specToken(29, cB.toString());
            default:
                return singleToken(0, ch);
        }
    }

    public Token singleToken(int type, int ch) {
        this.ch = nextCh();
        String s = Character.toString((char) ch);
        return new Token(type, s.getBytes());
    }

    public Token specToken(int type, String s) {
        ch = nextCh();
        return new Token(type, s.getBytes());
    }

    public Token plainToken(int type, String s) {
        return new Token(type, s.getBytes());
    }

    public byte nextCh() {
        if (limit == -1) {
            return -1;
        }
        if (limit > pos) {
            return buf[pos++];
        }
        if (limit == buf.length) {
            fill();
            return nextCh();
        }
        return -1;
    }

    public void fill() {
        try {
            this.limit = in.read(buf);
            this.pos = 0;
        } catch (IOException e) {
            this.limit = -1;
            closeInputStream();
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner();
        scanner.init();
        Token t;
        while ((t = scanner.next()) != null) {
            String s = new String(t.getValue());
            System.out.println(s);
        }
        scanner.closeInputStream();
    }

    public static class Token {
        private int type;
        private byte[] value;

        public Token() {
        }

        public Token(int type, byte[] value) {
            this.type = type;
            this.value = value;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public byte[] getValue() {
            return value;
        }

        public void setValue(byte[] value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Token{" +
                    "type=" + type +
                    ", value=" + new String(value) +
                    '}';
        }
    }
}
