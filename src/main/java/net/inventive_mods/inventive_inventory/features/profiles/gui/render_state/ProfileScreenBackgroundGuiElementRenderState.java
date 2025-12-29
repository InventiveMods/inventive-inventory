package net.inventive_mods.inventive_inventory.features.profiles.gui.render_state;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.inventive_mods.inventive_inventory.InventiveInventory;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.SimpleGuiElementRenderState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.TextureSetup;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;

public record ProfileScreenBackgroundGuiElementRenderState(RenderPipeline pipeline, TextureSetup textureSetup,
                                                           Matrix3x2f pose, float x0, float y0, float x1, float y1,
                                                           float x2, float y2, float x3, float y3, int color,
                                                           @Nullable ScreenRect scissorArea,
                                                           @Nullable ScreenRect bounds) implements SimpleGuiElementRenderState {
    public ProfileScreenBackgroundGuiElementRenderState(DrawContext context, float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, int color) {
        this(RenderPipelines.GUI, TextureSetup.empty(), context.getMatrices(), x0, y0, x1, y1, x2, y2, x3, y3, color, context.scissorStack.peekLast(), createBounds(context.getMatrices(), context.scissorStack.peekLast()));
    }

    public void setupVertices(VertexConsumer vertices, float depth) {
        vertices.vertex(this.pose(), this.x0(), this.y0(), depth).color(this.color());
        vertices.vertex(this.pose(), this.x1(), this.y1(), depth).color(this.color());
        vertices.vertex(this.pose(), this.x2(), this.y2(), depth).color(this.color());
        vertices.vertex(this.pose(), this.x3(), this.y3(), depth).color(this.color());
    }

    @Nullable
    private static ScreenRect createBounds(Matrix3x2f pose, @Nullable ScreenRect scissorArea) {
        new ScreenRect(0, 0, InventiveInventory.getScreen().width, InventiveInventory.getScreen().height);
        ScreenRect screenRect = (new ScreenRect(0, 0, InventiveInventory.getScreen().width, InventiveInventory.getScreen().height)).transformEachVertex(pose);
        return scissorArea != null ? scissorArea.intersection(screenRect) : screenRect;
    }
}
