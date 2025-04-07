package team.creative.littletiles.common.fix.creativecore;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import team.creative.creativecore.common.gui.controls.inventory.GuiPlayerInventoryGrid;

public class GuiPlayerInventoryGridFix extends GuiPlayerInventoryGrid {
    public GuiPlayerInventoryGridFix(Player player) {
        super(player);
    }

    @Override
    public ItemStack moveInside(ItemStack toAdd, int slot) {
        if (slot < 9) {
            this.insertClever(toAdd, 9, 36);
        } else {
            this.insertClever(toAdd, 0, 9);
        }

        return toAdd;
    }
}
