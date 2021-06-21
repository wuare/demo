package top.wuare.http.parser;

import org.junit.Assert;
import org.junit.Test;
import top.wuare.http.proto.HttpRequestLine;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * test http message parser
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpMessageParserTest {

    @Test
    public void testParseRequestLine() {
        String line = " POST /xx/ HTTP/1.1\r\n";
        InputStream in = new ByteArrayInputStream(line.getBytes());
        HttpMessageParser parser = new HttpMessageParser();
        HttpRequestLine httpLine = (HttpRequestLine) parser.parseRequestLine(in);
        Assert.assertEquals("POST", httpLine.getMethod());
        Assert.assertEquals("/xx/", httpLine.getUrl());
        Assert.assertEquals("HTTP/1.1", httpLine.getVersion());
    }
}
