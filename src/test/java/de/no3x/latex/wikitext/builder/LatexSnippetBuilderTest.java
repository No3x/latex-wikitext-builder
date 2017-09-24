package de.no3x.latex.wikitext.builder;

import org.eclipse.mylyn.wikitext.confluence.core.ConfluenceLanguage;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static de.no3x.latex.wikitext.builder.hasNumberOfOccurrences.hasNumberOfOccurrences;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.isEmptyString;
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
        assertThat(latex, is("\\textbf{strong}\n\\\\"));
    }

    @Test
    public void testEmphasis() {
        String markupContent = "_emphasis_";
        String latex = convertToLatex(markupContent);
        assertThat(latex, is("\\textit{emphasis}\n\\\\"));
    }

    @Test
    public void testEscaping() {
        String markupContent = "Alice & Bob";
        String latex = convertToLatex(markupContent);
        assertThat(latex, is("Alice \\& Bob\\\\"));
    }

    @Test
    public void testEscapingBackslash() {
        String markupContent = "C:\\Dev Tools\\test.txt";
        String latex = convertToLatex(markupContent);
        assertThat(latex, is("C:\\textbackslash Dev Tools\\textbackslash test.txt\\\\"));
    }

    @Test
    public void testRNLinebreak() {
        String markupContent = "Hello\r\nWorld";
        String latex = convertToLatex(markupContent);
        assertThat(latex, containsString("Hello\\\\World"));
    }

    @Test
    public void testNLinebreak() {
        String markupContent = "Hello\nWorld";
        String latex = convertToLatex(markupContent);
        assertThat(latex, containsString("Hello\\\\World"));
    }

    @Test
    public void testRealCode() {
        String markupContent = "*Code Review Guidelines*\n" + "\n" + "The following aspects need to be focused, when performing the code review.\n" + "\n" + "* Correct and useful comments for all files, operations, defines, types and attributes.\n" + "** For public symbols the comments are placed inside header files. \n" + "** For private/static operations, which are not declared inside the header file, comments are placed inside source files. \n" + "** Public operation descriptions need to document the arguments (except the me pointer).\n" + "** Where possible, attributes and operations must include references to the system specifications they satisfy.\n" + "** Comments must not contain C-code (which have just been commented out instead of being removed).\n" + "* Repeated-include protection\n" + "* Source code must be formatted and indented to make the code well readable.\n" + "* Implementation blocks must be comprehensible.\n" + "* Possible errors that can occur during runtime have to be handled adequately (usually by calling the error handler with an appropriate error code).";
        String latex = convertToLatex(markupContent);
        assertThat(latex, is(not(isEmptyString())));
    }

    @Test
    public void testHashtagReplacement() {
        String markupContent = "\\item{ Comment with a # in the sentence. }";
        String latex = convertToLatex(markupContent);
        assertThat(latex, containsString("\\#"));
    }

}