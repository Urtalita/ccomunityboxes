package com.Portality.ccomunityboxes.boxes.gen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import java.util.Set;

public class ItemModelGenerator extends ItemModelProvider {
    private final Set<RegistryObject<Item>> items;

    public ItemModelGenerator(
            PackOutput output,
            String modId,
            ExistingFileHelper existingFileHelper,
            Set<RegistryObject<Item>> items
    ) {
        super(output, modId, existingFileHelper);
        this.items = items;
    }

    @Override
    protected void registerModels() {
        for (RegistryObject<Item> item : items) {
            String itemName = item.getId().getPath();
            // Создаём модель предмета с родителем в models/
            getBuilder(itemName)
                    .parent(new ModelFile.UncheckedModelFile(modLoc(itemName)));
        }
    }
}