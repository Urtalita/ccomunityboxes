package com.Portality.ccomunityboxes.boxes.recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class NBTStonecutterRecipe extends StonecutterRecipe {
    public NBTStonecutterRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack result) {
        super(id, group, ingredient, result);
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        ItemStack resultStack = super.assemble(container, registryAccess);
        ItemStack inputStack = container.getItem(0);

        // Копируем NBT из входного предмета
        if (inputStack.hasTag()) {
            resultStack.setTag(inputStack.getTag().copy());
        }

        return resultStack;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<NBTStonecutterRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public NBTStonecutterRecipe fromJson(ResourceLocation id, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"), false);
            String resultId = GsonHelper.getAsString(json, "result");
            int count = GsonHelper.getAsInt(json, "count", 1);
            ItemStack result = new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(resultId)), count);
            return new NBTStonecutterRecipe(id, group, ingredient, result);
        }

        @Override
        public NBTStonecutterRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return new NBTStonecutterRecipe(id, group, ingredient, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, NBTStonecutterRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}