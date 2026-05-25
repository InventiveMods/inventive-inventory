package net.inventive_mods.client.util.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class TabbedHeaderAndFooterLayout extends HeaderAndFooterLayout {
    public TabbedHeaderAndFooterLayout(Screen screen, int headerHeight, int footerHeight) {
        super(screen, headerHeight, footerHeight);
    }

    public void buildHeaderWithTitle(Component title, Font font, List<Button> tabButtons) {
        LinearLayout vertical = LinearLayout.vertical().spacing(5);
        vertical.addChild(new StringWidget(title, font), LayoutSettings::alignHorizontallyCenter);
        LinearLayout tabBar = LinearLayout.horizontal().spacing(2);
        int buttonWidth = (this.getWidth() - 10) / tabButtons.size();
        tabButtons.forEach(button -> {
            button.setWidth(buttonWidth);
            tabBar.addChild(button);
        });
        vertical.addChild(tabBar, LayoutSettings::alignHorizontallyCenter);
        this.addToHeader(vertical);
    }
}
