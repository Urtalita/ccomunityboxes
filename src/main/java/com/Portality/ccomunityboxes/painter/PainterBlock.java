package com.Portality.ccomunityboxes.painter;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class PainterBlock extends WrenchableDirectionalBlock implements IBE<PainterBE>, IWrenchable {

    protected PainterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        AdvancementBehaviour.setPlacedBy(pLevel, pPos, pPlacer);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Player player = context.getPlayer();
        if(player.isShiftKeyDown()){
            BlockState state = super.getStateForPlacement(context);
            Direction facing = state.getValue(FACING);
            return state.setValue(FACING, facing.getOpposite());
        }
        return super.getStateForPlacement(context);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (player == null)
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        ItemStack itemInHand = player.getItemInHand(hand);
        if (AllItems.WRENCH.isIn(itemInHand))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if (onBlockEntityUse(level, pos, be -> {

            if(PackageItem.isPackage(itemInHand)){
                if(PackageItem.isPackage(be.heldStack)){
                    ItemStack held = be.heldStack.copy();
                    be.heldStack = itemInHand.copy();
                    be.craftProgress = 0;
                    player.setItemInHand(hand, held);
                } else {
                    be.heldStack = itemInHand.copy();
                    be.craftProgress = 0;
                    itemInHand.shrink(1);
                }
            } else if (itemInHand.isEmpty()){
                player.setItemInHand(hand, be.heldStack);
                be.heldStack = ItemStack.EMPTY;
                be.craftProgress = 0;
            }

            return InteractionResult.SUCCESS;
        }).consumesAction())
            return ItemInteractionResult.SUCCESS;

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public boolean shouldCheckWeakPower(BlockState state, SignalGetter level, BlockPos pos, Direction side) {
        return false;
    }

    @Override
    public Class<PainterBE> getBlockEntityClass() {
        return PainterBE.class;
    }

    @Override
    public BlockEntityType<PainterBE> getBlockEntityType() {
        return BoxBlockEntity.PAINTER.get();
    }
}
