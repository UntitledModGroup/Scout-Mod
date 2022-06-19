package pm.c7.scout.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.google.common.collect.Table;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @Redirect(method = "copySharedSlots", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Table;put(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
    private Object scout$skipNullInventories1(Table<Inventory, Integer, Integer> self, Object inventory, Object index, Object size) {
        if (inventory == null) {
            return null;
        }
        return self.put((Inventory) inventory, (int) index, (int) size);
    }
    @Redirect(method = "copySharedSlots", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Table;get(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", remap = false))
    private Object scout$skipNullInventories2(Table<Inventory, Integer, Integer> self, Object inventory, Object index) {
        if (inventory == null) {
            return null;
        }
        return self.get(inventory, (int) index);
    }
}
