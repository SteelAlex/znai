package com.twosigma.documentation.structure;

import java.nio.file.Path;

/**
 * @author mykola
 */
public interface DocStructure {
    void validateUrl(Path path, String sectionWithLinkTitle, DocUrl docUrl);
    String createUrl(DocUrl docUrl);
    String prefixUrlWithProductId(String url);
}