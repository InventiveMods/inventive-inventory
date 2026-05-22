package net.inventive_mods.client.config.screens.widgets;

import net.inventive_mods.client.InventiveInventoryClient;
import net.inventive_mods.client.config.options.fields.ColorFieldOption;
import net.inventive_mods.client.util.widgets.BaseWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.atomic.AtomicBoolean;

public class ColorPickerWidget extends BaseWidget {
    private final LinearLayout vertical = LinearLayout.vertical().spacing(5);

    public ColorPickerWidget(ColorFieldOption option) {
        super(150, 45);
        ColorFieldWidget colorField = new ColorFieldWidget(Component.literal(Integer.toHexString(ARGB.transparent(option.getValue()))), option);
        ConfigSliderWidget sliderWidget = new ConfigSliderWidget(150, 20, (double) ARGB.alpha(option.getValue()) / 255, option);
        LinearLayout horizontal = LinearLayout.horizontal().spacing(50);
        horizontal.addChild(colorField);
        horizontal.addChild(
                Button.builder(Component.translatable("config.visuals.button.text." + InventiveInventoryClient.MOD_ID + ".locked_slots.color.reset"),
                                _ -> {
                                    colorField.reset();
                                    sliderWidget.reset();
                                    super.playDownSound(InventiveInventoryClient.getClient().getSoundManager());
                                })
                        .tooltip(Tooltip.create(Component.translatable("config.visuals.button.tooltip." + InventiveInventoryClient.MOD_ID + ".locked_slots.color.reset")))
                        .size(50, 20)
                        .build()
        );
        this.vertical.addChild(horizontal);
        this.vertical.addChild(sliderWidget);
        this.vertical.arrangeElements();
    }

    @Override
    protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        this.vertical.setPosition(this.getX(), this.getY());
        this.vertical.visitChildren(widget -> {
            if (widget instanceof AbstractWidget)
                ((AbstractWidget) widget).extractRenderState(graphics, mouseX, mouseY, a);
            else if (widget instanceof LinearLayout)
                widget.visitWidgets(innerWidget -> innerWidget.extractRenderState(graphics, mouseX, mouseY, a));
        });
    }

    @Override
    public void onClick(@NonNull MouseButtonEvent click, boolean doubled) {
        this.vertical.visitChildren(element -> {
            if (element instanceof ConfigSliderWidget sliderWidget) {
                if (click.x() >= sliderWidget.getX() && click.x() <= sliderWidget.getRight() && click.y() >= sliderWidget.getY() && click.y() <= sliderWidget.getBottom()) {
                    sliderWidget.onClick(click, doubled);
                }
            } else if (element instanceof LinearLayout layoutWidget) {
                layoutWidget.visitChildren(innerElement -> {
                    if (innerElement instanceof EditBox textFieldWidget) {
                        if (click.x() >= textFieldWidget.getX() && click.x() <= textFieldWidget.getRight() && click.y() >= textFieldWidget.getY() && click.y() <= textFieldWidget.getBottom()) {
                            textFieldWidget.setFocused(true);
                            textFieldWidget.onClick(click, doubled);
                        } else textFieldWidget.setFocused(false);
                    } else if (innerElement instanceof AbstractWidget clickableWidget) {
                        if (click.x() >= clickableWidget.getX() && click.x() <= clickableWidget.getRight() && click.y() >= clickableWidget.getY() && click.y() <= clickableWidget.getBottom()) {
                            clickableWidget.onClick(click, doubled);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDrag(@NonNull MouseButtonEvent click, double deltaX, double deltaY) {
        this.vertical.visitChildren(element -> {
            if (element instanceof ConfigSliderWidget sliderWidget) {
                if (click.x() >= sliderWidget.getX() && click.x() <= sliderWidget.getRight() && click.y() >= sliderWidget.getY() && click.y() <= sliderWidget.getBottom()) {
                    sliderWidget.onDrag(click, deltaX, deltaY);
                }
            }
        });
    }

    @Override
    public boolean keyPressed(@NonNull KeyEvent keyInput) {
        this.vertical.visitChildren(element -> {
            if (element instanceof LinearLayout layoutWidget) {
                layoutWidget.visitChildren(innerElement -> {
                    if (innerElement instanceof EditBox textFieldWidget) {
                        textFieldWidget.keyPressed(keyInput);
                    }
                });
            }
        });
        return false;
    }

    @Override
    public boolean charTyped(@NonNull CharacterEvent charInput) {
        this.vertical.visitChildren(element -> {
            if (element instanceof LinearLayout layoutWidget) {
                layoutWidget.visitChildren(innerElement -> {
                    if (innerElement instanceof EditBox textFieldWidget) {
                        textFieldWidget.charTyped(charInput);
                    }
                });
            }
        });
        return false;
    }

    public boolean overSliderWidget(MouseButtonEvent click) {
        AtomicBoolean bl = new AtomicBoolean(false);
        this.vertical.visitChildren(element -> {
            if (element instanceof ConfigSliderWidget sliderWidget) {
                bl.set(click.x() >= sliderWidget.getX() && click.x() <= sliderWidget.getRight() && click.y() >= sliderWidget.getY() && click.y() <= sliderWidget.getBottom());
            }
        });
        return bl.get();
    }

    public void clickSliderWidget(MouseButtonEvent click, boolean doubled) {
        this.vertical.visitChildren(element -> {
            if (element instanceof ConfigSliderWidget sliderWidget) {
                sliderWidget.onClick(click, doubled);
            }
        });
    }

    public void dragSliderWidget(MouseButtonEvent click, double deltaX, double deltaY) {
        this.vertical.visitChildren(element -> {
            if (element instanceof ConfigSliderWidget sliderWidget) {
                sliderWidget.onDrag(click, deltaX, deltaY);
            }
        });
    }
}
