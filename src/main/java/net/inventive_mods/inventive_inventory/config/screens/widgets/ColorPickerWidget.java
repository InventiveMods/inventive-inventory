package net.inventive_mods.inventive_inventory.config.screens.widgets;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.options.fields.ColorFieldOption;
import net.inventive_mods.inventive_inventory.util.widgets.CustomClickableWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

import java.util.concurrent.atomic.AtomicBoolean;

public class ColorPickerWidget extends CustomClickableWidget {
    private final DirectionalLayoutWidget vertical = DirectionalLayoutWidget.vertical().spacing(5);

    public ColorPickerWidget(ColorFieldOption option) {
        super(150, 45);
        ColorFieldWidget colorField = new ColorFieldWidget(Text.of(Integer.toHexString(option.getValue() & 0xFFFFFF)), option);
        ConfigSliderWidget sliderWidget = new ConfigSliderWidget(150, 20, (double) ColorHelper.Argb.getAlpha(option.getValue()) / 255, option);
        DirectionalLayoutWidget horizontal = DirectionalLayoutWidget.horizontal().spacing(50);
        horizontal.add(colorField);
        horizontal.add(
                ButtonWidget.builder(Text.translatable("config.visuals.button.text.inventive_inventory.locked_slots.color.reset"),
                                button -> {
                                    colorField.reset();
                                    sliderWidget.reset();
                                    super.playDownSound(InventiveInventory.getClient().getSoundManager());
                                })
                        .tooltip(Tooltip.of(Text.translatable("config.visuals.button.tooltip.inventive_inventory.locked_slots.color.reset")))
                        .size(50, 20)
                        .build()
        );
        this.vertical.add(horizontal);
        this.vertical.add(sliderWidget);
        this.vertical.refreshPositions();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        this.vertical.setPosition(this.getX(), this.getY());
        this.vertical.forEachElement(widget -> {
            if (widget instanceof ClickableWidget) ((ClickableWidget) widget).render(context, mouseX, mouseY, delta);
            else if (widget instanceof DirectionalLayoutWidget)
                widget.forEachChild(innerWidget -> innerWidget.render(context, mouseX, mouseY, delta));
        });
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.vertical.forEachElement(element -> {
            if (element instanceof ConfigSliderWidget sliderWidget) {
                if (mouseX >= sliderWidget.getX() && mouseX <= sliderWidget.getRight() && mouseY >= sliderWidget.getY() && mouseY <= sliderWidget.getBottom()) {
                    sliderWidget.onClick(mouseX, mouseY);
                }
            } else if (element instanceof DirectionalLayoutWidget layoutWidget) {
                layoutWidget.forEachElement(innerElement -> {
                    if (innerElement instanceof TextFieldWidget textFieldWidget) {
                        if (mouseX >= textFieldWidget.getX() && mouseX <= textFieldWidget.getRight() && mouseY >= textFieldWidget.getY() && mouseY <= textFieldWidget.getBottom()) {
                            textFieldWidget.setFocused(true);
                            textFieldWidget.onClick(mouseX, mouseY);
                        } else textFieldWidget.setFocused(false);
                    } else if (innerElement instanceof ClickableWidget clickableWidget) {
                        if (mouseX >= clickableWidget.getX() && mouseX <= clickableWidget.getRight() && mouseY >= clickableWidget.getY() && mouseY <= clickableWidget.getBottom()) {
                            clickableWidget.onClick(mouseX, mouseY);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
        this.vertical.forEachElement(element -> {
            if (element instanceof ConfigSliderWidget sliderWidget) {
                if (mouseX >= sliderWidget.getX() && mouseX <= sliderWidget.getRight() && mouseY >= sliderWidget.getY() && mouseY <= sliderWidget.getBottom()) {
                    sliderWidget.onDrag(mouseX, mouseY, deltaX, deltaY);
                }
            }
        });
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.vertical.forEachElement(element -> {
            if (element instanceof DirectionalLayoutWidget layoutWidget) {
                layoutWidget.forEachElement(innerElement -> {
                    if (innerElement instanceof TextFieldWidget textFieldWidget) {
                        textFieldWidget.keyPressed(keyCode, scanCode, modifiers);
                    }
                });
            }
        });
        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        this.vertical.forEachElement(element -> {
            if (element instanceof DirectionalLayoutWidget layoutWidget) {
                layoutWidget.forEachElement(innerElement -> {
                    if (innerElement instanceof TextFieldWidget textFieldWidget) {
                        textFieldWidget.charTyped(chr, modifiers);
                    }
                });
            }
        });
        return false;
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    public boolean overSliderWidget(double mouseX, double mouseY) {
        AtomicBoolean bl = new AtomicBoolean(false);
        this.vertical.forEachElement(element -> {
            if (element instanceof ConfigSliderWidget sliderWidget) {
                bl.set(mouseX >= sliderWidget.getX() && mouseX <= sliderWidget.getRight() && mouseY >= sliderWidget.getY() && mouseY <= sliderWidget.getBottom());
            }
        });
        return bl.get();
    }

    public void clickSliderWidget(double mouseX, double mouseY) {
        this.vertical.forEachElement(element -> {
            if (element instanceof ConfigSliderWidget sliderWidget) {
                sliderWidget.onClick(mouseX, mouseY);
            }
        });
    }

    public void dragSliderWidget(double mouseX, double mouseY, double deltaX, double deltaY) {
        this.vertical.forEachElement(element -> {
            if (element instanceof ConfigSliderWidget sliderWidget) {
                sliderWidget.onDrag(mouseX, mouseY, deltaX, deltaY);
            }
        });
    }
}
