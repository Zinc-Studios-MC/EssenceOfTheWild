package net.mrmisc.essenceofthewild.integration.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.registries.ForgeRegistries;
import net.mrmisc.essenceofthewild.EssenceOfTheWildMod;
import net.mrmisc.essenceofthewild.entity.custom.chicken.ChickenEntity;
import net.mrmisc.essenceofthewild.entity.custom.cow.CowEntity;
import net.mrmisc.essenceofthewild.entity.custom.duck.DuckEntity;
import net.mrmisc.essenceofthewild.entity.custom.hare.HareEntity;
import net.mrmisc.essenceofthewild.entity.custom.mooshroom.MooshroomEntity;
import net.mrmisc.essenceofthewild.entity.custom.pig.PigEntity;
import net.mrmisc.essenceofthewild.entity.custom.rabbit.RabbitEntity;
import net.mrmisc.essenceofthewild.entity.custom.sheep.SheepEntity;
import net.mrmisc.essenceofthewild.item.custom.icecream.EffectIceCream;
import net.mrmisc.essenceofthewild.item.custom.icecream.IceCream;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum EOTWJadeEntityProvider implements IEntityComponentProvider, IServerDataProvider<EntityAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();

        if (data.getBoolean("EOTWIceCream")) {
            tooltip.add(Component.translatable("tooltip.essenceofthewild.icecream", data.getInt("ItemCount")));

            if (data.contains("Effect")) {
                tooltip.add(Component.translatable("tooltip.essenceofthewild.icecream.effect", Component.translatable(data.getString("Effect"))));
            }
            return;
        }

        if (!data.getBoolean("EOTWEntity")) {
            return;
        }

        if (data.contains("Variant")) {
            tooltip.add(Component.translatable("tooltip.essenceofthewild.entity.variant", cleanVariant(data.getString("Variant"))));
        }

        if (data.getBoolean("DeliveringNestEgg")) {
            tooltip.add(Component.translatable("tooltip.essenceofthewild.entity.nesting"));
        } else if (data.getBoolean("GuardingNest")) {
            tooltip.add(Component.translatable("tooltip.essenceofthewild.entity.guarding"));
        } else if (data.contains("EggTime")) {
            tooltip.add(Component.translatable("tooltip.essenceofthewild.entity.egg", EOTWJadeBlockProvider.formatTicks(data.getInt("EggTime"))));
        }

        if (data.getBoolean("MilkReady")) {
            tooltip.add(Component.translatable("tooltip.essenceofthewild.entity.milk"));
        }

        if (data.contains("GrowthTime")) {
            tooltip.add(Component.translatable("tooltip.essenceofthewild.entity.growth", EOTWJadeBlockProvider.formatTicks(data.getInt("GrowthTime"))));
        } else if (data.contains("BreedCooldown")) {
            tooltip.add(Component.translatable("tooltip.essenceofthewild.entity.breeding", EOTWJadeBlockProvider.formatTicks(data.getInt("BreedCooldown"))));
        } else if (data.getBoolean("InLove")) {
            tooltip.add(Component.translatable("tooltip.essenceofthewild.entity.love"));
        }
    }

    @Override
    public void appendServerData(CompoundTag data, EntityAccessor accessor) {
        Entity entity = accessor.getEntity();

        if (entity instanceof ItemEntity itemEntity && itemEntity.getItem().getItem() instanceof IceCream iceCream) {
            data.putBoolean("EOTWIceCream", true);
            data.putInt("ItemCount", itemEntity.getItem().getCount());

            if (iceCream instanceof EffectIceCream effectIceCream) {
                data.putString("Effect", effectIceCream.getMobEffect().getDescriptionId());
            }
            return;
        }

        ResourceLocation typeId = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());

        if (typeId == null || !EssenceOfTheWildMod.MOD_ID.equals(typeId.getNamespace())) {
            return;
        }

        data.putBoolean("EOTWEntity", true);
        addVariant(data, entity);

        if (entity instanceof AgeableMob ageableMob) {
            int age = ageableMob.getAge();
            if (age < 0) {
                data.putInt("GrowthTime", -age);
            } else if (age > 0) {
                data.putInt("BreedCooldown", age);
            }
        }

        if (entity instanceof Animal animal && animal.isInLove()) {
            data.putBoolean("InLove", true);
        }

        if (entity instanceof ChickenEntity chicken) {
            data.putInt("EggTime", Math.max(0, chicken.getEggTimeTicks()));
            data.putBoolean("DeliveringNestEgg", chicken.isDeliveringNestEgg());
            data.putBoolean("GuardingNest", chicken.isGuardingNest());
        } else if (entity instanceof DuckEntity duck) {
            data.putInt("EggTime", Math.max(0, duck.getEggTimeTicks()));
            data.putBoolean("DeliveringNestEgg", duck.isDeliveringNestEgg());
            data.putBoolean("GuardingNest", duck.isGuardingNest());
        }

        if (entity instanceof AgeableMob ageableMob && !ageableMob.isBaby()
                && (entity instanceof CowEntity || entity instanceof SheepEntity || entity instanceof MooshroomEntity)) {
            data.putBoolean("MilkReady", true);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return EOTWJadePlugin.ENTITIES;
    }

    private static void addVariant(CompoundTag data, Entity entity) {
        if (entity instanceof ChickenEntity chicken) {
            data.putString("Variant", chicken.getVariant().id());
        } else if (entity instanceof DuckEntity duck) {
            data.putString("Variant", duck.getVariant().id());
        } else if (entity instanceof CowEntity cow) {
            data.putString("Variant", cow.getVariant().id());
        } else if (entity instanceof SheepEntity sheep) {
            data.putString("Variant", sheep.getVariant().id());
        } else if (entity instanceof PigEntity pig) {
            data.putString("Variant", pig.getVariant().id());
        } else if (entity instanceof MooshroomEntity mooshroom) {
            data.putString("Variant", mooshroom.getVariantMooshroom().id());
        } else if (entity instanceof RabbitEntity rabbit) {
            data.putString("Variant", rabbit.getRabbitVariant().id());
        } else if (entity instanceof HareEntity hare) {
            data.putString("Variant", hare.getRabbitVariant().id());
        }
    }

    private static String cleanVariant(String variant) {
        String withSpaces = variant.replace('_', ' ');
        return withSpaces.isEmpty() ? variant : withSpaces.substring(0, 1).toUpperCase() + withSpaces.substring(1);
    }
}
