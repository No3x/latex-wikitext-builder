/*
 * No3x
 * Copyright (C) 2017 No3x
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.no3x.latex.wikitext.builder;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.mylyn.wikitext.core.parser.Attributes;
import org.eclipse.mylyn.wikitext.core.parser.DocumentBuilder;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by czoeller on 17.01.17.
 */
public class LatexSnippetBuilder extends DocumentBuilder {

    private static final int END_OFFSET = BlockType.values().length;
    private static final int SPAN_OFFSET = BlockType.values().length + END_OFFSET;
    private static final Integer HEADING = 999;
    private static final Integer HEADING_END = 1000;
    private StringWriter writer;
    private Stack<BlockType> blockTypeHistory = new Stack<>();
    private Stack<SpanType> spanTypeHistory = new Stack<>();
    private HashMap<Integer, String> mapping = new HashMap<>();

    public LatexSnippetBuilder(StringWriter writer) {
        this.writer = writer;
        mapping.put(BlockType.LIST_ITEM.ordinal(), "\\item ");
        mapping.put(BlockType.LIST_ITEM.ordinal() + END_OFFSET, "\n");
        mapping.put(BlockType.BULLETED_LIST.ordinal(), "\\begin{itemize} ");
        mapping.put(BlockType.BULLETED_LIST.ordinal() + END_OFFSET, "\\end{itemize}\n");
        mapping.put(BlockType.NUMERIC_LIST.ordinal(), "\\begin{enumerate} ");
        mapping.put(BlockType.NUMERIC_LIST.ordinal() + END_OFFSET, "\\end{enumerate}\n");
        mapping.put(BlockType.PARAGRAPH.ordinal(), "");
        mapping.put(BlockType.PARAGRAPH.ordinal() + END_OFFSET, "\\\\");
        mapping.put(BlockType.CODE.ordinal(), "\\begin{lstlisting}[breaklines=true]\n");
        mapping.put(BlockType.CODE.ordinal() + END_OFFSET, "\\end{lstlisting}\n");


        mapping.put(SpanType.STRONG.ordinal() + SPAN_OFFSET, "\\textbf{");
        mapping.put(SpanType.STRONG.ordinal() + SPAN_OFFSET + END_OFFSET, "}\n");
        mapping.put(SpanType.EMPHASIS.ordinal() + SPAN_OFFSET, "\\textit{");
        mapping.put(SpanType.EMPHASIS.ordinal() + SPAN_OFFSET + END_OFFSET, "}\n");

        mapping.put(HEADING, "\\section{");
        mapping.put(HEADING_END, "}\n");
    }

    private String getMappingOrEmptyString(Object key) {
        String s = mapping.get(key);
        if (null == s) {
            // No Mapping for this type
            s = "";
        }
        return s;
    }

    @Override
    public void beginDocument() {

    }

    @Override
    public void endDocument() {

    }

    @Override
    public void beginBlock(BlockType blockType, Attributes attributes) {
        blockTypeHistory.push(blockType);
        writer.write(getMappingOrEmptyString(blockType.ordinal()));
    }

    @Override
    public void endBlock() {
        endBlock(blockTypeHistory.pop());
    }

    private void endBlock(BlockType blockType) {
        final String s = getMappingOrEmptyString(blockType.ordinal() + END_OFFSET);
        writer.write(s);
    }

    @Override
    public void beginSpan(SpanType spanType, Attributes attributes) {
        spanTypeHistory.push(spanType);
        writer.write(getMappingOrEmptyString(spanType.ordinal() + SPAN_OFFSET));
    }

    @Override
    public void endSpan() {
        endSpan(spanTypeHistory.pop());
    }

    private void endSpan(SpanType spanType) {
        final String s = getMappingOrEmptyString(spanType.ordinal() + SPAN_OFFSET + END_OFFSET);
        writer.write(s);
    }

    @Override
    public void beginHeading(int i, Attributes attributes) {
        writer.write(mapping.get(HEADING));
    }

    @Override
    public void endHeading() {
        writer.write(mapping.get(HEADING_END));
    }

    @Override
    public void characters(String s) {
        String escaped = s;
        escaped = StringUtils.replaceAll(escaped, "\\\\", "\\\\textbackslash ");
        String[] chars = {"&", "%", "$", "#", "_", "{", "}", "~", "^"};
        for (String c : chars) {
            escaped = StringUtils.replaceAll(escaped, "\\" + c, "\\\\" + c);
        }
        writer.write(escaped);
    }

    @Override
    public void entityReference(String s) {

    }

    @Override
    public void image(Attributes attributes, String s) {

    }

    @Override
    public void link(Attributes attributes, String s, String s1) {

    }

    @Override
    public void imageLink(Attributes attributes, Attributes attributes1, String s, String s1) {

    }

    @Override
    public void acronym(String s, String s1) {

    }

    @Override
    public void lineBreak() {
        writer.write("\\\\");
    }

    @Override
    public void charactersUnescaped(String s) {
        writer.write(s);
    }

    public StringWriter getWriter() {
        return writer;
    }
}
