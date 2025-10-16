package net.inventive_mods.inventive_inventory.features.locked_slots;

import net.inventive_mods.inventive_inventory.util.slots.PlayerSlots;
import net.inventive_mods.inventive_inventory.util.slots.SlotRange;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LockedSlots extends ArrayList<Integer> {
    private LockedSlots(List<Integer> list) {
        super(list);
    }

    public static LockedSlots empty() {
        return new LockedSlots(List.of());
    }

    public LockedSlots adjust() {
        SlotRange playerSlots = PlayerSlots.get();
        if (playerSlots.isEmpty())
            return empty();

        int start = playerSlots.get(0);
        return new LockedSlots(this.stream().map(slot -> slot + start).collect(Collectors.toList()));
    }

    public LockedSlots unadjust() {
        SlotRange playerSlots = PlayerSlots.get();
        if (playerSlots.isEmpty())
            return empty();

        int start = playerSlots.get(0);
        return new LockedSlots(this.stream().map(slot -> slot - start).toList());
    }
}

