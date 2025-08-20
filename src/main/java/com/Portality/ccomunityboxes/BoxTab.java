package com.Portality.ccomunityboxes;

import com.Portality.ccomunityboxes.boxes.summonItems.BoxItems;
import com.Portality.ccomunityboxes.painter.BoxBlockEntity;
import com.Portality.ccomunityboxes.painter.BoxBlocks;
import com.simibubi.create.AllDataComponents;
import com.simibubi.create.AllItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BoxTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Ccomunityboxes.MODID);

    public static final Supplier<CreativeModeTab> BOX = CREATIVE_MODE_TAB.register("box_tab",
            () -> CreativeModeTab.builder().icon(() -> getItemFromName(Ccomunityboxes.BOXES[26]))
                    .title(Component.translatable("creativetab.ccomunityboxes.box_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(BoxItems.INK_BUCKET.get());
                        output.accept(BoxItems.ROZE_QUARTZ_POWDER.get());

                        ItemStack stack = BoxBlocks.PAINTER.asStack();
                        stack.set(DataComponents.DAMAGE, 1);
                        output.accept(stack);

                        for (int i = 0; i < Ccomunityboxes.BOXES.length; i++){
                            output.accept(getItemFromName(Ccomunityboxes.BOXES[i]));
                        }
                    }).build());

    public static ItemStack getItemFromName(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            return null;
        }

        ResourceLocation resLoc = ResourceLocation.tryParse(Ccomunityboxes.MODID + ":" + itemName);
        if (resLoc == null) {
            return null;
        }

        return new ItemStack(BuiltInRegistries.ITEM.get(resLoc));
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
