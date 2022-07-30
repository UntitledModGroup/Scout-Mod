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
            client.execute(() -> {
                ScoutPlayerScreenHandler screenHandler = (ScoutPlayerScreenHandler) client.player.playerScreenHandler;

                ItemStack satchelStack = ScoutUtil.findBagItem(client.player, BagType.SATCHEL, false);
                DefaultedList<BagSlot> satchelSlots = screenHandler.scout$getSatchelSlots();

                for (int i = 0; i < Scout.MAX_SATCHEL_SLOTS; i++) {
                    BagSlot slot = satchelSlots.get(i);
                    slot.setInventory(null);
                    slot.setEnabled(false);
                }
                if (!satchelStack.isEmpty()) {
                    BaseBagItem satchelItem = (BaseBagItem) satchelStack.getItem();
                    Inventory satchelInv = satchelItem.getInventory(satchelStack);

                    for (int i = 0; i < satchelItem.getSlotCount(); i++) {
                        BagSlot slot = satchelSlots.get(i);
                        slot.setInventory(satchelInv);
                        slot.setEnabled(true);
                    }
                }

                ItemStack leftPouchStack = ScoutUtil.findBagItem(client.player, BagType.POUCH, false);
                DefaultedList<BagSlot> leftPouchSlots = screenHandler.scout$getLeftPouchSlots();

                for (int i = 0; i < Scout.MAX_POUCH_SLOTS; i++) {
                    BagSlot slot = leftPouchSlots.get(i);
                    slot.setInventory(null);
                    slot.setEnabled(false);
                }
                if (!leftPouchStack.isEmpty()) {
                    BaseBagItem leftPouchItem = (BaseBagItem) leftPouchStack.getItem();
                    Inventory leftPouchInv = leftPouchItem.getInventory(leftPouchStack);

                    for (int i = 0; i < leftPouchItem.getSlotCount(); i++) {
                        BagSlot slot = leftPouchSlots.get(i);
                        slot.setInventory(leftPouchInv);
                        slot.setEnabled(true);
                    }
                }

                ItemStack rightPouchStack = ScoutUtil.findBagItem(client.player, BagType.POUCH, true);
                DefaultedList<BagSlot> rightPouchSlots = screenHandler.scout$getRightPouchSlots();

                for (int i = 0; i < Scout.MAX_POUCH_SLOTS; i++) {
                    BagSlot slot = rightPouchSlots.get(i);
                    slot.setInventory(null);
                    slot.setEnabled(false);
                }
                if (!rightPouchStack.isEmpty()) {
                    BaseBagItem rightPouchItem = (BaseBagItem) rightPouchStack.getItem();
                    Inventory rightPouchInv = rightPouchItem.getInventory(rightPouchStack);

                    for (int i = 0; i < rightPouchItem.getSlotCount(); i++) {
                        BagSlot slot = rightPouchSlots.get(i);
                        slot.setInventory(rightPouchInv);
                        slot.setEnabled(true);
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
