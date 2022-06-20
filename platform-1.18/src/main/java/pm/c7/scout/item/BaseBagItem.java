package pm.c7.scout.item;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import pm.c7.scout.Scout;
import pm.c7.scout.ScoutNetworking;
import pm.c7.scout.ScoutPlayerScreenHandler;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.screen.BagSlot;

@SuppressWarnings("deprecation")
public class BaseBagItem extends TrinketItem {
    private static final String ITEMS_KEY = "Items";

    private final int slots;
    private final BagType type;

    public BaseBagItem(Settings settings, int slots, BagType type) {
        super(settings);

        if (type == BagType.SATCHEL && slots > Scout.MAX_SATCHEL_SLOTS) {
            throw new IllegalArgumentException("Satchel has too many slots.");
        }
        if (type == BagType.POUCH && slots > Scout.MAX_POUCH_SLOTS) {
            throw new IllegalArgumentException("Pouch has too many slots.");
        }

        this.slots = slots;
        this.type = type;
    }

    public int getSlotCount() {
        return this.slots;
    }

    public BagType getType() {
        return this.type;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new TranslatableText("tooltip.scout.slots", new LiteralText(String.valueOf(this.slots)).formatted(Formatting.BLUE)).formatted(Formatting.GRAY));
    }

    public Inventory getInventory(ItemStack stack) {
        SimpleInventory inventory = new SimpleInventory(this.slots) {
            @Override
            public void markDirty() {
                stack.getOrCreateNbt().put(ITEMS_KEY, ScoutUtil.inventoryToTag(this));
                super.markDirty();
            }
        };

        NbtCompound compound = stack.getOrCreateNbt();
        if (!compound.contains(ITEMS_KEY)) {
            compound.put(ITEMS_KEY, new NbtList());
        }

        NbtList items = compound.getList(ITEMS_KEY, 10);

        ScoutUtil.inventoryFromTag(items, inventory);

        return inventory;
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        DefaultedList<ItemStack> stacks = DefaultedList.of();
        Inventory inventory = getInventory(stack);

        for (int i = 0; i < slots; i++) {
            stacks.add(inventory.getStack(i));
        }

        if (stacks.stream().allMatch(ItemStack::isEmpty)) return Optional.empty();

        return Optional.of(new BagTooltipData(stacks, slots));
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slotRef, LivingEntity entity) {
        PlayerEntity player = (PlayerEntity) entity;
        ScoutPlayerScreenHandler handler = (ScoutPlayerScreenHandler) player.playerScreenHandler;

        int slotIndex = slotRef.index();

        Inventory inventory = getInventory(stack);

        DefaultedList<BagSlot> bagSlots = DefaultedList.of();

        if (type == BagType.SATCHEL) {
            bagSlots = handler.scout$getSatchelSlots();
        } else if (type == BagType.POUCH) {
            if (slotIndex == 0) {
                bagSlots = handler.scout$getLeftPouchSlots();
            } else if (slotIndex == 1) {
                bagSlots = handler.scout$getRightPouchSlots();
            }
        }

        for (int i = 0; i < slots; i++) {
            BagSlot slot = bagSlots.get(i);
            slot.setInventory(inventory);
            slot.setEnabled(true);
        }

        PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
        packet.writeBoolean(true);
        packet.writeInt(slotIndex);
        packet.writeItemStack(stack);

        if (entity instanceof ServerPlayerEntity serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, ScoutNetworking.ENABLE_SLOTS, packet);
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slotRef, LivingEntity entity) {
        PlayerEntity player = (PlayerEntity) entity;
        ScoutPlayerScreenHandler handler = (ScoutPlayerScreenHandler) player.playerScreenHandler;

        int slotIndex = slotRef.index();

        DefaultedList<BagSlot> bagSlots = DefaultedList.of();

        if (type == BagType.SATCHEL) {
            bagSlots = handler.scout$getSatchelSlots();
        } else if (type == BagType.POUCH) {
            if (slotIndex == 0) {
                bagSlots = handler.scout$getLeftPouchSlots();
            } else if (slotIndex == 1) {
                bagSlots = handler.scout$getRightPouchSlots();
            }
        }

        for (int i = 0; i < slots; i++) {
            BagSlot slot = bagSlots.get(i);
            slot.setInventory(null);
            slot.setEnabled(false);
        }

        PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
        packet.writeBoolean(false);
        packet.writeInt(slotIndex);
        packet.writeItemStack(stack);

        if (entity instanceof ServerPlayerEntity serverPlayer) {
            ServerPlayNetworking.send(serverPlayer, ScoutNetworking.ENABLE_SLOTS, packet);
        }
    }

    public enum BagType {
        SATCHEL,
        POUCH
    }
}
