# latex-wikitext-builder
A Mylyn WikiText Document Builder to convert wiki to latex code.
# Usage
```java
// Setup
final StringWriter writer = new StringWriter();
final MarkupParser parser = new MarkupParser(new ConfluenceLanguage());
final LatexSnippetBuilder latexSnippetBuilder = new LatexSnippetBuilder(writer);
parser.setBuilder(latexSnippetBuilder);
// Use
final String markup = "*bold*";
parser.parse(markup);
latexSnippetBuilder.getWriter().toString();
String latex = latexSnippetBuilder.getWriter().toString(); // contains: \textbf{bold}


```

See tests for more details.
# Supported Elements
There are a few elements supported only:
- Lists
- Headings
- Bold/Italic font weight

See tests for more details.