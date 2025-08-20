package com.Portality.ccomunityboxes.boxes.recipes;

import com.Portality.ccomunityboxes.Ccomunityboxes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.IEventBus;

public class BoxRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Ccomunityboxes.MODID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Ccomunityboxes.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<NBTStonecutterRecipe>> NBT_STONECUTTER_SERIALIZER =
            SERIALIZERS.register("nbt_stonecutting", () -> new NBTStonecutterRecipe.Serializer(NBTStonecutterRecipe::new));
    public static final DeferredHolder<RecipeType<?>, RecipeType<NBTStonecutterRecipe>> NBT_STONECUTTER =
            TYPES.register("nbt_stonecutting", () -> new RecipeType<NBTStonecutterRecipe>() {
                @Override
                public String toString() {
                    return "nbt_stonecutting";
                }
            });

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}
