package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.magic.SilenceEntity;

public class magicSilence extends Magic {
	public magicSilence(ResourceLocation registryName, boolean hasToSelect, int maxLevel) {
		super(registryName, hasToSelect, maxLevel, null);
	}

	@Override
	public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {
		float silenceTime = getDamageMult(level);
		silenceTime *= fullMPBlastMult;

		lockOnTarget = getMagicLockOn(level) ? lockOnTarget : null;
		caster.swing(InteractionHand.MAIN_HAND);
		switch (level) {
		case 0:
			ThrowableProjectile silence = new SilenceEntity(player.level(), player, silenceTime, lockOnTarget);
			player.level().addFreshEntity(silence);
			silence.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1F, 0);
			break;
		case 1:
			ThrowableProjectile silencera = new SilenceEntity(player.level(), player, silenceTime, lockOnTarget);
			player.level().addFreshEntity(silencera);
			silencera.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5F, 0);
			break;
		case 2:
			ThrowableProjectile silencega = new SilenceEntity(player.level(), player, silenceTime, lockOnTarget);
			player.level().addFreshEntity(silencega);
			silencega.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			break;
		}
	}

	@Override
	protected void playMagicCastSound(Player player, Player caster, int level) {
		player.level().playSound(null, player.blockPosition(), ModSoundsRM.PLAYER_CAST.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}
