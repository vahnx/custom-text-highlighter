package com.customtexthighlighter;

import com.google.inject.Provides;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MessageNode;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
	name = "Custom Text Highlighter",
	description = "Highlights custom text in game messages",
	tags = {"chat", "text", "highlight", "colour", "color", "wildcard"}
)
public class CustomTextHighlighterPlugin extends Plugin
{
	private static final String PREVIEW_PREFIX = "Custom Text Highlighter preview: ";

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private CustomTextHighlighterConfig config;

	private List<HighlightRule> rules = Collections.emptyList();

	@Provides
	CustomTextHighlighterConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CustomTextHighlighterConfig.class);
	}

	@Override
	protected void startUp()
	{
		loadRules();
	}

	@Override
	protected void shutDown()
	{
		rules = Collections.emptyList();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!CustomTextHighlighterConfig.GROUP.equals(event.getGroup()))
		{
			return;
		}

		if ("rules".equals(event.getKey()))
		{
			loadRules();
			return;
		}

		if ("sendPreview".equals(event.getKey()))
		{
			sendPreviewMessage();
		}
	}

	/*
	 * Lower priority runs later, allowing this plugin to apply its formatting
	 * after most other chat recoloring plugins.
	 */
	@Subscribe(priority = -1000.0f)
	public void onChatMessage(ChatMessage event)
	{
		if (!isEnabledMessageType(event.getType()))
		{
			return;
		}

		MessageNode messageNode = event.getMessageNode();
		if (messageNode == null)
		{
			return;
		}

		String plainMessage = Text.removeTags(event.getMessage());
		if (plainMessage.startsWith(PREVIEW_PREFIX))
		{
			return;
		}

		String highlightedMessage = plainMessage;
		boolean changed = false;
		for (HighlightRule rule : rules)
		{
			String result = rule.apply(highlightedMessage);
			if (result != null)
			{
				highlightedMessage = result;
				changed = true;
			}
		}

		if (changed)
		{
			messageNode.setRuneLiteFormatMessage(highlightedMessage);
			client.refreshChat();
		}
	}

	private boolean isEnabledMessageType(ChatMessageType type)
	{
		switch (type)
		{
			case GAMEMESSAGE:
			case SPAM:
			case ENGINE:
			case MESBOX:
			case LEVELUPMESSAGE:
				return config.highlightGameMessages();
			case PUBLICCHAT:
			case MODCHAT:
				return config.highlightPublicChat();
			case PRIVATECHAT:
			case PRIVATECHATOUT:
			case MODPRIVATECHAT:
				return config.highlightPrivateMessages();
			case CLAN_CHAT:
			case CLAN_GIM_CHAT:
				return config.highlightClanChat();
			case FRIENDSCHAT:
				return config.highlightFriendsChat();
			case CLAN_GUEST_CHAT:
				return config.highlightGuestClanChat();
			case AUTOTYPER:
			case MODAUTOTYPER:
				return config.highlightOtherChat();
			default:
				return false;
		}
	}

	private void sendPreviewMessage()
	{
		clientThread.invokeLater(() ->
		{
			Color previewColour = config.previewColour();
			String previewText = getPreviewText();
			String message = PREVIEW_PREFIX
				+ ColorUtil.wrapWithColorTag(previewText, previewColour)
				+ " [" + toHex(previewColour) + "]";
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", message, null);
		});
	}

	private String getPreviewText()
	{
		if (!rules.isEmpty())
		{
			return rules.get(0).getText();
		}
		return "Example highlighted text";
	}

	private static String toHex(Color colour)
	{
		return String.format("#%02X%02X%02X", colour.getRed(), colour.getGreen(), colour.getBlue());
	}

	private void loadRules()
	{
		String configuredRules = config.rules();
		if (configuredRules == null || configuredRules.trim().isEmpty())
		{
			rules = Collections.emptyList();
			return;
		}

		List<HighlightRule> parsedRules = new ArrayList<>();
		String[] lines = configuredRules.split("\\R");
		for (int index = 0; index < lines.length; index++)
		{
			String line = lines[index].trim();
			if (line.isEmpty())
			{
				continue;
			}

			try
			{
				parsedRules.add(HighlightRule.parse(line));
			}
			catch (IllegalArgumentException exception)
			{
				log.warn("Ignoring invalid highlight rule on line {}: {} ({})",
					index + 1, line, exception.getMessage());
			}
		}

		rules = Collections.unmodifiableList(parsedRules);
		log.debug("Loaded {} custom text highlight rules", rules.size());
	}
}
