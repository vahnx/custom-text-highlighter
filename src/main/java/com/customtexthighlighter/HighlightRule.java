package com.customtexthighlighter;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.runelite.client.util.ColorUtil;

final class HighlightRule
{
	private final String text;
	private final Pattern pattern;
	private final Color colour;

	private HighlightRule(String text, Pattern pattern, Color colour)
	{
		this.text = text;
		this.pattern = pattern;
		this.colour = colour;
	}

	static HighlightRule parse(String ruleText)
	{
		String rule = ruleText.trim();
		String content = rule.substring(1, rule.length() - 1);
		int commaIndex = content.lastIndexOf(',');
		String text = content.substring(0, commaIndex).trim();
		Color colour = ColorParser.parse(content.substring(commaIndex + 1).trim());
		return new HighlightRule(text, Pattern.compile(buildPattern(text),
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS), colour);
	}

	private static String buildPattern(String text)
	{
		StringBuilder regex = new StringBuilder();
		StringBuilder literal = new StringBuilder();
		for (char character : text.toCharArray())
		{
			switch (character)
			{
				case '*': flushLiteral(regex, literal); regex.append("[\\p{L}\\p{N}_']*"); break;
				case '?': flushLiteral(regex, literal); regex.append('.'); break;
				case '|': flushLiteral(regex, literal); regex.append('|'); break;
				default: literal.append(character);
			}
		}
		flushLiteral(regex, literal);
		return regex.toString();
	}

	private static void flushLiteral(StringBuilder regex, StringBuilder literal)
	{
		if (literal.length() > 0)
		{
			regex.append(Pattern.quote(literal.toString()));
			literal.setLength(0);
		}
	}

	String getText()
	{
		return text;
	}

	String apply(String message)
	{
		Matcher matcher = pattern.matcher(message);
		if (!matcher.find())
		{
			return null;
		}

		matcher.reset();
		StringBuffer result = new StringBuffer();
		while (matcher.find())
		{
			matcher.appendReplacement(result,
				Matcher.quoteReplacement(ColorUtil.wrapWithColorTag(matcher.group(), colour) + "<colNORMAL>"));
		}
		matcher.appendTail(result);
		return result.toString();
	}
}
