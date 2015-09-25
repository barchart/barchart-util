package com.barchart.util.common.textgrid;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextGridLayout {

	private static final Logger logger = LoggerFactory.getLogger(TextGridLayout.class);

	private static final char NEW_LINE = '\n';

	private final List<ColumnDefinition> columns;

	private final String header;

	public final String horizontalRule;

	private final String dataFormatString;

	private TextGridLayout(Builder builder) {
		this.columns = builder.columns;
		this.header = buildHeader();
		this.horizontalRule = buildHorizontalRule();
		this.dataFormatString = buildDataFormatString();
	}

	public ColumnDefinition getColumn(int i) {
		return columns.get(i);
	}

	public int getColumnCount() {
		return columns.size();
	}

	private String buildHorizontalRule() {
		return padStart("", header.length(), '-');
	}

	private String buildHeader() {
		List<String> headers = new ArrayList<String>();
		for (ColumnDefinition column : columns) {
			headers.add(column.getHeader());
		}
		String headerFormatString = buildHeaderFormatString();
		return String.format(headerFormatString, headers.toArray());
	}

	private String buildHeaderFormatString() {
		StringBuilder builder = new StringBuilder();
		for (ColumnDefinition column : columns) {
			builder.append("%").append(column.getWidth()).append("s ");
		}
		return builder.toString();
	}

	private String buildDataFormatString() {
		StringBuilder builder = new StringBuilder();
		for (ColumnDefinition column : columns) {
			builder.append("%").append(column.getWidth()).append("s ");
		}
		return builder.toString();
	}

	public static Builder builder() {
		return new Builder();
	}

	public TextGrid newGrid() {
		return new GridImpl(this);
	}

	public static class Builder {

		private final List<ColumnDefinition> columns;

		private Builder() {
			this.columns = new ArrayList<ColumnDefinition>();
		}

		public Builder addColumn(String header, int width) {
			return addColumn(header, width, "");
		}
		
		public Builder addColumn(String header, int width, String formatString) {
			columns.add(new ColumnDefinition(header, width, formatString));
			return this;
		}

		public TextGridLayout build() {
			return new TextGridLayout(this);
		}

	}

	private static final class ColumnDefinition {

		private final String header;
		
		private final int width;
		
		private final String formatString;

		public ColumnDefinition(String header, int width, String formatString) {
			this.header = header;
			this.width = width;
			this.formatString = formatString;
		}

		public String getHeader() {
			return header;
		}

		public int getWidth() {
			return width;
		}

		public String getFormatString() {
			return formatString;
		}

	}

	private static final class GridImpl implements TextGrid {

		private final TextGridLayout textGridLayout;

		private final ArrayList<Object[]> rows;

		public GridImpl(TextGridLayout textGridLayout) {
			this.textGridLayout = textGridLayout;
			this.rows = new ArrayList<Object[]>();
		}

		@Override
		public TextGrid addRowData(Object... values) {
			rows.add(values);
			return this;
		}

		@Override
		public TextGrid addRowData(Iterable<Object> values) {
			ArrayList<Object> list = new ArrayList<Object>();
			for (Object o : values) {
				list.add(o);
			}
			addRowData(list.toArray());
			return this;
		}

		@Override
		public String toString() {
			return render();
		}

		private String render() {
			StringBuilder builder = new StringBuilder();
			builder.append(textGridLayout.header);
			builder.append(NEW_LINE);
			builder.append(textGridLayout.horizontalRule);
			builder.append(NEW_LINE);
			for (Object[] row : rows) {
				Object[] formattedValues = formatValues(row);
				String rowString = String.format(textGridLayout.dataFormatString, formattedValues);
				builder.append(rowString);
				builder.append(NEW_LINE);
			}
			return builder.toString();
		}

		private String[] formatValues(Object[] row) {
			String[] values = new String[textGridLayout.columns.size()];
			for (int i = 0; i < row.length; i++) {
				ColumnDefinition column = textGridLayout.getColumn(i);
				
				String stringValue = valueToString(row[i], column.getFormatString());
				
				String str = truncate(stringValue, column.getWidth());
				values[i] = str;
				

			}
			for (int i = row.length; i < textGridLayout.getColumnCount(); i++) {
				values[i] = "";
			}

			return values;
		}

		private String valueToString(Object object, String formatString) {
			if (formatString.isEmpty()) {
				return object.toString();				
			} else {
				return String.format(formatString, object);
			}
		}

		private String truncate(String string, int width) {
			if (string.length() > width) {
				return string.substring(0, width);
			} else {
				return string;
			}
		}

	}

	private static String padStart(String string, int minLength, char padChar) {
		if (string.length() >= minLength) {
			return string;
		}
		StringBuilder builder = new StringBuilder(minLength);
		for (int i = string.length(); i < minLength; i++) {
			builder.append(padChar);
		}
		builder.append(string);
		return builder.toString();
	}
}
