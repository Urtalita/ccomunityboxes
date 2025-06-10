package com.Portality.ccomunityboxes.painter;

import com.Portality.ccomunityboxes.Ccomunityboxes;
import com.Portality.ccomunityboxes.boxes.summonItems.BoxItems;
import com.simibubi.create.AllItems;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.api.packager.unpacking.UnpackingHandler;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.content.logistics.BigItemStack;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.content.logistics.packager.PackagerBlock;
import com.simibubi.create.content.logistics.packager.PackagerBlockEntity;
import com.simibubi.create.content.logistics.stockTicker.PackageOrderWithCrafts;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Stack;

import static com.Portality.ccomunityboxes.ponder.BoxScenes.getRandomBox;
import static com.Portality.ccomunityboxes.ponder.BoxScenes.getRandomRareBox;

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
        CompoundTag tag = heldStack.getOrCreateTag();
        ItemStack newStack;

        if(PackageItem.isPackage(filtering.getFilter())){
            newStack = filtering.getFilter();
        } else {
            newStack = getRandomBox();
        }

        if(!rareSplashed){
            if(filtering.getFilter().getItem() instanceof PackageItem packageItem){
                if(packageItem.style.rare()){
                    newStack = getRandomBox();
                }
            }
        } else {
            if(!PackageItem.isPackage(filtering.getFilter())){
                newStack = getRandomRareBox();
            }
        }
        rareSplashed = false;

        newStack.setTag(tag);
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

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return LazyOptional.of(() -> new IItemHandler() {
                private final int OUTPUT_SLOT = 0;
                private final int SLOT_COUNT = 1;

                @Override
                public int getSlots() {
                    return SLOT_COUNT;
                }

                @Override
                public @NotNull ItemStack getStackInSlot(int slot) {
                    if (slot == OUTPUT_SLOT) {
                        return heldStack;
                    }
                    return ItemStack.EMPTY;
                }

                @Override
                public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                    if (!(stack.getItem() instanceof PackageItem)) {
                        return stack;
                    }

                    if(PackageItem.isPackage(heldStack)){
                        return stack;
                    }

                    // Сброс состояния крафта
                    if (!simulate) {
                        craftProgress = 0;
                        finishedCraft = false;
                    }

                    heldStack = stack.copy();
                    setChanged();
                    notifyUpdate();

                    ItemStack result = ItemStack.EMPTY;
                    return result;
                }

                @Override
                public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                    if (slot != OUTPUT_SLOT || amount <= 0 || heldStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    if(!finishedCraft){
                        return ItemStack.EMPTY;
                    }

                    ItemStack filter = filtering.getFilter();

                    int extractCount = Math.min(amount, heldStack.getCount());
                    ItemStack extracted = heldStack.copyWithCount(extractCount);

                    if (!simulate) {
                        finishedCraft = false;
                        craftProgress = 0;
                        prevCraftProgress = 0;

                        heldStack.shrink(extractCount);
                        if (heldStack.isEmpty()) {
                            heldStack = ItemStack.EMPTY;
                        }
                        setChanged();
                        notifyUpdate();
                    }

                    filtering.setFilter(filter);

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
            }).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        finishedCraft = compound.getBoolean("finishedCraft");
        craftProgress = compound.getInt("craftProgress");
        heldStack = ItemStack.of(compound.getCompound("heldStack"));
        splashed = compound.getBoolean("splashed");
        rareSplashed = compound.getBoolean("rareSplashed");
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        compound.putBoolean("finishedCraft", finishedCraft);
        compound.putInt("craftProgress", craftProgress);
        compound.put("heldStack", heldStack.serializeNBT());
        compound.putBoolean("splashed", splashed);
        compound.putBoolean("rareSplashed", rareSplashed);
        super.write(compound, clientPacket);
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
