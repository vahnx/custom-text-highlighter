package com.customtexthighlighter;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CustomTextHighlighterPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(CustomTextHighlighterPlugin.class);
		RuneLite.main(args);
	}
}
