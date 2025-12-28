package net.inventive_mods.inventive_inventory.config.screens.widgets;

import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.inventive_mods.inventive_inventory.config.options.fields.ColorFieldOption;
import net.inventive_mods.inventive_inventory.util.widgets.CustomClickableWidget;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

import java.util.concurrent.atomic.AtomicBoolean;

public class ColorPickerWidget extends CustomClickableWidget {
    private final DirectionalLayoutWidget vertical = DirectionalLayoutWidget.vertical().spacing(5);

    public ColorPickerWidget(ColorFieldOption option) {
        super(150, 45);
        ColorFieldWidget colorField = new ColorFieldWidget(Text.of(Integer.toHexString(ColorHelper.zeroAlpha(option.getValue()))), option);
        ConfigSliderWidget sliderWidget = new ConfigSliderWidget(150, 20, (double) ColorHelper.getAlpha(option.getValue()) / 255, option);
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
    public void onClick(Click click, boolean doubled) {
        this.vertical.forEachElement(element -> {
            if (element instanceof ConfigSliderWidget sliderWidget) {
                if (click.x() >= sliderWidget.getX() && click.x() <= sliderWidget.getRight() && click.y() >= sliderWidget.getY() && click.y() <= sliderWidget.getBottom()) {
                    sliderWidget.onClick(click, doubled);
                }
            } else if (element instanceof DirectionalLayoutWidget layoutWidget) {
                layoutWidget.forEachElement(innerElement -> {
                    if (innerElement instanceof TextFieldWidget textFieldWidget) {
                        if (click.x() >= textFieldWidget.getX() && click.x() <= textFieldWidget.getRight() && click.y() >= textFieldWidget.getY() && click.y() <= textFieldWidget.getBottom()) {
                            textFieldWidget.setFocused(true);
                            textFieldWidget.onClick(click, doubled);
                        } else textFieldWidget.setFocused(false);
                    } else if (innerElement instanceof ClickableWidget clickableWidget) {
                        if (click.x() >= clickableWidget.getX() && click.x() <= clickableWidget.getRight() && click.y() >= clickableWidget.getY() && click.y() <= clickableWidget.getBottom()) {
                            clickableWidget.onClick(click, doubled);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDrag(Click click, double deltaX, double deltaY) {
        this.vertical.forEachElement(element -> {
            if (element instanceof ConfigSliderWidget sliderWidget) {
                if (click.x() >= sliderWidget.getX() && click.x() <= sliderWidget.getRight() && click.y() >= sliderWidget.getY() && click.y() <= sliderWidget.getBottom()) {
                    sliderWidget.onDrag(click, deltaX, deltaY);
                }
            }
        });
    }

    @Override
    public boolean keyPressed(KeyInput keyInput) {
        this.vertical.forEachElement(element -> {
            if (element instanceof DirectionalLayoutWidget layoutWidget) {
                layoutWidget.forEachElement(innerElement -> {
                    if (innerElement instanceof TextFieldWidget textFieldWidget) {
                        textFieldWidget.keyPressed(keyInput);
                    }
                });
            }
        });
        return false;
    }

    @Override
    public boolean charTyped(CharInput charInput) {
        this.vertical.forEachElement(element -> {
            if (element instanceof DirectionalLayoutWidget layoutWidget) {
                layoutWidget.forEachElement(innerElement -> {
                    if (innerElement instanceof TextFieldWidget textFieldWidget) {
                        textFieldWidget.charTyped(charInput);
                    }
                });
            }
        });
        return false;
    }

    @Override
    public void playDownSound(SoundManager soundManager) {}

    public boolean overSliderWidget(Click click) {
        AtomicBoolean bl = new AtomicBoolean(false);
        this.vertical.forEachElement(element -> {
            if (element instanceof ConfigSliderWidget sliderWidget) {
                bl.set(click.x() >= sliderWidget.getX() && click.x() <= sliderWidget.getRight() && click.y() >= sliderWidget.getY() && click.y() <= sliderWidget.getBottom());
            }
        });
        return bl.get();
    }

    public void clickSliderWidget(Click click, boolean doubled) {
        this.vertical.forEachElement(element -> {
            if (element instanceof ConfigSliderWidget sliderWidget) {
                sliderWidget.onClick(click, doubled);
            }
        });
    }

    public void dragSliderWidget(Click click, double deltaX, double deltaY) {
        this.vertical.forEachElement(element -> {
            if (element instanceof ConfigSliderWidget sliderWidget) {
                sliderWidget.onDrag(click, deltaX, deltaY);
            }
        });
    }
}
