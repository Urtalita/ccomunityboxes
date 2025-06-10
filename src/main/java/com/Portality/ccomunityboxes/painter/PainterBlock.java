package com.Portality.ccomunityboxes.painter;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.content.logistics.packager.PackagerBlock;
import com.simibubi.create.content.logistics.packager.PackagerBlockEntity;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.IItemHandler;

import java.util.EnumMap;
import java.util.Map;

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
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
                                 BlockHitResult hit) {
        if (player == null)
            return InteractionResult.PASS;

        ItemStack itemInHand = player.getItemInHand(handIn);
        if (AllItems.WRENCH.isIn(itemInHand))
            return InteractionResult.PASS;

        if (onBlockEntityUse(worldIn, pos, be -> {

            if(PackageItem.isPackage(itemInHand)){
                if(PackageItem.isPackage(be.heldStack)){
                    ItemStack held = be.heldStack.copy();
                    be.heldStack = itemInHand.copy();
                    be.craftProgress = 0;
                    player.setItemInHand(handIn, held);
                } else {
                    be.heldStack = itemInHand.copy();
                    be.craftProgress = 0;
                    itemInHand.shrink(1);
                }
            } else if (itemInHand.isEmpty()){
                player.setItemInHand(handIn, be.heldStack);
                be.heldStack = ItemStack.EMPTY;
                be.craftProgress = 0;
            }

            return InteractionResult.SUCCESS;
        }).consumesAction())
            return InteractionResult.SUCCESS;

        return InteractionResult.SUCCESS;
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
