package pm.c7.scout.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.ItemStack;
import pm.c7.scout.item.BaseBagItem;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    // Trinkets calls isItemEqual to check whether it should unequip old and equip new (https://github.com/emilyploszaj/trinkets/blob/37ee13d6/src/main/java/dev/emi/trinkets/mixin/LivingEntityMixin.java#L196-L199)
    // Excluding ourselves from this check to force unequip/equip when switching bag items fixes a duplication bug (GH-12)
    // Gross and hacky but oh well, can't mixin mixins.
    @Unique
    private void scout$grossTrinketsEquipFix(ItemStack newStack, CallbackInfoReturnable<Boolean> callbackInfo) {
        ItemStack self = (ItemStack) (Object) this;
        if (self.getItem() instanceof BaseBagItem && newStack.getItem() instanceof BaseBagItem) {
            callbackInfo.setReturnValue(false);
        }
    }
}
