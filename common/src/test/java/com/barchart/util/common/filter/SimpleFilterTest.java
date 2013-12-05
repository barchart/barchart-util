package com.barchart.util.common.filter;

import java.util.List;

import junit.framework.TestCase;

public class SimpleFilterTest extends TestCase {

	public void testSubstringMatching() {
        List<String> pieces;

        pieces = SimpleFilter.parseSubstring("*");
        assertTrue("Should match!", SimpleFilter.compareSubstring(pieces, ""));

        pieces = SimpleFilter.parseSubstring("foo");
        assertFalse("Should not match!",
                        SimpleFilter.compareSubstring(pieces, ""));

        pieces = SimpleFilter.parseSubstring("");
        assertTrue("Should match!", SimpleFilter.compareSubstring(pieces, ""));
        assertFalse("Should not match!",
                        SimpleFilter.compareSubstring(pieces, "foo"));

        pieces = SimpleFilter.parseSubstring("foo");
        assertTrue("Should match!",
                        SimpleFilter.compareSubstring(pieces, "foo"));
        assertFalse("Should not match!",
                        SimpleFilter.compareSubstring(pieces, "barfoo"));
        assertFalse("Should not match!",
                        SimpleFilter.compareSubstring(pieces, "foobar"));

        pieces = SimpleFilter.parseSubstring("foo*");
        assertTrue("Should match!",
                        SimpleFilter.compareSubstring(pieces, "foo"));
        assertFalse("Should not match!",
                        SimpleFilter.compareSubstring(pieces, "barfoo"));
        assertTrue("Should match!",
                        SimpleFilter.compareSubstring(pieces, "foobar"));

        pieces = SimpleFilter.parseSubstring("*foo");
        assertTrue("Should match!",
                        SimpleFilter.compareSubstring(pieces, "foo"));
        assertTrue("Should match!",
                        SimpleFilter.compareSubstring(pieces, "barfoo"));
        assertFalse("Should match!",
                        SimpleFilter.compareSubstring(pieces, "foobar"));

        pieces = SimpleFilter.parseSubstring("foo*bar");
        assertTrue("Should match!",
                        SimpleFilter.compareSubstring(pieces, "foobar"));
        assertFalse("Should not match!",
                        SimpleFilter.compareSubstring(pieces, "barfoo"));
        assertTrue("Should match!",
                        SimpleFilter.compareSubstring(pieces, "foosldfjbar"));

        pieces = SimpleFilter.parseSubstring("*foo*bar");
        assertTrue("Should match!",
                        SimpleFilter.compareSubstring(pieces, "foobar"));
        assertFalse("Should not match!",
                        SimpleFilter.compareSubstring(pieces, "foobarfoo"));
        assertTrue("Should match!",
                        SimpleFilter.compareSubstring(pieces, "barfoobar"));
        assertTrue("Should match!",
                        SimpleFilter.compareSubstring(pieces, "sdffoobsdfbar"));

        pieces = SimpleFilter.parseSubstring("*foo*bar*");
        assertTrue("Should match!",
                        SimpleFilter.compareSubstring(pieces, "foobar"));
        assertTrue("Should match!",
                        SimpleFilter.compareSubstring(pieces, "foobarfoo"));
        assertTrue("Should match!",
                        SimpleFilter.compareSubstring(pieces, "barfoobar"));
        assertTrue("Should match!",
                        SimpleFilter.compareSubstring(pieces, "sdffoobsdfbar"));
        assertTrue("Should match!",
                        SimpleFilter.compareSubstring(pieces, "sdffoobsdfbarlj"));
        assertFalse("Should not match!",
                        SimpleFilter.compareSubstring(pieces, "sdffobsdfbarlj"));

        pieces = SimpleFilter.parseSubstring("*foo(*bar*");
        assertTrue("Should match!",
                        SimpleFilter.compareSubstring(pieces, "foo()bar"));

        pieces = SimpleFilter.parseSubstring("*foo*bar*bar");
        assertFalse("Should not match!",
                        SimpleFilter.compareSubstring(pieces, "foobar"));

        pieces = SimpleFilter.parseSubstring("aaaa*aaaa");
        assertFalse("Should not match!",
                        SimpleFilter.compareSubstring(pieces, "aaaaaaa"));

        pieces = SimpleFilter.parseSubstring("aaa**aaa");
        assertTrue("Should match!",
                        SimpleFilter.compareSubstring(pieces, "aaaaaa"));
	}
	
}
