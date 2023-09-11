package online.magicksaddon.magicsaddonmod.entity.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnetEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.magicksaddon.magicsaddonmod.entity.ModEntitiesMA;

import java.util.List;

public class HolyEntity extends ThrowableProjectile {

    int maxTicks = 100;
    Player player;
    String caster;
    float dmgMult = 1;

    public HolyEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
        super(type, world);
        this.blocksBuilding = true;
    }

    public HolyEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        super(ModEntitiesMA.TYPE_HOLY.get(), world);
    }

    public HolyEntity(Level world) {
        super(ModEntitiesMA.TYPE_HOLY.get(), world);
        this.blocksBuilding = true;
    }

    public HolyEntity(Level world, Player player, float dmgMult) {
        super(ModEntitiesMA.TYPE_HOLY.get(), player, world);
        this.player = player;
        this.dmgMult = dmgMult;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return (Packet<ClientGamePacketListener>) NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected float getGravity() {
        return 0F;
    }

    double a = 0;

    @Override
    public void tick() {
        for (Player playerFromList : level.players()) {
            if(playerFromList.getDisplayName().getString().equals(getCaster())) {
                player = playerFromList;
                break;
            }
        }

        if(player == null)
            return;

        if (this.tickCount > maxTicks || player == null) {
            this.remove(RemovalReason.KILLED);
        }

        if(tickCount <= 1) {
            this.setDeltaMovement(0, 0, 0);

        } else if (tickCount < 50) { //Shield
            setPos(player.getX(), getY(), player.getZ());
            double radius = 1D;
            double cx = getX();
            double cy = getY();
            double cz = getZ();

            a+=40; //Speed and distance between particles
            double x = cx + (radius * Math.cos(Math.toRadians(a)));
            double z = cz + (radius * Math.sin(Math.toRadians(a)));

            double x2 = cx + (radius * Math.cos(Math.toRadians(-a)));
            double z2 = cz + (radius * Math.sin(Math.toRadians(-a)));

            if(!level.isClientSide) {
                ((ServerLevel) level).sendParticles(ParticleTypes.DRAGON_BREATH, x,  (cy+0.5) - a / 1080D, z, 1, 0,0,0, 0.5);
                ((ServerLevel) level).sendParticles(ParticleTypes.WHITE_ASH, x2, (cy+0.5) - a / 1080D, z2, 1, 0,0,0, 0.5);
            }

            List<Entity> list = this.level.getEntities(player, player.getBoundingBox().inflate(radius), Entity::isAlive);
            if (!list.isEmpty() && list.get(0) != this) {
                float baseDmg = DamageCalculation.getMagicDamage((Player) this.getOwner()) * 0.3F;
                float dmg = this.getOwner() instanceof Player ? baseDmg : 2;
                for (int i = 0; i < list.size(); i++) {
                    Entity e = (Entity) list.get(i);
                    if (e instanceof LivingEntity) {
                        e.hurt(DamageSource.thrown(this, (Player) this.getOwner()), dmg);
                    }
                }
            }

        } else { //Projectile
            shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
            player.level.playSound(null, player.blockPosition(), SoundEvents.BEACON_AMBIENT, SoundSource.PLAYERS, 1F, 1F);

            hurtMarked = true;
            float radius = 0.2F;
            for (int t = 1; t < 360; t += 30) {
                for (int s = 1; s < 360 ; s += 30) {
                    double x = getX() + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
                    double z = getZ() + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
                    double y = getY() + (radius * Math.cos(Math.toRadians(t)));
                    if(!level.isClientSide)
                        ((ServerLevel) level).sendParticles(ParticleTypes.WHITE_ASH, x, y, z, 1, 0,0,0, 0.5);
                }
            }

        }

        super.tick();
    }

    @Override
    protected void onHit(HitResult rtRes) {
        if (!level.isClientSide) {

            EntityHitResult ertResult = null;
            BlockHitResult brtResult = null;

            if (rtRes instanceof EntityHitResult) {
                ertResult = (EntityHitResult) rtRes;
            }

            if (rtRes instanceof BlockHitResult) {
                brtResult = (BlockHitResult) rtRes;
            }

            if (ertResult != null && ertResult.getEntity() != null && ertResult.getEntity() instanceof LivingEntity) {

                LivingEntity target = (LivingEntity) ertResult.getEntity();

                if (target.isOnFire()) {
                    target.clearFire();
                } else {
                    if (target != getOwner()) {
                        Party p = null;
                        if (getOwner() != null) {
                            p = ModCapabilities.getWorld(getOwner().level).getPartyFromMember(getOwner().getUUID());
                        }
                        if(p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { //If caster is not in a party || the party doesn't have the target in it || the party has FF on
                            float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) * 0.3F : 2;
                            target.hurt(DamageSource.thrown(this, this.getOwner()), dmg * dmgMult);
                            remove(RemovalReason.KILLED);
                        }
                    }
                }
            } else { // Block (not ERTR)
                remove(RemovalReason.KILLED);
            }
        }
    }


    public int getMaxTicks() {
        return maxTicks;
    }

    public void setMaxTicks(int maxTicks) {
        this.maxTicks = maxTicks;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putString("caster", this.getCaster());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.setCaster(compound.getString("caster"));
    }

    private static final EntityDataAccessor<String> CASTER = SynchedEntityData.defineId(MagnetEntity.class, EntityDataSerializers.STRING);

    public String getCaster() {
        return caster;
    }

    public void setCaster(String name) {
        this.entityData.set(CASTER, name);
        this.caster = name;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (key.equals(CASTER)) {
            this.caster = this.getCasterDataManager();
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(CASTER, "");
    }

    public String getCasterDataManager() {
        return this.entityData.get(CASTER);
    }
}