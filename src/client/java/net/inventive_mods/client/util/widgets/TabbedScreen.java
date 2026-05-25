package net.inventive_mods.client.util.widgets;

import net.inventive_mods.client.InventiveInventoryClient;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public abstract class TabbedScreen extends OptionsSubScreen {
    private int activeTabIndex = 0;
    private final List<TabEntry> tabEntries = new ArrayList<>();

    public TabbedScreen(Screen lastScreen, String titleTranslationKey) {
        super(lastScreen, InventiveInventoryClient.getClient().options, Component.translatable(titleTranslationKey));
    }

    @Override
    protected void init() {
        this.layout = new TabbedHeaderAndFooterLayout(this, 50, 33);
        this.tabEntries.clear();
        this.addTabs();
        this.addHeader();
        this.addBody();
        this.addFooter();
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    protected abstract void addTabs();

    protected void addHeader() {
        List<Button> buttons = new ArrayList<>();
        for (int i = 0; i < this.tabEntries.size(); i++) {
            int index = i;
            TabEntry entry = this.tabEntries.get(i);
            Button button = Button.builder(Component.translatable("config.screen.tab." + InventiveInventoryClient.MOD_ID + entry.translationKey()), _ -> {
                this.activeTabIndex = index;
                this.rebuildWidgets();
            }).build();
            button.active = i != this.activeTabIndex;
            buttons.add(button);
        }
        ((TabbedHeaderAndFooterLayout) this.layout).buildHeaderWithTitle(this.title, this.font, buttons);
    }

    protected void addBody() {
        this.list = this.layout.addToContents(this.tabEntries.get(this.activeTabIndex).tab());
    }

    protected void addTab(String translationKey, ScreenTab tab) {
        this.tabEntries.add(new TabEntry(translationKey, tab));
    }

    @Override
    protected void addOptions() {

    }

    @Override
    public void onClose() {
        this.tabEntries.forEach(tabEntry -> tabEntry.tab().onClose());
        this.minecraft.setScreen(this.lastScreen);
    }

    private record TabEntry(String translationKey, ScreenTab tab) {

    }
}
