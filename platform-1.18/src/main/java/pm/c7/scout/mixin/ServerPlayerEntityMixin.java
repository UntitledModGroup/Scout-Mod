package pm.c7.scout.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.GameRules;
import pm.c7.scout.ScoutNetworking;
import pm.c7.scout.ScoutPlayerScreenHandler;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.item.BaseBagItem;
import pm.c7.scout.screen.BagSlot;

@SuppressWarnings("deprecation")
@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "onDeath", at = @At("HEAD"))
    private void scout$attemptFixGraveMods(DamageSource source, CallbackInfo callbackInfo) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        ScoutPlayerScreenHandler handler = (ScoutPlayerScreenHandler) player.playerScreenHandler;

        if (!player.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            ItemStack backStack = ScoutUtil.getTrinketSlot(player, "chest/back", 0);
            if (!backStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) backStack.getItem();
                int slots = bagItem.getSlotCount();

                DefaultedList<BagSlot> bagSlots = handler.scout$getSatchelSlots();

                for (int i = 0; i < slots; i++) {
                    BagSlot slot = bagSlots.get(i);
                    slot.setInventory(null);
                    slot.setEnabled(false);
                }

                PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
                packet.writeBoolean(false);
                packet.writeInt(0);
                packet.writeItemStack(backStack);

                ServerPlayNetworking.send(player, ScoutNetworking.ENABLE_SLOTS, packet);
            }

            ItemStack leftPouchStack = ScoutUtil.getTrinketSlot(player, "legs/pouch", 0);
            if (!leftPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
                int slots = bagItem.getSlotCount();

                DefaultedList<BagSlot> bagSlots = handler.scout$getLeftPouchSlots();

                for (int i = 0; i < slots; i++) {
                    BagSlot slot = bagSlots.get(i);
                    slot.setInventory(null);
                    slot.setEnabled(false);
                }

                PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
                packet.writeBoolean(false);
                packet.writeInt(0);
                packet.writeItemStack(leftPouchStack);

                ServerPlayNetworking.send(player, ScoutNetworking.ENABLE_SLOTS, packet);
            }

            ItemStack rightPouchStack = ScoutUtil.getTrinketSlot(player, "legs/pouch", 1);
            if (!rightPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) rightPouchStack.getItem();
                int slots = bagItem.getSlotCount();

                DefaultedList<BagSlot> bagSlots = handler.scout$getRightPouchSlots();

                for (int i = 0; i < slots; i++) {
                    BagSlot slot = bagSlots.get(i);
                    slot.setInventory(null);
                    slot.setEnabled(false);
                }

                PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
                packet.writeBoolean(false);
                packet.writeInt(1);
                packet.writeItemStack(rightPouchStack);

                ServerPlayNetworking.send(player, ScoutNetworking.ENABLE_SLOTS, packet);
            }
        }
    }
}
