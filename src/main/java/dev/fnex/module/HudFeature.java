package dev.fnex.module;

import dev.fnex.gui.Draw;
import dev.fnex.gui.Theme;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public abstract class HudFeature extends Feature {
    public int hudX, hudY, w = 60, h = 18;
    /** Size of this HUD element (0.5x - 3x). Also changeable with the mouse wheel in the HUD Editor. */
    public final Setting.Num scale = add(new Setting.Num("Size", 1.0, 0.5, 3.0, 0.1));

    protected HudFeature(String n, String d, Category c, int x, int y) { super(n, d, c); hudX = x; hudY = y; }

    protected abstract String label();
    protected abstract String value();

    /** Draws the element in its own (unscaled) coordinate space; sets w and h. */
    public void draw(GuiGraphicsExtractor c, int x, int y) {
        String l = label() + " ", v = value();
        w = Draw.tw(l + v) + 14; h = 18;
        Draw.round(c, x, y, w, h, 4, 0xB00A0405);
        Draw.round(c, x, y + 2, 2, h - 4, 1, Theme.accent());
        Draw.text(c, l, x + 7, y + 5, Theme.accent2());
        Draw.text(c, v, x + 7 + Draw.tw(l), y + 5, 0xFFFFFFFF);
    }

    /** On-screen size after scaling. */
    public int sw() { return Math.max(1, Math.round(w * scale.f())); }
    public int sh() { return Math.max(1, Math.round(h * scale.f())); }

    public void drawScaled(GuiGraphicsExtractor c, int x, int y) {
        float s = scale.f();
        var m = c.pose();
        m.pushMatrix(); m.translate(x, y); m.scale(s, s);
        draw(c, 0, 0);
        m.popMatrix();
    }

    public void renderHud(GuiGraphicsExtractor c) {
        if (!enabled) return;
        int x = Math.max(0, Math.min(hudX, c.guiWidth() - sw()));
        int y = Math.max(0, Math.min(hudY, c.guiHeight() - sh()));
        drawScaled(c, x, y);
    }
}
