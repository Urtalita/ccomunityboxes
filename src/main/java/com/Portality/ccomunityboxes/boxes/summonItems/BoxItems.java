package com.Portality.ccomunityboxes.boxes.summonItems;

import com.Portality.ccomunityboxes.Ccomunityboxes;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.content.logistics.box.PackageStyles;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BoxItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Ccomunityboxes.MODID);

    public static void register(IEventBus eventBus) {
        for(int i = 0; i < Ccomunityboxes.BOXES.length; i++){
            ITEMS.register(Ccomunityboxes.BOXES[i],
                    () -> new SummonBoxItem(new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.UNCOMMON),
                            PackageStyles.STYLES.get(0)));
        }
        ITEMS.register(eventBus);
    }
}
