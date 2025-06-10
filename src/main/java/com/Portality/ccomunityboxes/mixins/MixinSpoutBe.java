package com.Portality.ccomunityboxes.mixins;

import com.Portality.ccomunityboxes.fluid.ModFluids;
import com.Portality.ccomunityboxes.painter.BoxBlockEntity;
import com.Portality.ccomunityboxes.painter.BoxBlocks;
import com.Portality.ccomunityboxes.painter.PainterBE;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.simibubi.create.content.fluids.spout.SpoutBlockEntity.FILLING_TIME;
import static com.simibubi.create.content.kinetics.base.DirectionalKineticBlock.FACING;

@Mixin(SpoutBlockEntity.class)
public abstract class MixinSpoutBe {

    @Shadow(remap = false)
    private int processingTicks;

    @Shadow protected abstract FluidStack getCurrentFluidInTank();

    @Shadow private SmartFluidTankBehaviour tank;

    @Shadow @Final public static int FILLING_TIME;

    @Inject(
            method = "tick()V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false // Отключаем ремаппинг если используется ForgeGradle 5+
    )

    private void tick(CallbackInfo ci) {
        BlockEntity be = (SpoutBlockEntity) (Object) this;
        Level level = be.getLevel();
        BlockPos pos = be.getBlockPos();

        FluidStack currentFluidInTank = getCurrentFluidInTank();
        if (processingTicks == -1 && (((SpoutBlockEntity) be).isVirtual() || !level.isClientSide())) {
            BlockPos filling = pos.below();
            if(level.getBlockEntity(filling) instanceof PainterBE painterBE){
                if(currentFluidInTank.getFluid().getFluidType() == Fluids.WATER.getFluidType()
                        && currentFluidInTank.getAmount() >= 500) {
                    splash(pos, level, currentFluidInTank, be, painterBE, false);
                } else if(currentFluidInTank.getFluid().getFluidType() == ModFluids.INK_TYPE.get()
                        && currentFluidInTank.getAmount() >= 100) {
                    splash(pos, level, currentFluidInTank, be, painterBE, true);
                }
            }
        }
    }

    private void splash(BlockPos pos, Level level, FluidStack currentFluidInTank, BlockEntity be, PainterBE painterBE, boolean rareMode){
        if(painterBE.craftProgress == 5){
            if(rareMode){
                painterBE.splashed = true;
                painterBE.rareSplashed = true;
                tank.getPrimaryHandler()
                        .setFluid(FluidHelper.copyStackWithAmount(currentFluidInTank,
                                currentFluidInTank.getAmount() - 100));
            } else {
                painterBE.splashed = true;
                tank.getPrimaryHandler()
                        .setFluid(FluidHelper.copyStackWithAmount(currentFluidInTank,
                                currentFluidInTank.getAmount() - 500));
            }
            processingTicks = FILLING_TIME;

            AllSoundEvents.SPOUTING.playOnServer(level, pos);

            ((SpoutBlockEntity) be).notifyUpdate();
        }
    }
}