package com.patrick.hess.util;

import org.apache.commons.lang3.StringUtils;

public final class PageUtils {

    public static String trimQueryParams(String link){
        if(StringUtils.isBlank(link)) return null;

        int questionMarkIndex = link.indexOf('?');

        return questionMarkIndex == -1 ? link : link.substring(0, questionMarkIndex);
    }

    public static String trimProtocol(String url){
        if(StringUtils.isBlank(url)) {
            return null;
        }

        url = PageUtils.trimQueryParams(url);

        int doubleSlash = url.indexOf("//");
        if(doubleSlash == -1)
            doubleSlash = 0;
        else
            doubleSlash += 2;

        return url.substring(doubleSlash);
    }
}