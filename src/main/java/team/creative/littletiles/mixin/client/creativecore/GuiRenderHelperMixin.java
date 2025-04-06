package team.creative.littletiles.mixin.client.creativecore;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import team.creative.creativecore.client.render.GuiRenderHelper;

@Mixin(GuiRenderHelper.class)
public abstract class GuiRenderHelperMixin {

    @Shadow(remap = false)
    public static void colorRect(PoseStack pose, BufferBuilder builder, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
    }

    /**
     * @author Pandier
     * @reason Fix rendering issues
     */
    @Overwrite(remap = false)
    public static void drawItemStackDecorations(PoseStack pose, ItemStack stack, int count) {
        if (stack.isEmpty()) return;
        Minecraft mc = Minecraft.getInstance();

        pose.pushPose();
        if (count != 1) {
            String s = String.valueOf(stack.getCount());
            pose.translate(0.0F, 0.0F, 200.0F);
            MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            mc.font.drawInBatch(s, 19 - 2 - mc.font.width(s), 6 + 3, 16777215, true, pose.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            bufferSource.endBatch();
        }

        if (stack.isBarVisible()) {
            int l = stack.getBarWidth();
            int i = stack.getBarColor();
            int j = 2;
            int k = 13;
            BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
            colorRect(pose, bufferBuilder, j, k, 13, 2, 0, 0, 0, 255);
            colorRect(pose, bufferBuilder, j, k, l, 1, i >> 16 & 255, i >> 8 & 255, i & 255, 255);
        }

        LocalPlayer player = mc.player;
        float cooldown = player == null ? 0.0F : player.getCooldowns().getCooldownPercent(stack.getItem(), mc.getFrameTime());
        if (cooldown > 0.0F) {
            BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
            colorRect(pose, bufferBuilder, 0, Mth.floor(16.0F * (1.0F - cooldown)), 16, Mth.ceil(16.0F * cooldown), 255, 255, 255, 127);
        }

        pose.popPose();
    }

    /**
     * @author Pandier
     * @reason Fix rendering issues
     */
    @Overwrite(remap = false)
    public static void drawItemStack(PoseStack pose, ItemStack stack, float alpha) {
        if (stack.isEmpty()) return;
        Minecraft mc = Minecraft.getInstance();
        BakedModel bakedmodel = mc.getItemRenderer().getModel(stack, null, null, 0);
        pose.pushPose();
        pose.translate(8, 8, 150);

        pose.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        pose.scale(16.0F, 16.0F, 16.0F);
        boolean flag = !bakedmodel.usesBlockLight();
        if (flag) {
            Lighting.setupForFlatItems();
        }

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        mc.getItemRenderer().render(stack, ItemDisplayContext.GUI, false, pose, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, bakedmodel);
        bufferSource.endBatch();

        if (flag) {
            Lighting.setupFor3DItems();
        }
        pose.popPose();
    }
}
