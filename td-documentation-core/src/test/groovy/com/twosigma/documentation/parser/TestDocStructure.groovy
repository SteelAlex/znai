package com.twosigma.documentation.parser

import com.twosigma.documentation.validation.DocStructure

import java.nio.file.Path

/**
 * @author mykola
 */
class TestDocStructure implements DocStructure {
    private Set<String> validLinks = [] as Set

    @Override
    void validateLink(Path path, String sectionWithLinkTitle, String dirName, String fileName, String pageSectionId) {
        def urlBase = "${dirName}/${fileName}"
        def url = urlBase + (pageSectionId.isEmpty() ? "" : "#${pageSectionId}")

        if (! validLinks.contains(url.toString())) {
            throw new IllegalArgumentException("no valid link found in section '${sectionWithLinkTitle}': " + url)
        }
    }

    @Override
    String createLink(String dirName, String fileName, String pageSectionId) {
        def base = "/test-doc/${dirName}/${fileName}"
        return base + (pageSectionId ? "#${pageSectionId}" : "")
    }

    void addValidLink(String link) {
        validLinks.add(link)
    }
}
