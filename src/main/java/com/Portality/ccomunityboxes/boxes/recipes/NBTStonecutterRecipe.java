package com.Portality.ccomunityboxes.boxes.recipes;

import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.AllDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class NBTStonecutterRecipe extends StonecutterRecipe {

    public NBTStonecutterRecipe(String group, Ingredient ingredient, ItemStack result) {
        super(group, ingredient, result);
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider provider) {
        ItemStack resultStack = super.assemble(input, provider);
        ItemStack inputStack = input.getItem(0);

        // Копируем NBT из входного предмета
        if (inputStack.has(AllDataComponents.PACKAGE_CONTENTS)) {
            resultStack.set(AllDataComponents.PACKAGE_CONTENTS, inputStack.get(AllDataComponents.PACKAGE_CONTENTS));
        }

        return resultStack;
    }

    public static class Serializer extends StonecutterRecipe.Serializer<NBTStonecutterRecipe> implements RecipeSerializer<NBTStonecutterRecipe>{

        protected Serializer(Factory<NBTStonecutterRecipe> factory) {
            super(factory);
        }

        @Override
        public MapCodec<NBTStonecutterRecipe> codec() {
            return super.codec();
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, NBTStonecutterRecipe> streamCodec() {
            return super.streamCodec();
        }
    }
}