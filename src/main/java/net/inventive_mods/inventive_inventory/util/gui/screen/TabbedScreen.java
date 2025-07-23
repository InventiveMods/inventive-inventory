package net.inventive_mods.inventive_inventory.util.gui.screen;

import net.inventive_mods.inventive_inventory.util.gui.layout.TabbedLayout;
import net.inventive_mods.inventive_inventory.util.gui.widget.ScreenTab;
import net.inventive_mods.inventive_inventory.util.gui.widget.TextWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class TabbedScreen extends Screen {
    private final Screen parent;
    protected static ScreenTab activeTab;
    protected final List<ScreenTab> tabs = new ArrayList<>();
    public final TabbedLayout layout = new TabbedLayout(this, 50, 33);

    public TabbedScreen(Screen parent, String titleTranslationKey) {
        super(Component.translatable(titleTranslationKey));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.initHeader();
        this.addTabs();
        this.initContent();
        this.initFooter();
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    protected void initHeader() {
        this.layout.addToHeader(new TextWidget(this.title, this.font), positioner -> positioner.alignVertically(0.25f));
    }

    protected void addTabs() {}

    protected void initContent() {
        activeTab = this.layout.addToContents(this.tabs.getFirst());
    }

    protected void initFooter() {
        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).width(200).build());
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        this.tabs.forEach(screenTab -> screenTab.updateSize(this.width, this.layout));
    }

    protected void addTab(String translationKey, ScreenTab tab, boolean active) {
        Button tabButton = Button.builder(Component.translatable("config.screen.tab.inventive_inventory." + translationKey), button -> {
            this.removeWidget(activeTab);
            activeTab = this.layout.replaceBody(tab);
            this.addRenderableWidget(tab);
            this.layout.toggleButtons(button);
        }).width(125).build();
        tabButton.active = !active;
        this.layout.addTabButton(tabButton);
        this.tabs.add(tab);
    }

    @Override
    public void onClose() {
        this.tabs.forEach(ScreenTab::onClose);
        if (this.minecraft != null) this.minecraft.setScreen(this.parent);
    }

}
