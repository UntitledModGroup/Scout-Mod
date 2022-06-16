package pm.c7.scout.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;

import pm.c7.scout.ScoutUtil;
import pm.c7.scout.item.BaseBagItem;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {
    private InventoryScreenMixin() {
        super(null, null, null);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"), method = "drawBackground", cancellable = true)
    private void scout$drawSatchelRow(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        if (this.client != null && this.client.player != null) {
            ItemStack backStack = ScoutUtil.getTrinketSlot(this.client.player, "chest/back");
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
    
    @Inject(at = @At("RETURN"), method = "drawBackground")
    private void scout$drawPouchSlots() {
        
    }
}
