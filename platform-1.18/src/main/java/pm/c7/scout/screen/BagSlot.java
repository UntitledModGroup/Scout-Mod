package pm.c7.scout.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class BagSlot extends Slot {
    private final int index;
    public Inventory inventory;
    private boolean enabled = false;

    public BagSlot(int index, int x, int y) {
        super(null, index, x, y);
        this.index = index;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void setEnabled(boolean state) {
        enabled = state;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return enabled && inventory != null;
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        return enabled && inventory != null;
    }

    @Override
    public boolean isEnabled() {
        return enabled && inventory != null;
    }

    @Override
    public ItemStack getStack() {
        return enabled && this.inventory != null ? this.inventory.getStack(this.index) : ItemStack.EMPTY;
    }

    @Override
    public void setStack(ItemStack stack) {
        if (enabled && this.inventory != null) {
            this.inventory.setStack(this.index, stack);
            this.markDirty();
        }
    }

    @Override
    public void markDirty() {
        if (enabled && this.inventory != null) {
            this.inventory.markDirty();
        }
    }

    @Override
    public ItemStack takeStack(int amount) {
        return enabled && this.inventory != null ? this.inventory.removeStack(this.index, amount) : ItemStack.EMPTY;
    }

    @Override
    public int getMaxItemCount() {
        return enabled && this.inventory != null ? this.inventory.getMaxCountPerStack() : 0;
    }
}
