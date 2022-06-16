package pm.c7.scout.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V"), method = "drawBackground")
    private void scout$drawSatchelRow(InventoryScreen that, MatrixStack matrices, int _x, int _y, int _u, int _v, int w, int h) {
        if (this.client != null && this.client.player != null) {
            ItemStack backStack = ScoutUtil.getTrinketSlot(this.client.player, "chest/back", 0);
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
            } else {
                this.drawTexture(matrices, _x, _y, _u, _v, w, h);
            }
        } else {
            this.drawTexture(matrices, _x, _y, _u, _v, w, h);
        }
    }

    @Inject(at = @At("RETURN"), method = "drawBackground")
    private void scout$drawPouchSlots(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        if (this.client != null && this.client.player != null) {
            int pouchRows = 0;
            ItemStack backStack = ScoutUtil.getTrinketSlot(this.client.player, "chest/back", 0);
            if (!backStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) backStack.getItem();
                int slots = bagItem.getSlotCount();
                pouchRows = (int) Math.ceil(slots / 9);
            }

            ItemStack leftPouchStack = ScoutUtil.getTrinketSlot(this.client.player, "legs/pouch", 0);
            if (!leftPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);

                int x = this.x;
                int y = this.y;

                y += 137;

                if (pouchRows > 0)
                    y += pouchRows * 18;

                RenderSystem.setShaderTexture(0, ScoutUtil.SLOT_TEXTURE);
                RenderSystem.setShaderColor(0.0f, 0.0f, 1.0f, 1.0f);

                this.drawTexture(matrices, x, y, 18, 25, 7, 7);
                for (int i = 0; i < columns; i++) {
                    x -= 11;
                    this.drawTexture(matrices, x, y, 7, 25, 11, 7);
                }
                if (columns > 1) {
                    for (int i = 0; i < columns - 1; i++) {
                        x -= 7;
                        this.drawTexture(matrices, x, y, 7, 25, 7, 7);
                    }
                }
                x -= 7;
                this.drawTexture(matrices, x, y, 0, 25, 7, 7);

                x = this.x + 7;
                y -= 54;
                for (int slot = 0; slot < slots; slot++) {
                    if (slot % 3 == 0) {
                        x -= 18;
                        y += 54;
                    }
                    y -= 18;
                    this.drawTexture(matrices, x, y, 7, 7, 18, 18);
                }

                x -= 7;
                y += 54;
                for (int i = 0; i < 3; i++) {
                    y -= 18;
                    this.drawTexture(matrices, x, y, 0, 7, 7, 18);
                }

                x = this.x;
                y -= 7;
                this.drawTexture(matrices, x, y, 18, 0, 7, 7);
                for (int i = 0; i < columns; i++) {
                    x -= 11;
                    this.drawTexture(matrices, x, y, 7, 0, 11, 7);
                }
                if (columns > 1) {
                    for (int i = 0; i < columns - 1; i++) {
                        x -= 7;
                        this.drawTexture(matrices, x, y, 7, 0, 7, 7);
                    }
                }
                x -= 7;
                this.drawTexture(matrices, x, y, 0, 0, 7, 7);

                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            }

            ItemStack rightPouchStack = ScoutUtil.getTrinketSlot(this.client.player, "legs/pouch", 1);
            if (!rightPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) rightPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);

                int x = this.x;
                int y = this.y;

                x += this.backgroundWidth - 7;
                y += 137;

                if (pouchRows > 0)
                    y += pouchRows * 18;

                RenderSystem.setShaderTexture(0, ScoutUtil.SLOT_TEXTURE);
                RenderSystem.setShaderColor(0.0f, 0.0f, 1.0f, 1.0f);

                this.drawTexture(matrices, x, y, 25, 25, 7, 7);
                x += 7;
                for (int i = 0; i < columns; i++) {
                    this.drawTexture(matrices, x, y, 7, 25, 11, 7);
                    x += 11;
                }
                if (columns > 1) {
                    for (int i = 0; i < columns - 1; i++) {
                        this.drawTexture(matrices, x, y, 7, 25, 7, 7);
                        x += 7;
                    }
                }
                this.drawTexture(matrices, x, y, 32, 25, 7, 7);

                x = this.x + this.backgroundWidth - 25;
                y -= 54;
                for (int slot = 0; slot < slots; slot++) {
                    if (slot % 3 == 0) {
                        x += 18;
                        y += 54;
                    }
                    y -= 18;
                    this.drawTexture(matrices, x, y, 7, 7, 18, 18);
                }

                x += 18;
                y += 54;
                for (int i = 0; i < 3; i++) {
                    y -= 18;
                    this.drawTexture(matrices, x, y, 32, 7, 7, 18);
                }

                x = this.x + this.backgroundWidth - 7;
                y -= 7;
                this.drawTexture(matrices, x, y, 25, 0, 7, 7);
                x += 7;
                for (int i = 0; i < columns; i++) {
                    this.drawTexture(matrices, x, y, 7, 0, 11, 7);
                    x += 11;
                }
                if (columns > 1) {
                    for (int i = 0; i < columns - 1; i++) {
                        this.drawTexture(matrices, x, y, 7, 0, 7, 7);
                        x += 7;
                    }
                }
                this.drawTexture(matrices, x, y, 32, 0, 7, 7);

                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }
}
