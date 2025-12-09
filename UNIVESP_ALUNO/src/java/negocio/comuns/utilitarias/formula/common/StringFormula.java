package negocio.comuns.utilitarias.formula.common;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

public final class StringFormula implements Appendable, CharSequence, Serializable {

	private static final long serialVersionUID = 1L;

	private StringBuilder builder;

	private String prefix;
	private String delimiter;
	private String suffix;
	private boolean appendStart;
	private CharSequence value;

	public StringFormula() {
		this("", "", "", "", false);
	}
	
	public StringFormula(CharSequence value) {
		this(value, "", "", "", false);
	}

	public StringFormula(CharSequence value, CharSequence delimiter) {
		this(value, delimiter, "", "", false);
	}

	public StringFormula(CharSequence value, CharSequence delimiter, CharSequence prefix, CharSequence suffix, boolean appendStart) {

		Objects.requireNonNull(value, "The value must not be null");
		Objects.requireNonNull(delimiter, "The delimiter must not be null");
		Objects.requireNonNull(prefix, "The prefix must not be null");
		Objects.requireNonNull(suffix, "The suffix must not be null");

		this.prefix = prefix.toString();
		this.delimiter = delimiter.toString();
		this.suffix = suffix.toString();
		this.appendStart = appendStart;
		this.value = value;

		this.builder = new StringBuilder();
	}

	private StringFormula(CharSequence value, StringBuilder builder, CharSequence delimiter, CharSequence prefix,
			CharSequence suffix, boolean appendStart) {

		this(value, delimiter, prefix, suffix, appendStart);

		this.builder = builder;
	}

	@Override
	public int length() {
		return builder.length();
	}

	@Override
	public char charAt(int index) {
		return builder.charAt(index);
	}

	@Override
	public StringFormula subSequence(int start, int end) {
		builder.subSequence(start, end);
		return this;
	}

	@Override
	public StringFormula append(CharSequence csq) {
		builder.append(csq + delimiter);
		return this;
	}

	@Override
	public StringFormula append(CharSequence csq, int start, int end) {
		builder.append(csq + delimiter, start, end);
		return this;
	}

	@Override
	public StringFormula append(char c) {
		builder.append(c + delimiter);
		return this;
	}

	@Override
	public String toString() {
		if (this.appendStart) {
			return this.builder.insert(0, this.prefix).append(this.value + this.delimiter).append(suffix).toString();
		}
		return this.builder.insert(0, this.value + this.delimiter).insert(0, this.prefix).append(suffix).toString();
	}

	public StringFormula replaceAll(String regex, String replacement) {
		Pattern pattern = Pattern.compile(regex);
		this.builder = new StringBuilder(pattern.matcher(this.builder).replaceAll(replacement));
		this.value = pattern.matcher(this.value).replaceAll(replacement);
		return this.copyStringFormula();
	}

	private StringFormula copyStringFormula() {
		return new StringFormula(this.value, this.builder, this.delimiter, this.prefix, this.suffix, this.appendStart);
	}

	public static StringFormula valueOf(CharSequence value, CharSequence delimiter, CharSequence prefix,
			CharSequence suffix, boolean appendStart) {
		return new StringFormula(value, delimiter, prefix, suffix, appendStart);
	}

}
