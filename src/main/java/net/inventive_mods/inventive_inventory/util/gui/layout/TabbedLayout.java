package net.inventive_mods.inventive_inventory.util.gui.layout;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;

public class TabbedLayout extends HeaderAndFooterLayout {
    private final LinearLayout tabBar = LinearLayout.horizontal().spacing(1);

    public TabbedLayout(Screen screen, int headerHeight, int footerHeight) {
        super(screen, headerHeight, footerHeight);
        this.addToHeader(tabBar, layoutSettings -> layoutSettings.alignVertically(0.9f));
    }

    public <T extends Button> void addTabButton(T button) {
        this.tabBar.addChild(button);
    }

    public <T extends LayoutElement> T replaceBody(T widget) {
        this.contentsFrame.children.clear();
        return this.contentsFrame.addChild(widget);
    }

    public void toggleButtons(Button pressedButton) {
        this.tabBar.visitChildren(widget -> widget.visitWidgets(button -> button.active = !button.equals(pressedButton)));
    }
}
