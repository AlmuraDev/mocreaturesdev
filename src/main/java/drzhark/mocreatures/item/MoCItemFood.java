/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

/**
 * Mo' Creatures food. In 26.3 the nutrition lives in the food component and the eating time and effects in the
 * consumable component. Wolves eat the meats because they are in the minecraft:meat item tag.
 */
public class MoCItemFood extends MoCItem {

    protected MoCItemFood(MoCItemFood.Builder builder) {
        super(builder.properties.food(builder.foodBuilder.build(), builder.consumable.build()));
    }

    public static class Builder {
        private final FoodProperties.Builder foodBuilder;
        private final Consumable.Builder consumable;
        private final Item.Properties properties;

        public Builder(Item.Properties properties, int amount) {
            this(properties, amount, 0.6F, false);
        }

        public Builder(Item.Properties properties, int amount, float saturation, boolean isWolfFood) {
            this(properties, amount, saturation, isWolfFood, 32);
        }

        /** isWolfFood is kept for the registrations; the minecraft:meat tag decides what wolves eat. */
        public Builder(Item.Properties properties, int amount, float saturation, boolean isWolfFood, int eatingSpeed) {
            this.properties = properties;
            this.foodBuilder = new FoodProperties.Builder().nutrition(amount).saturationModifier(saturation);
            this.consumable = Consumables.defaultFood().consumeSeconds((eatingSpeed == 0 ? 32 : eatingSpeed) / 20.0F);
        }

        public Builder setAlwaysEdible() {
            this.foodBuilder.alwaysEdible();
            return this;
        }

        public Builder setPotionEffect(MobEffectInstance effectIn, float probability) {
            this.consumable.onConsume(new ApplyStatusEffectsConsumeEffect(effectIn, probability));
            return this;
        }

        public MoCItemFood build() {
            return new MoCItemFood(this);
        }
    }
}
