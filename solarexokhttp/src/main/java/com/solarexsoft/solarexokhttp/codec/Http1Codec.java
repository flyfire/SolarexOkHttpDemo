package com.solarexsoft.solarexokhttp.codec;

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 21:08/2020/2/29
 *    Desc:
 * </pre>
 */

public class Http1Codec {
    public static final String CRLF = "\r\n";
    public static final int CR = 13;
    public static final int LF = 10;
    public static final String SPACE = " ";
    public static final String VERSION = "HTTP/1.1";
    public static final String COLON = ":";

    public static final String HEAD_HOST = "Host";
    public static final String HEAD_CONNECTION = "Connection";
    public static final String HEAD_CONTENT_TYPE = "Content-Type";
    public static final String HEAD_CONTENT_LENGTH = "Content-Length";
    public static final String HEAD_TRANSFER_ENCODING = "Transfer-Encoding";

    public static final String HEAD_VALUE_KEEP_ALIVE = "Keep-Alive";
    public static final String HEAD_VALUE_CHUNKED = "chunked";
}
