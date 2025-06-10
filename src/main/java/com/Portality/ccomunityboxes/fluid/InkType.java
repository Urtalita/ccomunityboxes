package com.Portality.ccomunityboxes.fluid;

import com.Portality.ccomunityboxes.Ccomunityboxes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class InkType extends FluidType {
    public InkType() {
        super(Properties.create()
                .density(3000)       // Высокая плотность (тонет даже в воде)
                .viscosity(6000)     // Очень высокая вязкость (медленное течение)
                .temperature(1300)   // Высокая температура (наносит урон)
                .lightLevel(15)// Максимальное свечение
                .canHydrate(false)   // Не увлажняет землю
                .canDrown(false)     // Не топит существ
                .canExtinguish(false)// Не тушит огонь
                .canSwim(false)      // Нельзя плавать
                .supportsBoating(false) // Нельзя использовать лодку
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
        );
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return Ccomunityboxes.asResource("block/ink_still");
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return Ccomunityboxes.asResource("block/ink_flow");
            }
        });
    }
}
