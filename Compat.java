package dev.fnex.compat;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.input.KeyEvent;

/** Differences between Minecraft versions: this is the 26.2 (GLFW) variant. */
public final class Compat {
    /** 26.2 delivers typed characters via charTyped, so no key-based text is needed. */
    public static final boolean KEY_TEXT = false;
    public static char typedChar(KeyEvent ev) { return 0; }

    public static boolean isKeyDown(int key) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), key);
    }
    public static String keyName(int key) {
        return InputConstants.Type.KEYSYM.getOrCreate(key).getDisplayName().getString();
    }
    public static Screen optionsScreen(Screen parent) {
        return new OptionsScreen(parent, Minecraft.getInstance().options, false);
    }
}
