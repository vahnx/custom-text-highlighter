package com.customtexthighlighter;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(CustomTextHighlighterConfig.GROUP)
public interface CustomTextHighlighterConfig extends Config
{
    String GROUP = "customtexthighlighter";

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
}