package online.magicksaddon.magicsaddonmod.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.magicksaddon.magicsaddonmod.client.sound.ModSoundsRM;
import online.magicksaddon.magicsaddonmod.entity.magic.OsmoseEntity;
import online.magicksaddon.magicsaddonmod.entity.magic.SilenceEntity;

public class magicSilence extends Magic {
    public magicSilence(ResourceLocation registryName, boolean hasToSelect, int maxLevel) {
        super(registryName, hasToSelect, maxLevel, null);
    }

    @Override
    protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {
        float silenceTime = getDamageMult(level);
        silenceTime *= fullMPBlastMult;

        lockOnTarget = getMagicLockOn(level) ? lockOnTarget : null;
        caster.swing(InteractionHand.MAIN_HAND);
        //player.level().playSound(null, player.blockPosition(), ModSoundsRM.SILENCE.get(), SoundSource.PLAYERS, 1F, 1F);
        switch(level) {
            case 0:
                ThrowableProjectile silence = new SilenceEntity(player.level(), player, silenceTime,lockOnTarget);
                player.level().addFreshEntity(silence);
                silence.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
                break;
            case 1:
                ThrowableProjectile silencera = new SilenceEntity(player.level(), player, silenceTime,lockOnTarget);
                player.level().addFreshEntity(silencera);
                silencera.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.5F, 0);
                break;
            case 2:
                ThrowableProjectile silencega = new SilenceEntity(player.level(), player, silenceTime,lockOnTarget);
                player.level().addFreshEntity(silencega);
                silencega.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 3F, 0);
                break;
        }
    }

}