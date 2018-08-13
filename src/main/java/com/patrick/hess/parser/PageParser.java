package com.patrick.hess.parser;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import com.patrick.hess.database.PageStats;
import com.patrick.hess.util.DomainUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PageParser {

    public static final Logger LOG = LoggerFactory.getLogger(PageParser.class);

    protected WebClient webClient;

    public void setup() {
        webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setRedirectEnabled(true);
        //fix javax.net.ssl.SSLProtocolException: handshake alert: unrecognized_name
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.waitForBackgroundJavaScript(10000);
//        JavaScriptEngine engine = webClient.getJavaScriptEngine();
//        engine.holdPosponedActions();
    }

    public void destroy() {
        webClient.close();
    }

    public PageStats fillPageStats(PageStats pageStats) {

        try {
            HtmlPage linkPage = webClient.getPage(pageStats.getLink());
            if (Objects.nonNull(linkPage) && Objects.nonNull(linkPage.getDocumentElement())) {
//                waitForLoad(linkPage);
                fillHtmlElements(pageStats, linkPage);
                fillLinks(pageStats, linkPage);

                String pageText = linkPage.asText();
                String[] pageLines = pageText.split("/n");
                int lineCount = 0;
                int letterCount = 0;
                int numberCount = 0;
                int wordCount = 0;
                int sentenceCount = 0;
                boolean word = false;
                int sentence = 0;
                for (String pageLine : pageLines) {
                    if (StringUtils.isNotBlank(pageLine)) {
                        lineCount++;
                        for (int i = 0; i < pageLine.length(); i++) {
                            if (Character.isDigit(pageLine.charAt(i))) {
                                numberCount++;
                                word = true;
                            } else if (Character.isLetter(pageLine.charAt(i))) {
                                letterCount++;
                                word = true;
                            } else if (Character.isSpaceChar(pageLine.charAt(i))) {
                                if (word) {
                                    wordCount++;
                                    if (sentence == 4) {
                                        sentenceCount++;
                                        sentence = 0;
                                    } else {
                                        sentence++;
                                    }
                                }
                                word = false;
                            } else {
                                if (pageLine.charAt(i) == '\n') {
                                    lineCount++;
                                }
                                if (word) {
                                    wordCount++;
                                    if (sentence == 4) {
                                        sentenceCount++;
                                    }
                                }
                                word = false;
                                sentence = 0;
                            }
                        }
                        word = false;
                        sentence = 0;
                    }
                }
                pageStats.setLineCount(lineCount);
                pageStats.setNumberCount(numberCount);
                pageStats.setLetterCount(letterCount);
                pageStats.setWordCount(wordCount);
                pageStats.setSentenceCount(sentenceCount);
            }
        } catch (IOException e) {
            LOG.warn(String.format("Parsing page: %s failed.", pageStats.getLink()), e);
        } finally {
            clearWebClient();
        }

        return pageStats;
    }

    private void fillHtmlElements(PageStats pageStats, HtmlPage linkPage) {
        String title = linkPage.getTitleText();
        pageStats.setTitle(title);
        String langAttr = linkPage.getDocumentElement().getAttribute("lang");
        pageStats.setLanguageTag(langAttr);
        int headerCount = linkPage.querySelectorAll("h1,h2,h3,h4,h5,h6,H1,H2,H3,H4,H5,H6").size();
        pageStats.setHtmlHeadingCount(headerCount);
        int paragraphCount = linkPage.getElementsByTagName("p").size();
        pageStats.setHtmlParagraphCount(paragraphCount);
        int divCount = linkPage.getElementsByTagName("div").size();
        pageStats.setHtmlDivCount(divCount);
    }

    private void fillLinks(PageStats pageStats, HtmlPage linkPage) {
        String domainName = DomainUtils.getDomainName(pageStats.getLink(), false);
        String domainNameWithProtocol = DomainUtils.getDomainName(pageStats.getLink(), true);
        int externalLinkCount = 0;
        int internalLinkCount = 0;
        StringBuilder listOfLinks = new StringBuilder();
        List<HtmlAnchor> anchors = linkPage.getAnchors();
        List<String> links = anchors.stream().map(HtmlAnchor::getHrefAttribute).collect(Collectors.toList());
        for (String link : links) {
            if (Objects.nonNull(domainName)) {
                if (link.startsWith("/")) {
                    internalLinkCount++;
                    link = domainNameWithProtocol + link;
                    listOfLinks.append(link).append(",");
                } else if (link.toLowerCase().contains(domainName.toLowerCase())) {
                    internalLinkCount++;
                    listOfLinks.append(link).append(",");
                } else if (link.startsWith("http")) {
                    externalLinkCount++;
                    listOfLinks.append(link).append(",");
                }
            }
        }
        pageStats.setExternalLinkCount(externalLinkCount);
        pageStats.setInternalLinkCount(internalLinkCount);
        pageStats.setListOfLinks(listOfLinks.toString());
    }


    //If data loads through api calls, we should wait until js scripts fill html page
    protected void waitForLoad(final Page page) {
        waitForLoad(page, 1000);
    }

    protected void waitForLoad(final Page page, long millis) {
        JavaScriptJobManager manager = page.getEnclosingWindow().getJobManager();
        //TODO use com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManagerImpl.getJobCount(com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager.JavaScriptJobFilter)
        while (manager.getJobCount() > 0) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
    }

    protected void clearWebClient() {
        webClient.getCookieManager().clearCookies();
        webClient.getCache().clear();
        for (final TopLevelWindow topWindow : webClient.getTopLevelWindows()) {
            topWindow.close();
        }
    }
}
