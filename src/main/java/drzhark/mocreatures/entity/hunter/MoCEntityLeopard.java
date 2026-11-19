/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.hunter;

import drzhark.mocreatures.MoCTools;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import drzhark.mocreatures.init.MoCLootTables;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class MoCEntityLeopard extends MoCEntityBigCat {

    public MoCEntityLeopard(EntityType<? extends MoCEntityLeopard> type, Level world) {
        super(type, world);
        //setSize(1.165F, 1.01F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MoCEntityBigCat.createAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    public void selectType() {
        if (getTypeMoC() == 0) {
            checkSpawningBiome();
        }
        super.selectType();
    }

    @Override
    public boolean checkSpawningBiome() {
        int i = Mth.floor(this.getX());
        int j = Mth.floor(getBoundingBox().minY);
        int k = Mth.floor(this.getZ());
        BlockPos pos = new BlockPos(i, j, k);

        ResourceKey<Biome> currentbiome = MoCTools.biomeKind(this.level(), pos);
        try {
            // In 1.20.1, BiomeDictionary is deprecated, use biome path instead
            String biomePath = currentbiome.location().getPath();
            if (biomePath.contains("snow") || biomePath.contains("frozen")) {
                setTypeMoC(2); //snow leopard
                return true;
            }
        } catch (Exception ignored) {
        }
        setTypeMoC(1);
        return true;
    }

    @Override
    public Identifier getTexture() {
        if (MoCreatures.proxy.legacyBigCatModels) {
            if (getTypeMoC() == 2) {
                return MoCreatures.proxy.getModelTexture("big_cat_snow_leopard_legacy.png");
            }
            return MoCreatures.proxy.getModelTexture("big_cat_leopard_legacy.png");
        }
        if (getTypeMoC() == 2) {
            return MoCreatures.proxy.getModelTexture("big_cat_snow_leopard.png");
        }
        return MoCreatures.proxy.getModelTexture("big_cat_leopard.png");
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        final InteractionResult tameResult = this.processTameInteract(player, hand);
        if (tameResult != null) {
            return tameResult;
        }

        if (this.getIsRideable() && this.getIsAdult() && (!this.getIsChested() || !player.isShiftKeyDown()) && !this.isVehicle()) {
            if (!this.level().isClientSide() && player.startRiding(this)) {
                player.setYRot(this.getYRot());
                player.setXRot(this.getXRot());
                setSitting(false);
            }

            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    protected Identifier getDefaultLootTable() {
        return MoCLootTables.LEOPARD;
    }

    @Override
    public String getOffspringClazz(IMoCTameable mate) {
        if (mate instanceof MoCEntityPanther && mate.getTypeMoC() == 1) {
            return "Panthard";//"Panther";
        }
        if (mate instanceof MoCEntityTiger && mate.getTypeMoC() == 1) {
            return "Leoger";//"Tiger";
        }
        if (mate instanceof MoCEntityLion && mate.getTypeMoC() == 2) {
            return "Liard";//"Lion";
        }
        return "Leopard";
    }

    @Override
    public int getOffspringTypeInt(IMoCTameable mate) {
        if (mate instanceof MoCEntityPanther && mate.getTypeMoC() == 1) {
            return 1;//3; //panthard
        }
        if (mate instanceof MoCEntityTiger && mate.getTypeMoC() == 1) {
            return 1;//4; //leoger
        }
        if (mate instanceof MoCEntityLion && mate.getTypeMoC() == 2) {
            return 1;//4; //liard
        }
        return this.getTypeMoC();
    }

    @Override
    public boolean compatibleMate(Entity mate) {
        return (mate instanceof MoCEntityLeopard && ((MoCEntityLeopard) mate).getTypeMoC() == this.getTypeMoC())
                || (mate instanceof MoCEntityPanther && ((MoCEntityPanther) mate).getTypeMoC() == 1)
                || (mate instanceof MoCEntityTiger && ((MoCEntityTiger) mate).getTypeMoC() == 1)
                || (mate instanceof MoCEntityLion && ((MoCEntityLion) mate).getTypeMoC() == 2);
    }

    @Override
    public int getMoCMaxAge() {
        return 95;
    }

    @Override
    public boolean canAttackTarget(LivingEntity entity) {
        if (!this.getIsAdult() && (this.getMoCAge() < this.getMoCMaxAge() * 0.8)) {
            return false;
        }
        if (entity instanceof MoCEntityLeopard) {
            return false;
        }
        return entity.getBbHeight() < 1.3F && entity.getBbWidth() < 1.3F;
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return this.getBbHeight() * 0.92F;
    }
}
