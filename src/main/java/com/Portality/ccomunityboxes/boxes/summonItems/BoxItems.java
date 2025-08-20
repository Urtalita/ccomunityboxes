package com.Portality.ccomunityboxes.boxes.summonItems;

import com.Portality.ccomunityboxes.Ccomunityboxes;
import com.simibubi.create.content.logistics.box.PackageStyles;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.Portality.ccomunityboxes.fluid.ModFluids.*;

public class BoxItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Ccomunityboxes.MODID);

    public static final DeferredHolder<Item, BucketItem> INK_BUCKET =
            ITEMS.register("ink_bucket", () -> new BucketItem(SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET)));

    public static final DeferredHolder<Item, Item> ROZE_QUARTZ_POWDER =
            ITEMS.register("roze_quartz_powder", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        for(int i = 0; i < Ccomunityboxes.BOXES.length; i++){
            ITEMS.register(Ccomunityboxes.BOXES[i],
                    () -> new SummonBoxitem(new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.UNCOMMON),
                            PackageStyles.STYLES.get(0)));
        }
        ITEMS.register(eventBus);
    }
}
