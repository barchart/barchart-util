package com.barchart.util.common.textgrid;


public interface TextGrid {

	public TextGrid addRowData(Object...values);
	
	public TextGrid addRowData(Iterable<Object> values);

}
