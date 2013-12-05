package com.barchart.util.common.filter;

import java.util.List;

import org.junit.Test;

public class TestSimpleFilter {

	@Test
    public void testOne() throws Exception {

            System.out.println("---------------");

            final String text = "(market-id=123)";

            final SimpleFilter filter1 = SimpleFilter.parse(text);
            System.out.println("filter1=" + filter1);
            System.out.println("operation1=" + filter1.getOperation());
            System.out.println("name1=" + filter1.getName());
            System.out.println("value=" + filter1.getValue());

    }

    @Test
    public void testTwo() throws Exception {

            System.out.println("---------------");

            final String text = "(&(market-id=123)(exchange-id=cme))";

            final SimpleFilter filter1 = SimpleFilter.parse(text);
            System.out.println("filter1=" + filter1);
            System.out.println("operation1=" + filter1.getOperation());
            System.out.println("name1=" + filter1.getName());
            System.out.println("value=" + filter1.getValue());

            System.out.println("---------------");

            @SuppressWarnings("unchecked")
            final List<SimpleFilter> list = (List<SimpleFilter>) filter1.getValue();

            final SimpleFilter filter2 = list.get(0);
            System.out.println("filter2=" + filter2);
            System.out.println("operation2=" + filter2.getOperation());
            System.out.println("name2=" + filter2.getName());
            System.out.println("value2=" + filter2.getValue());

            System.out.println("---------------");

            final SimpleFilter filter3 = list.get(1);
            System.out.println("filter3=" + filter3);
            System.out.println("operation3=" + filter3.getOperation());
            System.out.println("name3=" + filter3.getName());
            System.out.println("value3=" + filter3.getValue());

    }
	
}
