package net.inventive_mods.client.util.widgets;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;

public class TabbedThreePartsLayoutWidget extends HeaderAndFooterLayout {
    private final LinearLayout tabBar = LinearLayout.horizontal().spacing(1);
    private int tabBarChildrenCount = 0;

    public TabbedThreePartsLayoutWidget(TabbedScreen screen, int headerHeight, int footerHeight) {
        super(screen, headerHeight, footerHeight);
        this.addToHeader(this.tabBar, positioner -> positioner.alignVertically(0.9f).paddingHorizontal(5));
    }

    public <T extends Button> void addTabButton(T button) {
        this.tabBarChildrenCount++;
        this.tabBar.addChild(button);
        this.tabBar.visitWidgets(widget -> widget.setWidth((this.getWidth() - 10) / this.tabBarChildrenCount));
    }

    public <T extends AbstractWidget> T replaceBody(T widget) {
        return this.addToContents(widget);
    }

    public void toggleButtons(Button pressedButton) {
        this.tabBar.visitWidgets(widget -> widget.visitWidgets(button -> button.active = button != pressedButton));
    }
}
