package dev.fnex.compat;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;

/** Differences between Minecraft versions: this is the 26.3 (SDL3) variant. */
public final class Compat {
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
