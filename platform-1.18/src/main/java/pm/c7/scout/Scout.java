package pm.c7.scout;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import pm.c7.scout.item.BaseBagItem;

public class Scout implements ModInitializer {
    public static final String MOD_ID = "scout";

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier("scout", "itemgroup"), () -> new ItemStack(Scout.SATCHEL));

    public static final BaseBagItem SATCHEL = new BaseBagItem(new FabricItemSettings().group(Scout.ITEM_GROUP).maxCount(1), 9);
    public static final BaseBagItem UPGRADED_SATCHEL = new BaseBagItem(new FabricItemSettings().group(Scout.ITEM_GROUP).maxCount(1).rarity(Rarity.RARE), 18);
    public static final BaseBagItem POUCH = new BaseBagItem(new FabricItemSettings().group(Scout.ITEM_GROUP).maxCount(1), 3);
    public static final BaseBagItem UPGRADED_POUCH = new BaseBagItem(new FabricItemSettings().group(Scout.ITEM_GROUP).maxCount(1).rarity(Rarity.RARE), 6);

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "satchel"), SATCHEL);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "upgraded_satchel"), UPGRADED_SATCHEL);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "pouch"), POUCH);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "upgraded_pouch"), UPGRADED_POUCH);
    }
}
