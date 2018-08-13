package com.patrick.hess.util;

import org.apache.commons.lang3.StringUtils;

public final class DomainUtils {

    public static String getDomainName(String url) {
        return getDomainName(url, false);
    }

    public static String getDomainName(String url, boolean withProtocol) {
        if (StringUtils.isBlank(url)) {
            return null;
        }

        url = PageUtils.trimQueryParams(url);

        int doubleSlash = url.indexOf("//");
        if (doubleSlash == -1)
            doubleSlash = 0;
        else
            doubleSlash += 2;

        int end = url.indexOf('/', doubleSlash);
        end = end >= 0 ? end : url.length();

        int port = url.indexOf(':', doubleSlash);
        end = (port > 0 && port < end) ? port : end;
        int start = withProtocol ? 0 : doubleSlash;
        return url.substring(start, end);
    }

    public static String getBaseDomainName(String url) {
        return getBaseDomainName(url, false);
    }

    public static String getBaseDomainName(String url, boolean withProtocol) {

        String host = getDomainName(url, withProtocol);

        if (StringUtils.isBlank(host)) {
            return null;
        }

        int startIndex = 0;
        int nextIndex = host.indexOf('.');
        int lastIndex = host.lastIndexOf('.');
        while (nextIndex < lastIndex) {
            startIndex = nextIndex + 1;
            nextIndex = host.indexOf('.', startIndex);
        }
        if (startIndex > 0) {
            return host.substring(startIndex);
        } else {
            return host;
        }
    }

}