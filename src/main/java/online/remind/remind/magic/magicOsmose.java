package online.remind.remind.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.remind.remind.client.sound.ModSoundsRM;
import online.remind.remind.entity.magic.OsmoseEntity;

public class magicOsmose extends Magic {

	public magicOsmose(ResourceLocation registryName, boolean hasToSelect, int maxLevel) {
		super(registryName, hasToSelect, maxLevel, null);
	}

	@Override
	public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float mpTaken = getDamageMult(level);
		mpTaken *= fullMPBlastMult;

		lockOnEntity = getMagicLockOn(level) ? lockOnEntity : null;
		caster.swing(InteractionHand.MAIN_HAND);
		switch (level) {
		case 0:
			ThrowableProjectile osmose = new OsmoseEntity(player.level(), player, mpTaken, lockOnEntity);
			player.level().addFreshEntity(osmose);
			osmose.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			break;
		case 1:
			ThrowableProjectile osmosera = new OsmoseEntity(player.level(), player, mpTaken, lockOnEntity);
			player.level().addFreshEntity(osmosera);
			osmosera.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.5F, 0);
			break;
		case 2:
			ThrowableProjectile osmosega = new OsmoseEntity(player.level(), player, mpTaken, lockOnEntity);
			player.level().addFreshEntity(osmosega);
			osmosega.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 3F, 0);
			break;
		}
	}

	@Override
	protected void playMagicCastSound(Player player, Player caster, int level) {
		player.level().playSound(null, player.blockPosition(), ModSoundsRM.OSMOSE.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}
