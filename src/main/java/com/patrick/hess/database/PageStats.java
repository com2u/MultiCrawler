package com.patrick.hess.database;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.Instant;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "page",
        uniqueConstraints = {
                @UniqueConstraint(name = "uniqueUrl", columnNames = "url"),
                @UniqueConstraint(name = "uniqueLink", columnNames = "link")
        })
@GenericGenerator(
        name = "seq_page_generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = @org.hibernate.annotations.Parameter(
                name = "sequence_name", value = "seq_page_id")
)
public class PageStats {

    @Id
    @GeneratedValue(generator = "seq_page_generator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id")
    private Domain domain;

    @Column(name = "link", length = 2000, nullable = false)
    private String link;

    @Column(name = "url", length = 2000, nullable = false)
    private String url;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "language_tag", length = 255)
    private String languageTag;

    @Column(name = "line_count")
    private Integer lineCount;

    @Column(name = "number_count")
    private Integer numberCount;

    @Column(name = "letter_count")
    private Integer letterCount;

    @Column(name = "word_count")
    private Integer wordCount;

    @Column(name = "sentence_count")
    private Integer sentenceCount;

    @Column(name = "html_heading_count")
    private Integer htmlHeadingCount;

    @Column(name = "html_paragraph_count")
    private Integer htmlParagraphCount;

    @Column(name = "html_div_count")
    private Integer htmlDivCount;

    @Column(name = "internal_link_count")
    private Integer internalLinkCount;

    @Column(name = "external_link_count")
    private Integer externalLinkCount;

    @Column(name = "list_of_links")
    private String listOfLinks;

    @Column(name = "visited_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentInstantAsTimestamp")
    private Instant visitedDate;

    @Column(name = "created_date", nullable = false, updatable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentInstantAsTimestamp")
    private Instant createdDate;

    public Long getId() {
        return id;
    }

    public PageStats setId(Long id) {
        this.id = id;
        return this;
    }

    public Domain getDomain() {
        return domain;
    }

    public PageStats setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }

    public String getLink() {
        return link;
    }

    public PageStats setLink(String link) {
        this.link = link;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public PageStats setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PageStats setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getLanguageTag() {
        return languageTag;
    }

    public PageStats setLanguageTag(String languageTag) {
        this.languageTag = languageTag;
        return this;
    }

    public Integer getLineCount() {
        return lineCount;
    }

    public PageStats setLineCount(Integer lineCount) {
        this.lineCount = lineCount;
        return this;
    }

    public Integer getNumberCount() {
        return numberCount;
    }

    public PageStats setNumberCount(Integer numberCount) {
        this.numberCount = numberCount;
        return this;
    }

    public Integer getLetterCount() {
        return letterCount;
    }

    public PageStats setLetterCount(Integer letterCount) {
        this.letterCount = letterCount;
        return this;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public PageStats setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
        return this;
    }

    public Integer getSentenceCount() {
        return sentenceCount;
    }

    public PageStats setSentenceCount(Integer sentenceCount) {
        this.sentenceCount = sentenceCount;
        return this;
    }

    public Integer getHtmlHeadingCount() {
        return htmlHeadingCount;
    }

    public PageStats setHtmlHeadingCount(Integer htmlHeadingCount) {
        this.htmlHeadingCount = htmlHeadingCount;
        return this;
    }

    public Integer getHtmlParagraphCount() {
        return htmlParagraphCount;
    }

    public PageStats setHtmlParagraphCount(Integer htmlParagraphCount) {
        this.htmlParagraphCount = htmlParagraphCount;
        return this;
    }

    public Integer getHtmlDivCount() {
        return htmlDivCount;
    }

    public PageStats setHtmlDivCount(Integer htmlDivCount) {
        this.htmlDivCount = htmlDivCount;
        return this;
    }

    public Integer getInternalLinkCount() {
        return internalLinkCount;
    }

    public PageStats setInternalLinkCount(Integer internalLinkCount) {
        this.internalLinkCount = internalLinkCount;
        return this;
    }

    public Integer getExternalLinkCount() {
        return externalLinkCount;
    }

    public PageStats setExternalLinkCount(Integer externalLinkCount) {
        this.externalLinkCount = externalLinkCount;
        return this;
    }

    public String getListOfLinks() {
        return listOfLinks;
    }

    public PageStats setListOfLinks(String listOfLinks) {
        this.listOfLinks = listOfLinks;
        return this;
    }

    public Instant getVisitedDate() {
        return visitedDate;
    }

    public PageStats setVisitedDate(Instant visitedDate) {
        this.visitedDate = visitedDate;
        return this;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public PageStats setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageStats)) return false;
        PageStats pageStats = (PageStats) o;
        return Objects.equals(id, pageStats.id) &&
                Objects.equals(domain, pageStats.domain) &&
                Objects.equals(link, pageStats.link) &&
                Objects.equals(url, pageStats.url) &&
                Objects.equals(title, pageStats.title) &&
                Objects.equals(languageTag, pageStats.languageTag) &&
                Objects.equals(lineCount, pageStats.lineCount) &&
                Objects.equals(numberCount, pageStats.numberCount) &&
                Objects.equals(letterCount, pageStats.letterCount) &&
                Objects.equals(wordCount, pageStats.wordCount) &&
                Objects.equals(sentenceCount, pageStats.sentenceCount) &&
                Objects.equals(htmlHeadingCount, pageStats.htmlHeadingCount) &&
                Objects.equals(htmlParagraphCount, pageStats.htmlParagraphCount) &&
                Objects.equals(htmlDivCount, pageStats.htmlDivCount) &&
                Objects.equals(internalLinkCount, pageStats.internalLinkCount) &&
                Objects.equals(externalLinkCount, pageStats.externalLinkCount) &&
                Objects.equals(listOfLinks, pageStats.listOfLinks) &&
                Objects.equals(visitedDate, pageStats.visitedDate) &&
                Objects.equals(createdDate, pageStats.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, domain, link, url, title, languageTag, lineCount, numberCount, letterCount, wordCount, sentenceCount, htmlHeadingCount, htmlParagraphCount, htmlDivCount, internalLinkCount, externalLinkCount, listOfLinks, visitedDate, createdDate);
    }

    @Override
    public String toString() {
        return "Page{" +
                "id=" + id +
                ", domain=" + domain +
                ", link='" + link + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", languageTag='" + languageTag + '\'' +
                ", lineCount=" + lineCount +
                ", numberCount=" + numberCount +
                ", letterCount=" + letterCount +
                ", wordCount=" + wordCount +
                ", sentenceCount=" + sentenceCount +
                ", htmlHeadingCount=" + htmlHeadingCount +
                ", htmlParagraphCount=" + htmlParagraphCount +
                ", htmlDivCount=" + htmlDivCount +
                ", internalLinkCount=" + internalLinkCount +
                ", externalLinkCount=" + externalLinkCount +
                ", listOfLinks='" + listOfLinks + '\'' +
                ", visitedDate=" + visitedDate +
                ", createdDate=" + createdDate +
                '}';
    }
}

