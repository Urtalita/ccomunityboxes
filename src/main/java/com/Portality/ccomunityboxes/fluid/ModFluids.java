package com.Portality.ccomunityboxes.fluid;

import com.Portality.ccomunityboxes.Ccomunityboxes;
import com.Portality.ccomunityboxes.painter.BoxBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static com.Portality.ccomunityboxes.boxes.summonItems.BoxItems.INK_BUCKET;

public class ModFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Ccomunityboxes.MODID);

    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(Registries.FLUID, Ccomunityboxes.MODID);

    public static final DeferredHolder<FluidType, FluidType> INK =
            FLUID_TYPES.register("ink_fluid", InkType::new);

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> SOURCE =
            FLUIDS.register("ink_fluid", () -> new BaseFlowingFluid.Source(ModFluids.CUSTOM_PROPERTIES));

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING =
            FLUIDS.register("ink_flowing", () -> new BaseFlowingFluid.Flowing(ModFluids.CUSTOM_PROPERTIES));

    private static final BaseFlowingFluid.Properties CUSTOM_PROPERTIES =
            new BaseFlowingFluid.Properties(INK, SOURCE, FLOWING)
                    .bucket(() -> INK_BUCKET.get())
                    .block(() -> BoxBlocks.INK_FLUID.get());


    public static void register(IEventBus eventBus){
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
    }
}
