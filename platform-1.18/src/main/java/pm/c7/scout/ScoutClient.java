package pm.c7.scout;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import pm.c7.scout.gui.BagTooltipComponent;
import pm.c7.scout.item.BagTooltipData;
import pm.c7.scout.item.BaseBagItem;
import pm.c7.scout.item.BaseBagItem.BagType;
import pm.c7.scout.screen.BagSlot;

@SuppressWarnings("deprecation")
public class ScoutClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ScoutNetworking.ENABLE_SLOTS, (client, handler, packet, sender) -> {
            boolean enable = packet.readBoolean();
            int slotIndex = packet.readInt();
            ItemStack bagStack = packet.readItemStack();

            BaseBagItem bagItem = (BaseBagItem) bagStack.getItem();
            BagType type = bagItem.getType();
            int slots = bagItem.getSlotCount();

            client.execute(() -> {
                ScoutPlayerScreenHandler screenHandler = (ScoutPlayerScreenHandler) client.player.playerScreenHandler;

                Inventory inventory = bagItem.getInventory(bagStack);

                DefaultedList<BagSlot> bagSlots = DefaultedList.of();

                if (type == BagType.SATCHEL) {
                    bagSlots = screenHandler.scout$getSatchelSlots();
                } else if (type == BagType.POUCH) {
                    if (slotIndex == 0) {
                        bagSlots = screenHandler.scout$getLeftPouchSlots();
                    } else if (slotIndex == 1) {
                        bagSlots = screenHandler.scout$getRightPouchSlots();
                    }
                }

                for (int i = 0; i < slots; i++) {
                    BagSlot slot = bagSlots.get(i);
                    if (enable) {
                        slot.setInventory(inventory);
                        slot.setEnabled(true);
                    } else {
                        slot.setInventory(null);
                        slot.setEnabled(false);
                    }
                }
            });
        });

        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof BagTooltipData d) {
                return new BagTooltipComponent(d);
            }

            return null;
        });
    }
}
