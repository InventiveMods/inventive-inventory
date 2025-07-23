package net.inventive_mods.inventive_inventory.feature.profile.gui.render_state;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.gui.render.state.GuiElementRenderState;
import net.minecraft.client.renderer.RenderPipelines;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fStack;

public record ProfileScreenBackgroundGuiElementRenderState(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2fStack pose, float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, int color, @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds) implements GuiElementRenderState {
    public ProfileScreenBackgroundGuiElementRenderState(GuiGraphics guiGraphics, float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, int color) {
        this(RenderPipelines.GUI, TextureSetup.noTexture(), guiGraphics.pose(), x0, y0, x1, y1, x2, y2, x3, y3, color, guiGraphics.peekScissorStack(), createBounds(guiGraphics.pose(), guiGraphics.peekScissorStack()));
    }

    public void buildVertices(VertexConsumer vertices, float depth) {
        vertices.addVertexWith2DPose(this.pose(), this.x0(), this.y0(), depth).setColor(this.color());
        vertices.addVertexWith2DPose(this.pose(), this.x1(), this.y1(), depth).setColor(this.color());
        vertices.addVertexWith2DPose(this.pose(), this.x2(), this.y2(), depth).setColor(this.color());
        vertices.addVertexWith2DPose(this.pose(), this.x3(), this.y3(), depth).setColor(this.color());
    }

    @Nullable
    private static ScreenRectangle createBounds(Matrix3x2f pose, @Nullable ScreenRectangle scissorArea) {
        new ScreenRectangle(0, 0, InventiveInventory.getScreen().width, InventiveInventory.getScreen().height);
        ScreenRectangle screenRect = (new ScreenRectangle(0, 0, InventiveInventory.getScreen().width, InventiveInventory.getScreen().height)).transformMaxBounds(pose);
        return scissorArea != null ? scissorArea.intersection(screenRect) : screenRect;
    }
}
