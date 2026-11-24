package dev.fnex.gui;

import com.mojang.blaze3d.platform.InputConstants;
import dev.fnex.config.ConfigManager;
import dev.fnex.module.Feature;
import dev.fnex.module.HudFeature;
import dev.fnex.module.Modules;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;

import java.util.Locale;

public class HudEditorScreen extends FnexScreen {
    private final Screen parent;
    private HudFeature dragged;
    private int offX, offY;

    public HudEditorScreen(Screen parent) { super("HUD Editor"); this.parent = parent; }

    private HudFeature hovered(int mx, int my) {
        for (int i = Modules.ALL.size() - 1; i >= 0; i--)
            if (Modules.ALL.get(i) instanceof HudFeature h && h.enabled && in(mx, my, h.hudX, h.hudY, h.sw(), h.sh())) return h;
        return null;
    }

    @Override protected void draw(GuiGraphicsExtractor c, int mx, int my) {
        Draw.rect(c, 0, 0, width, height, 0x80000000);
        int ac = Theme.accent();
        boolean any = false;
        HudFeature hov = dragged != null ? dragged : hovered(mx, my);
        for (Feature f : Modules.ALL) {
            if (!(f instanceof HudFeature h) || !h.enabled) continue;
            any = true;
            h.hudX = Math.max(0, Math.min(h.hudX, width - h.sw()));
            h.hudY = Math.max(0, Math.min(h.hudY, height - h.sh()));
            h.drawScaled(c, h.hudX, h.hudY);
            Draw.frame(c, h.hudX - 1, h.hudY - 1, h.sw() + 2, h.sh() + 2, h == hov ? Theme.AMBER : Draw.alpha(ac, 0.7f));
        }
        if (hov != null) {
            String s = hov.name + "  -  Size " + String.format(Locale.ROOT, "%.1f", hov.scale.value) + "x";
            int ty = hov.hudY >= 12 ? hov.hudY - 11 : hov.hudY + hov.sh() + 3;
            Draw.text(c, s, hov.hudX, ty, Theme.AMBER);
        }
        Draw.textC(c, "HUD EDITOR", width / 2, 12, Theme.AMBER);
        Draw.textC(c, any ? "Drag = move   |   Mouse wheel = bigger / smaller   |   ESC = done"
                          : "No HUD element is enabled. Enable some in the FNEX menu first.", width / 2, 26, Theme.GRAY);
    }

    @Override protected boolean click(int mx, int my, int btn) {
        if (btn != InputConstants.MOUSE_BUTTON_LEFT) return false;
        HudFeature h = hovered(mx, my);
        if (h == null) return false;
        dragged = h; offX = mx - h.hudX; offY = my - h.hudY;
        return true;
    }
    @Override protected boolean drag(int mx, int my, int btn) {
        if (dragged == null) return false;
        dragged.hudX = Math.max(0, Math.min(mx - offX, width - dragged.sw()));
        dragged.hudY = Math.max(0, Math.min(my - offY, height - dragged.sh()));
        ConfigManager.markDirty();
        return true;
    }
    @Override protected boolean release(int mx, int my, int btn) { boolean d = dragged != null; dragged = null; return d; }
    @Override protected boolean scroll(int mx, int my, double amount) {
        HudFeature h = dragged != null ? dragged : hovered(mx, my);
        if (h == null) return false;
        h.scale.set(h.scale.value + (amount > 0 ? 0.1 : -0.1));
        return true;
    }
    @Override public void onClose() { ConfigManager.save(); minecraft.setScreenAndShow(parent); }
}
