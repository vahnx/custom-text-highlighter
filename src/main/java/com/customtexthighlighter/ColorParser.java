package com.customtexthighlighter;

import java.awt.Color;
import java.util.Locale;

final class ColorParser
{
	private ColorParser()
	{
	}

	static Color parse(String value)
	{
		String colour = value.trim().toLowerCase(Locale.ROOT);
		switch (colour)
		{
			case "red": return Color.RED;
			case "green": return Color.GREEN;
			case "blue": return Color.BLUE;
			case "yellow": return Color.YELLOW;
			case "cyan": return Color.CYAN;
			case "orange": return Color.ORANGE;
			case "pink": return Color.PINK;
			case "purple": return new Color(128, 0, 128);
			case "white": return Color.WHITE;
			case "grey":
			case "gray": return Color.GRAY;
			case "black": return Color.BLACK;
			default: return parseHex(colour);
		}
	}

	private static Color parseHex(String value)
	{
		String hex = value.startsWith("#") ? value.substring(1) : value;
		if (!hex.matches("[0-9a-fA-F]{6}"))
		{
			throw new IllegalArgumentException("Unknown colour. Use a supported colour name or six-digit hex value.");
		}
		return new Color(Integer.parseInt(hex, 16));
	}
}
