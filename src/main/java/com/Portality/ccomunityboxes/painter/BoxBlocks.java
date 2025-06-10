package com.Portality.ccomunityboxes.painter;

import com.Portality.ccomunityboxes.Ccomunityboxes;
import com.Portality.ccomunityboxes.fluid.ModFluids;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.Create;
import com.simibubi.create.content.logistics.box.PackageStyles;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.Portality.ccomunityboxes.Ccomunityboxes.BOX_REGISTRATE;

public class BoxBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Ccomunityboxes.MODID);

    public static final BlockEntry<PainterBlock> PAINTER = BOX_REGISTRATE
            .block("painter", PainterBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .simpleItem()
            .properties(p -> p.noOcclusion())
            .register();

    public static final RegistryObject<LiquidBlock> INK_FLUID = BLOCKS.register(
            "custom_fluid_block",
            () -> new LiquidBlock(ModFluids.SOURCE, Block.Properties.copy(Blocks.WATER))
    );



    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
