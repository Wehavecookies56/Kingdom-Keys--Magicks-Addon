package online.remind.remind.capabilities;

import net.minecraft.nbt.CompoundTag;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;

public abstract class IWorldCapabilitiesRM implements IWorldCapabilities {
    @Override
    public CompoundTag serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}
