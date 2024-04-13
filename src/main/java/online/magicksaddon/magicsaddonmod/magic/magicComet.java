package online.magicksaddon.magicsaddonmod.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.magicksaddon.magicsaddonmod.client.sound.ModSoundsRM;
import online.magicksaddon.magicsaddonmod.entity.magic.CometEntity;
import online.magicksaddon.magicsaddonmod.lib.StringsRM;

public class magicComet extends Magic {

	public magicComet(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
		super(registryName, hasToSelect, maxLevel, gmAbility);
	}

	@Override
	public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {
		float dmgMult = getDamageMult(level) + ModCapabilities.getPlayer(player).getNumberOfAbilitiesEquipped(StringsRM.darknessBoost) * 0.2F;
		dmgMult *= fullMPBlastMult;

		switch (level) {
		case 0:
			// Comet
			ThrowableProjectile comet = new CometEntity(player.level(), player, dmgMult, 2, 0);
			comet.setOwner(caster);
			comet.setPos(player.getX(), player.getY() + 1.8F, player.getZ());
			player.level().addFreshEntity(comet);
			comet.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5F, 0);
			break;
		case 1:
			// Meteor
			for (int i = -3; i <= 3; i++) {
				ThrowableProjectile meteor = new CometEntity(player.level(), player, dmgMult, 2, i);
				meteor.setOwner(caster);
				meteor.setPos(player.getX() + (Math.random() * 10) - 5, player.getY() + (Math.random() * i) + 2, player.getZ() + (Math.random() * 10) - 5);
				player.level().addFreshEntity(meteor);
				meteor.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5F, 0);
			}
			break;
		}

	}

	@Override
	protected void playMagicCastSound(Player player, Player caster, int level) {
		player.level().playSound(null, player.blockPosition(), ModSoundsRM.PLAYER_CAST.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}
