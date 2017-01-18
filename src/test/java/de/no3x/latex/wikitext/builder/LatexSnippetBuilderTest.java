package de.no3x.latex.wikitext.builder;

import org.eclipse.mylyn.wikitext.confluence.core.ConfluenceLanguage;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static de.no3x.latex.wikitext.builder.hasNumberOfOccurrences.hasNumberOfOccurrences;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by czoeller on 18.01.17.
 */
public class LatexSnippetBuilderTest {

    private MarkupParser parser;
    private LatexSnippetBuilder latexSnippetBuilder;

    private String convertToLatex(String markup) {
        parser.parse(markup);
        latexSnippetBuilder.getWriter().toString();
        return latexSnippetBuilder.getWriter().toString();
    }

    @Before
    public void setUp() throws Exception {
        final StringWriter writer = new StringWriter();
        parser = new MarkupParser(new ConfluenceLanguage());
        latexSnippetBuilder = new LatexSnippetBuilder(writer);
        parser.setBuilder(latexSnippetBuilder);
    }

    @Test
    public void testLists() {

        String markupContent =
                "# Here's a sentence.\n" +
                "## This is a sub-list point.\n" +
                "### Third list level.\n" +
                "### Another point at the third level.\n" +
                "## And a second sub-list point.\n" +
                "# Here's another sentence.";
        String latex = convertToLatex(markupContent);
        assertThat(latex, hasNumberOfOccurrences(3, "begin"));
        assertThat(latex, hasNumberOfOccurrences(3, "end"));
        assertThat(latex, hasNumberOfOccurrences(6, "item"));
    }

    @Test
    public void testHeadings() {

        String markupContent =
                "h1. Biggest & Bla heading\n" +
                "h5. Small heading";
        String latex = convertToLatex(markupContent);
        assertThat(latex, hasNumberOfOccurrences(2, "\\section{"));
    }

    @Test
    public void testStrongAndBold() {
        String markupContent = "*strong*";
        String latex = convertToLatex(markupContent);
        assertThat(latex, is("\\textbf{strong}"));
    }

    @Test
    public void testEmphasis() {
        String markupContent = "_emphasis_";
        String latex = convertToLatex(markupContent);
        assertThat(latex, is("\\textit{emphasis}"));
    }

    @Test
    public void testEscaping() {
        String markupContent = "Alice & Bob";
        String latex = convertToLatex(markupContent);
        assertThat(latex, is("Alice \\& Bob"));
    }

    @Test
    public void testEscapingBackslash() {
        String markupContent = "C:\\Dev Tools\\test.txt";
        String latex = convertToLatex(markupContent);
        assertThat(latex, is("C:\\textbackslash Dev Tools\\textbackslash test.txt"));
    }

}