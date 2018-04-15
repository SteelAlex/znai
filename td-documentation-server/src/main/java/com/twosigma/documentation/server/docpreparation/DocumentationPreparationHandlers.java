package com.twosigma.documentation.server.docpreparation;

import com.twosigma.utils.ServiceLoaderUtils;

import java.util.Set;

public class DocumentationPreparationHandlers {
    private static Set<DocumentationPreparationHandler> handlers =
            ServiceLoaderUtils.load(DocumentationPreparationHandler.class);

    public static void prepare(String docId, DocumentationPreparationProgress preparationProgress) {
        DocumentationPreparationHandler handler = handlers.stream()
                .filter(h -> h.handles(docId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "can't find handler for documentation preparation of documentation: " + docId));

        handler.prepare(docId, preparationProgress);
    }

    public static void add(DocumentationPreparationHandler handler) {
        handlers.add(handler);
    }
}
