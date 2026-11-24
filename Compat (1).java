package dev.fnex.compat;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.input.KeyEvent;

/** Differences between Minecraft versions: this is the 26.3 (SDL3) variant. */
public final class Compat {
    /** 26.3 only sends charTyped while SDL text input is on, so letters are read from the key event instead. */
    public static final boolean KEY_TEXT = true;
    public static char typedChar(KeyEvent ev) {
        int k = ev.keycode(); // SDL keycode: lowercase ASCII for letters/digits
        if ((k >= 'a' && k <= 'z') || (k >= '0' && k <= '9') || k == ' ' || k == '-' || k == '_') return (char) k;
        return 0;
    }

    public static boolean isKeyDown(int key) {
        return InputConstants.isKeyDown(key);
    }
    public static String keyName(int key) {
        return InputConstants.Type.KEYBOARD.getOrCreate(key).getDisplayName().getString();
    }
    public static Screen optionsScreen(Screen parent) {
        return new OptionsScreen(parent, Minecraft.getInstance().options);
    }
}
