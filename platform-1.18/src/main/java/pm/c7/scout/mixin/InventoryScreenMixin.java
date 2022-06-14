package pm.c7.scout.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.SlotType;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pm.c7.scout.item.BaseBagItem;

import java.util.Optional;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {
    private InventoryScreenMixin() {
        super(null, null, null);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"), method = "drawBackground", cancellable = true)
    private void scout$drawSatchelRow(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        if (this.client != null && this.client.player != null) {
            ItemStack backStack = getTrinketSlot(this.client.player, "chest/back");
            if (!backStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) backStack.getItem();
                int slots = bagItem.getSlotCount();

                int x = this.x;
                int y = this.y;
                this.drawTexture(matrices, x, y, 0, 0, this.backgroundWidth, 83);
                y += 83;

                int u = 0;
                int v = 83;

                RenderSystem.setShaderColor(0.0f, 1.0f, 0.0f, 1.0f);

                for (int slot = 0; slot < slots; slot++) {
                    if (slot % 9 == 0) {
                        x = this.x;
                        u = 0;
                        this.drawTexture(matrices, x, y, u, v, 7, 18);
                        x += 7;
                        u += 7;
                    }

                    this.drawTexture(matrices, x, y, u, v, 18, 18);

                    x += 18;
                    u += 18;

                    if ((slot + 1) % 9 == 0) {
                        this.drawTexture(matrices, x, y, u, v, 7, 18);
                        y += 18;
                    }
                }

                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

                x = this.x;
                this.drawTexture(matrices, x, y, 0, v, this.backgroundWidth, this.backgroundHeight - 83);

                callbackInfo.cancel();
            }
        }
    }

    private ItemStack getTrinketSlot(PlayerEntity player, String slot) {
        ItemStack targetStack = ItemStack.EMPTY;

        // TODO: can this be simplified?
        Optional<TrinketComponent> _component = TrinketsApi.getTrinketComponent(player);
        if (_component.isPresent()) {
            TrinketComponent component = _component.get();
            for (Pair<SlotReference, ItemStack> pair : component.getAllEquipped()) {
                SlotReference slotRef = pair.getLeft();

                // TODO: figure out how to handle slots with amounts for pouches

                SlotType slotType = slotRef.inventory().getSlotType();
                String slotGroupAndName = slotType.getGroup() + "/" + slotType.getName();
                if (slotGroupAndName.equals(slot)) {
                    ItemStack slotStack = pair.getRight();
                    if (slotStack.getItem() instanceof BaseBagItem) {
                        targetStack = slotStack;
                        break;
                    }
                }
            }
        }

        return targetStack;
    }
}
