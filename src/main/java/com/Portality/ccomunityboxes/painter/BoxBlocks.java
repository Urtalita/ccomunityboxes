package com.Portality.ccomunityboxes.painter;

import com.Portality.ccomunityboxes.Ccomunityboxes;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.Portality.ccomunityboxes.Ccomunityboxes.BOX_REGISTRATE;
import static com.Portality.ccomunityboxes.fluid.ModFluids.SOURCE;

public class BoxBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Ccomunityboxes.MODID);

    public static final DeferredHolder<Block, LiquidBlock> INK_FLUID =
            BLOCKS.register("custom_fluid_block", () -> new LiquidBlock(SOURCE.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));

    public static final BlockEntry<PainterBlock> PAINTER = BOX_REGISTRATE
            .block("painter", PainterBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .simpleItem()
            .properties(p -> p.noOcclusion())
            .register();

    public static void register(IEventBus bus){
        BLOCKS.register(bus);
    }
}
