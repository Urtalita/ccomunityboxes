package com.Portality.ccomunityboxes.fluid;

import com.Portality.ccomunityboxes.Ccomunityboxes;
import com.Portality.ccomunityboxes.boxes.summonItems.BoxItems;
import com.Portality.ccomunityboxes.painter.BoxBlocks;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Ccomunityboxes.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Ccomunityboxes.MODID);

    // Тип жидкости
    public static final RegistryObject<FluidType> INK_TYPE = FLUID_TYPES.register(
            "ink_fluid",
            InkType::new
    );

    // Источник и текущая версия
    public static final RegistryObject<FlowingFluid> SOURCE = FLUIDS.register(
            "ink_fluid",
            () -> new ForgeFlowingFluid.Source(ModFluids.PROPERTIES)
    );

    public static final RegistryObject<FlowingFluid> FLOWING = FLUIDS.register(
            "ink_flowing",
            () -> new ForgeFlowingFluid.Flowing(ModFluids.PROPERTIES)
    );

    // Настройки жидкости
    public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(
            INK_TYPE,
            SOURCE,
            FLOWING
    )
            .bucket(BoxItems.INK_BUCKET) // Ведро
            .slopeFindDistance(2)
            .levelDecreasePerBlock(2)
            .explosionResistance(100F)
            .block(BoxBlocks.INK_FLUID); // Блок
}
