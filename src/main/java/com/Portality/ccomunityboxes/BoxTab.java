package com.Portality.ccomunityboxes;

import com.Portality.ccomunityboxes.boxes.summonItems.BoxItems;
import com.Portality.ccomunityboxes.painter.BoxBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.Portality.ccomunityboxes.ponder.BoxScenes.getItemFromName;
import static com.Portality.ccomunityboxes.ponder.BoxScenes.getItemFromNameClear;

public class BoxTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Ccomunityboxes.MODID);

    public static final RegistryObject<CreativeModeTab> CREATE_SPRINGS_MAIN_TAB = CREATIVE_MODE_TAB.register("creativetab.box_tab",
            () -> CreativeModeTab.builder().icon(() -> getItemFromName(Ccomunityboxes.BOXES[26]))
                    .title(Component.translatable("creativetab.box_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(BoxBlocks.PAINTER);
                        pOutput.accept(BoxItems.INK_BUCKET.get());
                        pOutput.accept(BoxItems.ROZE_QUARTZ_POWDER.get());

                        for (int i = 0; i < Ccomunityboxes.BOXES.length; i++){
                            pOutput.accept(getItemFromName(Ccomunityboxes.BOXES[i]));
                        }
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
