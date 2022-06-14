package pm.c7.scout.item;

import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BaseBagItem extends TrinketItem {
    private int slots;

    public BaseBagItem(Settings settings, int slots) {
        super(settings);
        this.slots = slots;
    }

    public int getSlotCount() {
        return this.slots;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new TranslatableText("tooltip.scout.slots", new LiteralText(String.valueOf(this.slots)).formatted(Formatting.BLUE)).formatted(Formatting.GRAY));
    }
}
