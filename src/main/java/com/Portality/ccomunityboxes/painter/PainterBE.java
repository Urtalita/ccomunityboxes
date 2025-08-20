package com.Portality.ccomunityboxes.painter;

import com.simibubi.create.AllDataComponents;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.Portality.ccomunityboxes.Ccomunityboxes.getRandomBox;
import static com.Portality.ccomunityboxes.Ccomunityboxes.getRandomRareBox;

public class PainterBE extends SmartBlockEntity {
    private boolean finishedCraft = false;
    public int craftProgress = 0;
    public int prevCraftProgress = 0;
    private int maxCraftProgress = SpoutBlockEntity.FILLING_TIME + 10;
    public ItemStack heldStack = ItemStack.EMPTY;
    public boolean splashed = false;
    public boolean rareSplashed = false;
    private FilteringBehaviour filtering;

    public PainterBE(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        filtering = new FilteringBehaviour(this, new PainterValueBox());
        behaviours.add(filtering);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                BoxBlockEntity.PAINTER.get(),
                (be, side) -> new PainterBE.ItemHandlerImpl(be)
        );
    }

    public ItemStack getRenderedBox(){return heldStack;}

    @Override
    public void tick() {
        super.tick();
        prevCraftProgress = craftProgress;
        if(!(level.getBlockEntity(worldPosition.above()) instanceof SpoutBlockEntity spoutBlockEntity)){return;}

        if (!level.isClientSide && PackageItem.isPackage(heldStack)) {
            if (craftProgress < maxCraftProgress) {
                if(craftProgress == 5){
                    if(splashed){
                        craftProgress++;
                        splashed = false;
                    }
                } else {
                    craftProgress++;
                }

                if(craftProgress == 5 + SpoutBlockEntity.FILLING_TIME / 2){
                    switchBox();
                }

                setChanged();
                notifyUpdate();

            } else if (!finishedCraft) {
                finishedCraft = true;
                setChanged();
                notifyUpdate();
            }
        }
    }

    private void switchBox() {
        ItemContainerContents tag = heldStack.get(AllDataComponents.PACKAGE_CONTENTS);
        ItemStack newStack;

        if(PackageItem.isPackage(filtering.getFilter())){
            newStack = filtering.getFilter();
        } else {
            newStack = getRandomBox(level.random);
        }

        if(!rareSplashed){
            if(filtering.getFilter().getItem() instanceof PackageItem packageItem){
                if(packageItem.style.rare()){
                    newStack = getRandomBox(level.random);
                }
            }
        } else {
            if(!PackageItem.isPackage(filtering.getFilter())){
                newStack = getRandomRareBox(level.random);
            }
        }
        rareSplashed = false;

        newStack.set(AllDataComponents.PACKAGE_CONTENTS, tag);
        heldStack = newStack;
    }

    public float getInOutProgress(float partialTicks) {
        if(craftProgress <= 5){return getInterpolated(partialTicks, craftProgress, prevCraftProgress) - 0.5f;}
        if(craftProgress >= maxCraftProgress - 5){return getInterpolated(partialTicks, craftProgress - (maxCraftProgress-5), prevCraftProgress - (maxCraftProgress-5));}

        return 0;
    }

    private float getInterpolated(float pt, int craftProgress, int prevCraftProgress){
        return Mth.lerp(pt, prevCraftProgress, craftProgress) * 0.1f;
    }

    public static class ItemHandlerImpl implements IItemHandler {
        private final int OUTPUT_SLOT = 0;
        private final int SLOT_COUNT = 1;
        private final PainterBE blockEntity;

        public ItemHandlerImpl(PainterBE blockEntity) {
            this.blockEntity = blockEntity;
        }

        @Override
        public int getSlots() {
            return SLOT_COUNT;
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            if (slot == OUTPUT_SLOT) {
                return blockEntity.heldStack;
            }
            return ItemStack.EMPTY;
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (!(stack.getItem() instanceof PackageItem)) {
                return stack;
            }

            if(PackageItem.isPackage(blockEntity.heldStack)){
                return stack;
            }

            // Сброс состояния крафта
            if (!simulate) {
                blockEntity.craftProgress = 0;
                blockEntity.finishedCraft = false;
            }

            blockEntity.heldStack = stack.copy();
            blockEntity.setChanged();
            blockEntity.notifyUpdate();

            ItemStack result = ItemStack.EMPTY;
            return result;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot != OUTPUT_SLOT || amount <= 0 || blockEntity.heldStack.isEmpty()) {
                return ItemStack.EMPTY;
            }

            if(!blockEntity.finishedCraft){
                return ItemStack.EMPTY;
            }

            ItemStack filter = blockEntity.filtering.getFilter();

            int extractCount = Math.min(amount, blockEntity.heldStack.getCount());
            ItemStack extracted = blockEntity.heldStack.copyWithCount(extractCount);

            if (!simulate) {
                blockEntity.finishedCraft = false;
                blockEntity.craftProgress = 0;
                blockEntity.prevCraftProgress = 0;

                blockEntity.heldStack.shrink(extractCount);
                if (blockEntity.heldStack.isEmpty()) {
                    blockEntity.heldStack = ItemStack.EMPTY;
                }
                blockEntity.setChanged();
                blockEntity.notifyUpdate();
            }

            blockEntity.filtering.setFilter(filter);

            return extracted;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return slot == OUTPUT_SLOT && stack.getItem() instanceof PackageItem;
        }
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        finishedCraft = compound.getBoolean("finishedCraft");
        craftProgress = compound.getInt("craftProgress");

        if(!compound.getCompound("heldStack").isEmpty()){
            heldStack = ItemStack.parseOptional(registries ,compound.getCompound("heldStack"));
            if(heldStack.getItem() == Blocks.BARRIER.asItem()){heldStack = ItemStack.EMPTY;}
        }

        splashed = compound.getBoolean("splashed");
        rareSplashed = compound.getBoolean("rareSplashed");
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        CompoundTag nbt = new CompoundTag();

        compound.putBoolean("finishedCraft", finishedCraft);
        compound.putInt("craftProgress", craftProgress);
        if (!heldStack.isEmpty()){
            compound.put("heldStack", heldStack.save(registries));
        }
        compound.putBoolean("splashed", splashed);
        compound.putBoolean("rareSplashed", rareSplashed);
        super.write(compound, registries, clientPacket);
    }

    class PainterValueBox extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 4, 16.05);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return direction.getAxis()
                    .isHorizontal();
        }

    }
}
