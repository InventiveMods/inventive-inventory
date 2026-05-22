package net.inventive_mods.client.util.widgets;

import net.inventive_mods.client.InventiveInventoryClient;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public abstract class TabbedScreen extends OptionsSubScreen {
    private final Screen parent;
    protected static ScreenTab activeTab;
    protected final List<ScreenTab> tabs = new ArrayList<>();
    protected final TabbedThreePartsLayoutWidget layout = new TabbedThreePartsLayoutWidget(this, 50, 33);

    public TabbedScreen(Screen parent, String titleTranslationKey) {
        super(parent, InventiveInventoryClient.getClient().options, Component.translatable(titleTranslationKey));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.initHeader();
        this.addTabs();
        this.initBody();
        this.initFooter();
        this.layout.visitWidgets(this::addRenderableWidget);
        this.initTabNavigation();
    }

    protected void initHeader() {
        this.layout.addToHeader(new StringWidget(this.title, this.font), positioner -> positioner.alignVertically(0.25f).alignHorizontallyCenter());
    }

    protected void addTabs() {

    }

    protected void initBody() {
        activeTab = this.layout.addToContents(this.tabs.getFirst());
    }

    protected void initFooter() {
        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, (_) -> this.onClose()).width(200).build());
    }

    protected void initTabNavigation() {
        this.layout.arrangeElements();
        this.tabs.forEach(screenTab -> screenTab.updateSize(this.width, this.layout));
    }

    @Override
    public void onClose() {
        this.tabs.forEach(ScreenTab::onClose);
        this.minecraft.setScreen(this.parent);
    }

    protected void addTab(String translationKey, ScreenTab tab, boolean active) {
        Button tabButton = Button.builder(Component.translatable("config.screen.tab." + InventiveInventoryClient.MOD_ID + "." + translationKey), button -> {
            this.removeWidget(activeTab);
            activeTab = this.layout.replaceBody(tab);
            this.addRenderableWidget(tab);
            this.layout.toggleButtons(button);
        }).build();
        tabButton.active = !active;
        this.layout.addTabButton(tabButton);
        this.tabs.add(tab);
    }
    
    protected void addOptions() {
        
    }
}
