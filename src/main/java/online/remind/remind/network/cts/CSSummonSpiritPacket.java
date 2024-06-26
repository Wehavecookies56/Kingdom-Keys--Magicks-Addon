package online.remind.remind.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.remind.remind.capabilities.IGlobalCapabilitiesRM;
import online.remind.remind.capabilities.ModCapabilitiesRM;
import online.remind.remind.entity.mob.ChirithyEntity;
import online.remind.remind.network.PacketHandlerRM;

import java.util.function.Supplier;

public class CSSummonSpiritPacket {


    public CSSummonSpiritPacket(){}

    public void encode(FriendlyByteBuf buffer) {
    }

    public static CSSummonSpiritPacket decode(FriendlyByteBuf buffer) {
        CSSummonSpiritPacket msg = new CSSummonSpiritPacket();
        return msg;
    }


    public static void handle(final CSSummonSpiritPacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player owner = ctx.get().getSender();

            IGlobalCapabilitiesRM playerData = ModCapabilitiesRM.getGlobal(owner);
            if (playerData == null)

                return;

            if(!playerData.hasDreamEaterSummoned()) {
            ChirithyEntity dreamEater = new ChirithyEntity(owner.level(), owner);
            owner.level().addFreshEntity(dreamEater);
            dreamEater.setPos(owner.getX(), owner.getY() + 2, owner.getZ());
            dreamEater.getUUID();
            playerData.setHasDreamEaterSummoned(true);
            System.out.println(playerData.hasDreamEaterSummoned());
            playerData.setDreamEaterSummonedID(+1);
            PacketHandlerRM.syncGlobalToAllAround(owner, playerData);
            }
            else if (playerData.hasDreamEaterSummoned()){

                playerData.setHasDreamEaterSummoned(false);
                playerData.setDreamEaterSummonedID(-1);
                PacketHandlerRM.syncGlobalToAllAround(owner, playerData);
                System.out.println(playerData.hasDreamEaterSummoned());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
