package net.inventive_mods.inventive_inventory.config.gui.widget;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.option.field.ColorFieldOption;
import net.inventive_mods.inventive_inventory.util.gui.widget.ClickableWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

import java.util.concurrent.atomic.AtomicBoolean;

public class ColorPickerWidget extends ClickableWidget {
    private final LinearLayout verticalLayout = LinearLayout.vertical().spacing(5);

    public ColorPickerWidget(ColorFieldOption option) {
        super(150, 50);
        ColorFieldWidget colorFieldWidget = new ColorFieldWidget(Integer.toHexString(ARGB.color(0, option.getValue())), option);
        ColorOpacitySliderWidget sliderWidget = new ColorOpacitySliderWidget(150, 20, (double) ARGB.alpha(option.getValue()) / 255, option);
        LinearLayout horizontalLayout = LinearLayout.horizontal().spacing(50);
        horizontalLayout.addChild(colorFieldWidget);
        horizontalLayout.addChild(
                Button.builder(Component.translatable("config.visuals.button.text.inventive_inventory.locked_slots.color.reset"),
                        button -> {
                            colorFieldWidget.reset();
                            sliderWidget.reset();
                            super.playDownSound(InventiveInventory.getMinecraft().getSoundManager());
                        })
                        .tooltip(Tooltip.create(Component.translatable("config.visuals.button.tooltip.inventive_inventory.locked_slots.color.reset")))
                        .size(50, 20)
                        .build()
        );
        this.verticalLayout.addChild(horizontalLayout);
        this.verticalLayout.addChild(sliderWidget);
        this.verticalLayout.arrangeElements();
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.verticalLayout.setPosition(this.getX(), this.getY());
        this.verticalLayout.visitChildren(layoutElement -> {
            if (layoutElement instanceof AbstractWidget) ((AbstractWidget) layoutElement).render(guiGraphics, mouseX, mouseY, partialTick);
            else if (layoutElement instanceof LinearLayout) layoutElement.visitWidgets(widget -> widget.render(guiGraphics, mouseX, mouseY, partialTick));
        });
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        this.verticalLayout.visitChildren(layoutElement -> {
            if (layoutElement instanceof ColorOpacitySliderWidget sliderWidget) {
                if (sliderWidget.isMouseOver(mouseX, mouseY))
                    sliderWidget.onClick(mouseX, mouseY, button);
            } else if (layoutElement instanceof LinearLayout layout) {
                layout.visitWidgets(widget -> {
                    if (widget instanceof EditBox editBox) {
                        if (editBox.isMouseOver(mouseX, mouseY)) {
                            editBox.setFocused(true);
                            editBox.onClick(mouseX, mouseY, button);
                        } else editBox.setFocused(false);
                    } else {
                        if (widget.isMouseOver(mouseX, mouseY))
                            widget.onClick(mouseX, mouseY, button);
                    }
                });
            }
        });
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        this.verticalLayout.visitChildren(layoutElement -> {
            if (layoutElement instanceof ColorOpacitySliderWidget sliderWidget) {
                if (sliderWidget.isMouseOver(mouseX, mouseY))
                    sliderWidget.onDrag(mouseX, mouseY, dragX, dragY);
            }
        });
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.verticalLayout.visitChildren(layoutElement -> {
            if (layoutElement instanceof LinearLayout layout) {
                layout.visitWidgets(widget -> {
                    if (widget instanceof EditBox editBox)
                        editBox.keyPressed(keyCode, scanCode, modifiers);
                });
            }
        });
        return false;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        this.verticalLayout.visitChildren(layoutElement -> {
            if (layoutElement instanceof LinearLayout layout) {
                layout.visitWidgets(widget -> {
                    if (widget instanceof EditBox editBox)
                        editBox.charTyped(codePoint, modifiers);
                });
            }
        });
        return false;
    }

    @Override
    public void playDownSound(SoundManager handler) {
    }

    public boolean overSliderWidget(double mouseX, double mouseY) {
        AtomicBoolean bl = new AtomicBoolean(false);
        this.verticalLayout.visitChildren(element -> {
            if (element instanceof ColorOpacitySliderWidget sliderWidget) {
                bl.set(sliderWidget.isMouseOver(mouseX, mouseY));
            }
        });
        return bl.get();
    }

    public void clickSliderWidget(double mouseX, double mouseY) {
        this.verticalLayout.visitChildren(element -> {
            if (element instanceof ColorOpacitySliderWidget sliderWidget) {
                sliderWidget.onClick(mouseX, mouseY);
            }
        });
    }

    public void dragSliderWidget(double mouseX, double mouseY, double deltaX, double deltaY) {
        this.verticalLayout.visitChildren(element -> {
            if (element instanceof ColorOpacitySliderWidget sliderWidget) {
                sliderWidget.onDrag(mouseX, mouseY, deltaX, deltaY);
            }
        });
    }
}
