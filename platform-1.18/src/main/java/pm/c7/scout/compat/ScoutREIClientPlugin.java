package pm.c7.scout.compat;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.screen.ExclusionZones;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.item.BaseBagItem;
import pm.c7.scout.mixin.HandledScreenAccessor;

public class ScoutREIClientPlugin implements REIClientPlugin {
    @Override
    public void registerScreens(ScreenRegistry registry) {
        ExclusionZones zones = registry.exclusionZones();
        zones.register(InventoryScreen.class, screen -> {
            List<Rectangle> bounds = new ArrayList<>();

            MinecraftClient client = MinecraftClient.getInstance();
            ItemStack leftPouchStack = ScoutUtil.getTrinketSlot(client.player, "legs/pouch", 0);
            if (!leftPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);

                int x = ((HandledScreenAccessor) screen).getX() - (columns * 18);
                int y = ((HandledScreenAccessor) screen).getY() + 76;

                bounds.add(new Rectangle(x, y, columns * 18, 68));
            }

            ItemStack rightPouchStack = ScoutUtil.getTrinketSlot(client.player, "legs/pouch", 1);
            if (!rightPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) rightPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);

                int x = ((HandledScreenAccessor) screen).getX() + 176;
                int y = ((HandledScreenAccessor) screen).getY() + 76;

                bounds.add(new Rectangle(x, y, columns * 18, 68));
            }

            return bounds;
        });
    }
}
