package com.twosigma.documentation.parser;

import java.nio.file.Path;
import java.util.Collections;

import com.twosigma.documentation.core.ComponentsRegistry;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;

import com.twosigma.documentation.parser.commonmark.CommonMarkExtension;
import com.twosigma.documentation.parser.commonmark.include.IncludeNode;
import com.twosigma.documentation.parser.docelement.DocElementCreationParserHandler;

/**
 * @author mykola
 */
public class MarkdownParser implements MarkupParser {
    private final Parser parser;
    private final ComponentsRegistry componentsRegistry;

    // TODO need to react on external resources so they can be deployed. like images
    public MarkdownParser(ComponentsRegistry componentsRegistry) {
        this.componentsRegistry = componentsRegistry;
        CommonMarkExtension extension = new CommonMarkExtension();
        parser = Parser.builder().extensions(Collections.singletonList(extension)).build();
    }

    public MarkupParserResult parse(Path path, String markdown) {
        Node node = parser.parse(markdown);

        final DocElementCreationParserHandler parserHandler = new DocElementCreationParserHandler(componentsRegistry, path);
        final DocElementVisitor visitor = new DocElementVisitor(parserHandler);
        node.accept(visitor);

        if (visitor.sectionStarted) {
            parserHandler.onSectionEnd();
        }

        parserHandler.onParsingEnd();

        return new MarkupParserResult(parserHandler.getDocElement(), parserHandler.getAuxiliaryFiles());
    }

    private static class DocElementVisitor extends AbstractVisitor {
        private DocElementCreationParserHandler parserHandler;
        private boolean sectionStarted;

        public DocElementVisitor(DocElementCreationParserHandler parserHandler) {
            this.parserHandler = parserHandler;
        }

        @Override
        public void visit(final Paragraph paragraph) {
            parserHandler.onParagraphStart();
            visitChildren(paragraph);
            parserHandler.onParagraphEnd();
        }

        @Override
        public void visit(final Emphasis emphasis) {
            parserHandler.onEmphasisStart();
            visitChildren(emphasis);
            parserHandler.onEmphasisEnd();
        }

        @Override
        public void visit(final StrongEmphasis strongEmphasis) {
            parserHandler.onStrongEmphasisStart();
            visitChildren(strongEmphasis);
            parserHandler.onStrongEmphasisEnd();
        }

        @Override
        public void visit(final Text text) {
            parserHandler.onSimpleText(text.getLiteral());
        }

        @Override
        public void visit(BulletList bulletList) {
            parserHandler.onBulletListStart(bulletList.getBulletMarker(), bulletList.isTight());
            visitChildren(bulletList);
            parserHandler.onBulletListEnd();
        }

        @Override
        public void visit(OrderedList orderedList) {
            parserHandler.onOrderedListStart(orderedList.getDelimiter(), orderedList.getStartNumber());
            visitChildren(orderedList);
            parserHandler.onOrderedListEnd();
        }

        @Override
        public void visit(ListItem listItem) {
            parserHandler.onListItemStart();
            visitChildren(listItem);
            parserHandler.onListItemEnd();
        }

        @Override
        public void visit(Code code) {
            parserHandler.onInlinedCode(code.getLiteral());
        }

        @Override
        public void visit(ThematicBreak thematicBreak) {
            parserHandler.onThematicBreak();
        }

        @Override
        public void visit(HardLineBreak hardLineBreak) {
            parserHandler.onHardLineBreak();
        }

        @Override
        public void visit(SoftLineBreak softLineBreak) {
            parserHandler.onSoftLineBreak();
        }

        @Override
        public void visit(BlockQuote blockQuote) {
            parserHandler.onBlockQuoteStart();
            visitChildren(blockQuote);
            parserHandler.onBlockQuoteEnd();
        }

        @Override
        public void visit(CustomBlock customBlock) {
            if (customBlock instanceof IncludeNode) {
                final IncludeNode includeNode = (IncludeNode) customBlock;
                parserHandler.onInclude(includeNode.getId(), includeNode.getValue());
            } else {
                super.visit(customBlock);
            }
        }

        @Override
        public void visit(Image image) {
            Node firstChild = image.getFirstChild();
            parserHandler.onImage(image.getTitle(), image.getDestination(), ((Text) firstChild).getLiteral());
        }

        @Override
        public void visit(final IndentedCodeBlock indentedCodeBlock) {
            parserHandler.onSnippet("", "", indentedCodeBlock.getLiteral());
        }

        @Override
        public void visit(final FencedCodeBlock fencedCodeBlock) {
            parserHandler.onSnippet(fencedCodeBlock.getInfo(), "", fencedCodeBlock.getLiteral());
        }

        @Override
        public void visit(final Link link) {
            parserHandler.onLinkStart(link.getDestination());
            visitChildren(link);
            parserHandler.onLinkEnd();
        }

        @Override
        public void visit(final Heading heading) {
            if (heading.getLevel() == 1) {
                if (sectionStarted) {
                    parserHandler.onSectionEnd();
                }

                final String literal = ((Text) heading.getFirstChild()).getLiteral();
                parserHandler.onSectionStart(literal);
                sectionStarted = true;
            } else {
                super.visit(heading);
            }
        }
    }
}
