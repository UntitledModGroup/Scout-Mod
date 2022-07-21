package pm.c7.scout;

import java.util.Optional;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import pm.c7.scout.item.BaseBagItem;
import pm.c7.scout.item.BaseBagItem.BagType;

public class ScoutUtil {
    public static final Identifier SLOT_TEXTURE = new Identifier("scout", "textures/gui/slots.png");

    public static ItemStack findBagItem(PlayerEntity player, BaseBagItem.BagType type, boolean right) {
        ItemStack targetStack = ItemStack.EMPTY;

        boolean hasFirstPouch = false;
        Optional<TrinketComponent> _component = TrinketsApi.getTrinketComponent(player);
        if (_component.isPresent()) {
            TrinketComponent component = _component.get();
            for (Pair<SlotReference, ItemStack> pair : component.getAllEquipped()) {
                ItemStack slotStack = pair.getRight();

                if (slotStack.getItem() instanceof BaseBagItem) {
                    BaseBagItem bagItem = (BaseBagItem) slotStack.getItem();

                    if (bagItem.getType() == type) {
                        if (type == BagType.POUCH) {
                            if (right == true && hasFirstPouch == false) {
                                hasFirstPouch = true;
                                continue;
                            } else {
                                targetStack = slotStack;
                                break;
                            }
                        } else {
                            targetStack = slotStack;
                            break;
                        }
                    }
                }
            }
        }

        return targetStack;
    }

    public static NbtList inventoryToTag(SimpleInventory inventory) {
        NbtList tag = new NbtList();

        for(int i = 0; i < inventory.size(); i++) {
            NbtCompound stackTag = new NbtCompound();
            stackTag.putInt("Slot", i);
            stackTag.put("Stack", inventory.getStack(i).writeNbt(new NbtCompound()));
            tag.add(stackTag);
        }

        return tag;
    }

    public static void inventoryFromTag(NbtList tag, SimpleInventory inventory) {
        inventory.clear();

        tag.forEach(element -> {
            NbtCompound stackTag = (NbtCompound) element;
            int slot = stackTag.getInt("Slot");
            ItemStack stack = ItemStack.fromNbt(stackTag.getCompound("Stack"));
            inventory.setStack(slot, stack);
        });
    }
}
