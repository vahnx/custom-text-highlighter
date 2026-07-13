package com.customtexthighlighter;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup(CustomTextHighlighterConfig.GROUP)
public interface CustomTextHighlighterConfig extends Config
{
	String GROUP = "customtexthighlighter";

	@ConfigSection(
		name = "Chat channels",
		description = "Choose which chat channels can be highlighted",
		position = 3
	)
	String chatChannelsSection = "chatChannels";

	@ConfigItem(
		keyName = "rules",
		name = "Highlight rules",
		description = "Enter one rule per line in the format [text,colour]. Wildcards: * completes the current word, ? matches one character, | matches either option.",
		position = 0
	)
	default String rules()
	{
		return "[Example,#00FF00]";
	}

	@ConfigItem(
		keyName = "previewColour",
		name = "Preview colour",
		description = "Choose the colour used by the preview message",
		position = 1
	)
	default Color previewColour()
	{
		return Color.GREEN;
	}

	@ConfigItem(
		keyName = "sendPreview",
		name = "Send preview message",
		description = "Toggle this setting in either direction to send a preview using the first rule.",
		position = 2
	)
	default boolean sendPreview()
	{
		return false;
	}

	@ConfigItem(
		keyName = "highlightGameMessages",
		name = "Game messages",
		description = "Highlight supported game and system messages",
		section = chatChannelsSection,
		position = 4
	)
	default boolean highlightGameMessages()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightPublicChat",
		name = "Public chat",
		description = "Highlight public chat messages",
		section = chatChannelsSection,
		position = 5
	)
	default boolean highlightPublicChat()
	{
		return false;
	}

	@ConfigItem(
		keyName = "highlightPrivateMessages",
		name = "Private messages",
		description = "Highlight incoming and outgoing private messages",
		section = chatChannelsSection,
		position = 6
	)
	default boolean highlightPrivateMessages()
	{
		return false;
	}

	@ConfigItem(
		keyName = "highlightClanChat",
		name = "Clan chat",
		description = "Highlight clan and group iron chat messages",
		section = chatChannelsSection,
		position = 7
	)
	default boolean highlightClanChat()
	{
		return false;
	}

	@ConfigItem(
		keyName = "highlightFriendsChat",
		name = "Friends chat",
		description = "Highlight friends chat messages",
		section = chatChannelsSection,
		position = 8
	)
	default boolean highlightFriendsChat()
	{
		return false;
	}

	@ConfigItem(
		keyName = "highlightGuestClanChat",
		name = "Guest clan chat",
		description = "Highlight guest clan chat messages",
		section = chatChannelsSection,
		position = 9
	)
	default boolean highlightGuestClanChat()
	{
		return false;
	}

	@ConfigItem(
		keyName = "highlightOtherChat",
		name = "Other chat",
		description = "Highlight supported player chat types not covered above, such as autotyper messages",
		section = chatChannelsSection,
		position = 10
	)
	default boolean highlightOtherChat()
	{
		return false;
	}
}
