package team.creative.littletiles.mixin.client.creativecore;

import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import team.creative.creativecore.common.gui.controls.inventory.GuiSlotBase;
import team.creative.creativecore.common.gui.style.display.DisplayColor;

@Mixin(GuiSlotBase.class)
public class GuiSlotBaseMixin {

    @Redirect(method = "renderContent", at = @At(value = "INVOKE", target = "Lteam/creative/creativecore/common/gui/style/display/DisplayColor;render(Lcom/mojang/blaze3d/vertex/PoseStack;DD)V"), remap = false)
    private void renderHover(DisplayColor instance, PoseStack poseStack, double width, double height) {
        instance.render(poseStack, 1.0, 1.0, width - 2.0, height - 2.0);
    }
}
