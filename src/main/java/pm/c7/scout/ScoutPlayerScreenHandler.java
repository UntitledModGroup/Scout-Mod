package pm.c7.scout;

import net.minecraft.util.collection.DefaultedList;
import pm.c7.scout.screen.BagSlot;

public interface ScoutPlayerScreenHandler {
    DefaultedList<BagSlot> scout$getSatchelSlots();
    DefaultedList<BagSlot> scout$getLeftPouchSlots();
    DefaultedList<BagSlot> scout$getRightPouchSlots();
}
