/*
 * No3x
 * Copyright (C) 2017 No3x
 * This file is covered by the LICENSE file in the root of this project.
 */

package de.no3x.latex.wikitext.builder;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Matcher to test a string contains a given count of occurrences of a given needle.
 *
 */
public class hasNumberOfOccurrences extends TypeSafeMatcher<String> {

    private final String needle;
    private final int count;
    private int actualCount;

    public hasNumberOfOccurrences(int count, String needle) {
        this.needle = needle;
        this.count = count;
    }

    @Factory
    public static Matcher<String> hasNumberOfOccurrences(int count, String needle) {
        return new hasNumberOfOccurrences(count, needle);
    }

    @Override
    protected boolean matchesSafely(String item) {
        actualCount = StringUtils.countMatches(item, needle);
        return  actualCount == count;
    }

    @Override
    public void describeMismatchSafely(String item, Description mismatchDescription) {
        mismatchDescription.appendValue(actualCount)
                           .appendText(" occurrence(s) of ")
                           .appendValue(needle)
                           .appendText(" in ")
                           .appendText(item);
    }

    /**
     * Generates a description of the object.  The description may be part of a
     * a description of a larger object of which this is just a component, so it
     * should be worded appropriately.
     *
     * @param description The description to be built or appended to.
     */
    @Override
    public void describeTo(Description description) {
        description.appendText("String containing ")
                   .appendText(count + " occurrences of ")
                   .appendText(needle);
    }
}
