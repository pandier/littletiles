package team.creative.littletiles.common.gui.controls;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import team.creative.creativecore.client.render.GuiRenderHelper;
import team.creative.creativecore.common.gui.GuiChildControl;
import team.creative.creativecore.common.gui.controls.simple.GuiProgressbar;
import team.creative.creativecore.common.gui.style.ControlFormatting;
import team.creative.creativecore.common.util.math.geo.Rect;
import team.creative.creativecore.common.util.text.TextBuilder;
import team.creative.creativecore.common.util.type.Color;

public class GuiColorProgressBar extends GuiProgressbar {
    
    public Color color;
    
    public GuiColorProgressBar(String name, double pos, double max, Color color) {
        super(name, pos, max, (v, pMax) -> mapToInt(v, pMax, 100) + "%");
        this.color = color;
    }

    protected static int mapToInt(double value, double max, double mapped) {
        return mapToInt(value / max, mapped);
    }

    // We want to return at least 1 when the progress is even a little bit above 0.0
    protected static int mapToInt(double progress, double mapped) {
        if (progress == 0.0) return 0;
        return (int) (progress * (mapped - 1) + 1.0);
    }

    @Override
    public ControlFormatting getControlFormatting() {
        return ControlFormatting.NESTED_NO_PADDING;
    }
    
    @Override
    public List<Component> getTooltip() {
        List<Component> tooltip = new TextBuilder()
                .number(this.pos, true)
                .text("/")
                .number(this.max, true)
                .text(" (")
                .text(parser.parse(this.pos, this.max))
                .text(")")
                .build();
        if (tooltip != null)
            tooltip.add(Component.translatable("gui.color.rightclick"));
        return tooltip;
    }
    
    @Override
    public boolean mouseClicked(Rect rect, double x, double y, int button) {
        if (button == 1) {
            playSound(SoundEvents.UI_BUTTON_CLICK);
            return true;
        }
        return false;
    }
    
    @Override
    protected void renderProgress(PoseStack pose, GuiChildControl control, Rect rect, double percent) {
        GuiRenderHelper.colorRect(pose, 0, 0, mapToInt(percent, rect.getWidth()), (int) (rect.getHeight()), color.toInt());
    }
}
