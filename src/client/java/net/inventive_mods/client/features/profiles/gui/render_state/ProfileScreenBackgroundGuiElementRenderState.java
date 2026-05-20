package net.inventive_mods.client.features.profiles.gui.render_state;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.inventive_mods.client.InventiveInventoryClient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public record ProfileScreenBackgroundGuiElementRenderState(RenderPipeline pipeline, TextureSetup textureSetup,
                                                           Matrix3x2f pose, float x0, float y0, float x1, float y1,
                                                           float x2, float y2, float x3, float y3, int color,
                                                           @Nullable ScreenRectangle scissorArea,
                                                           @Nullable ScreenRectangle bounds) implements GuiElementRenderState {
    public ProfileScreenBackgroundGuiElementRenderState(GuiGraphicsExtractor context, float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, int color) {
        this(RenderPipelines.GUI, TextureSetup.noTexture(), context.pose(), x0, y0, x1, y1, x2, y2, x3, y3, color, context.scissorStack.peek(), createBounds(context.pose(), context.scissorStack.peek()));
    }

    public void buildVertices(VertexConsumer vertices) {
        vertices.addVertexWith2DPose(this.pose(), this.x0(), this.y0()).setColor(this.color());
        vertices.addVertexWith2DPose(this.pose(), this.x1(), this.y1()).setColor(this.color());
        vertices.addVertexWith2DPose(this.pose(), this.x2(), this.y2()).setColor(this.color());
        vertices.addVertexWith2DPose(this.pose(), this.x3(), this.y3()).setColor(this.color());
    }

    @Nullable
    private static ScreenRectangle createBounds(Matrix3x2f pose, @Nullable ScreenRectangle scissorArea) {
        new ScreenRectangle(0, 0, InventiveInventoryClient.getScreen().width, InventiveInventoryClient.getScreen().height);
        ScreenRectangle screenRect = (new ScreenRectangle(0, 0, InventiveInventoryClient.getScreen().width, InventiveInventoryClient.getScreen().height)).transformMaxBounds(pose);
        return scissorArea != null ? scissorArea.intersection(screenRect) : screenRect;
    }
}
