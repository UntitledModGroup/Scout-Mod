package pm.c7.scout.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.item.BaseBagItem;
import pm.c7.scout.item.BaseBagItem.BagType;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {
    private InventoryScreenMixin() {
        super(null, null, null);
    }

    @Inject(method = "drawBackground", at = @At("HEAD"))
    private void scout$drawSatchelRow(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        if (this.client != null && this.client.player != null) {
            ItemStack backStack = ScoutUtil.findBagItem(this.client.player, BagType.SATCHEL, false);
            if (!backStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) backStack.getItem();
                int slots = bagItem.getSlotCount();

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderTexture(0, ScoutUtil.SLOT_TEXTURE);
                RenderSystem.setShaderColor(149.0f / 255.0f, 94.0f / 255.0f, 59.0f / 255.0f, 1.0f);

                int x = this.x;
                int y = this.y + this.backgroundHeight - 3;
                this.drawTexture(matrices, x, y, 0, 32, this.backgroundWidth, 4);
                y += 4;

                int u = 0;
                int v = 36;

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

                x = this.x;
                this.drawTexture(matrices, x, y, 0, 54, this.backgroundWidth, 7);
            }
        }
    }

    @Inject(method = "drawBackground", at = @At("RETURN"))
    private void scout$drawPouchSlots(MatrixStack matrices, float delta, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        if (this.client != null && this.client.player != null) {
            ItemStack leftPouchStack = ScoutUtil.findBagItem(this.client.player, BagType.POUCH, false);
            if (!leftPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);

                int x = this.x;
                int y = this.y;

                y += 137;

                RenderSystem.setShaderTexture(0, ScoutUtil.SLOT_TEXTURE);
                RenderSystem.setShaderColor(215.0f / 255.0f, 107.0f / 255.0f, 67.0f / 255.0f, 1.0f);

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

            ItemStack rightPouchStack = ScoutUtil.findBagItem(this.client.player, BagType.POUCH, true);
            if (!rightPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) rightPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);

                int x = this.x;
                int y = this.y;

                x += this.backgroundWidth - 7;
                y += 137;

                RenderSystem.setShaderTexture(0, ScoutUtil.SLOT_TEXTURE);
                RenderSystem.setShaderColor(215.0f / 255.0f, 107.0f / 255.0f, 67.0f / 255.0f, 1.0f);

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

    @Inject(method = "isClickOutsideBounds", at = @At("TAIL"), cancellable = true)
    private void scout$adjustOutsideBounds(double mouseX, double mouseY, int left, int top, int button, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (this.client != null && this.client.player != null) {
            ItemStack backStack = ScoutUtil.findBagItem(this.client.player, BagType.SATCHEL, false);
            if (!backStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) backStack.getItem();
                int slots = bagItem.getSlotCount();
                int rows = (int) Math.ceil(slots / 9);

                if (mouseY < (top + this.backgroundHeight) + 8 + (18 * rows) && mouseY >= (top + this.backgroundHeight) && mouseX >= left && mouseY < (left + this.backgroundWidth)) {
                    callbackInfo.setReturnValue(false);
                }
            }

            ItemStack leftPouchStack = ScoutUtil.findBagItem(this.client.player, BagType.POUCH, false);
            if (!leftPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);

                if (mouseX >= left - (columns * 18) && mouseX < left && mouseY >= (top + this.backgroundHeight) - 90 && mouseY < (top + this.backgroundHeight) - 22) {
                    callbackInfo.setReturnValue(false);
                }
            }

            ItemStack rightPouchStack = ScoutUtil.findBagItem(this.client.player, BagType.POUCH, true);
            if (!rightPouchStack.isEmpty()) {
                BaseBagItem bagItem = (BaseBagItem) rightPouchStack.getItem();
                int slots = bagItem.getSlotCount();
                int columns = (int) Math.ceil(slots / 3);

                if (mouseX >= (left + this.backgroundWidth) && mouseX < (left + this.backgroundWidth) + (columns * 18) && mouseY >= (top + this.backgroundHeight) - 90 && mouseY < (top + this.backgroundHeight) - 22) {
                    callbackInfo.setReturnValue(false);
                }
            }
        }
    }
}
