package com.twosigma.documentation.html;

import com.twosigma.documentation.structure.DocMeta;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author mykola
 */
public class DocumentationReactProps {
    private final DocMeta docMeta;
    private final PageReactProps pageProps;
    private FooterProps footerProps;

    public DocumentationReactProps(DocMeta docMeta, PageReactProps pageProps, FooterProps footerProps) {
        this.docMeta = docMeta;
        this.pageProps = pageProps;
        this.footerProps = footerProps;
    }

    public Map<String, ?> toMap() {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put("docMeta", docMeta.toMap());
        map.put("page", pageProps.toMap());
        map.put("footer", footerProps.toMap());

        return map;
    }
}