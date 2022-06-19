package pm.c7.scout.compat;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.mixin.accessor.HandledScreenAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.item.BaseBagItem;

public class ScoutEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        registry.addExclusionArea(InventoryScreen.class, (screen, consumer) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ItemStack leftPouchStack = ScoutUtil.getTrinketSlot(client.player, "legs/pouch", 0);
            if (!leftPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);

                int x = ((HandledScreenAccessor) screen).getX() - (columns * 18);
                int y = ((HandledScreenAccessor) screen).getY() + 76;

                consumer.accept(new Bounds(x, y, columns * 18, 68));
            }

            ItemStack rightPouchStack = ScoutUtil.getTrinketSlot(client.player, "legs/pouch", 1);
            if (!rightPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) rightPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);

                int x = ((HandledScreenAccessor) screen).getX() + 176;
                int y = ((HandledScreenAccessor) screen).getY() + 76;

                consumer.accept(new Bounds(x, y, columns * 18, 68));
            }
        });
    }

}
