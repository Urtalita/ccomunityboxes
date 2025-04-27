package com.Portality.ccomunityboxes.boxes.summonItems;

import com.Portality.ccomunityboxes.Ccomunityboxes;
import com.Portality.ccomunityboxes.boxes.Boxes;
import com.Portality.ccomunityboxes.boxes.CustomBoxEntity;
import com.simibubi.create.Create;
import com.simibubi.create.content.logistics.box.PackageEntity;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.content.logistics.box.PackageStyles;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.ref.WeakReference;
import java.util.List;

public class SummonBoxItem extends PackageItem {
    public SummonBoxItem(Properties properties, PackageStyles.PackageStyle style) {
        super(properties, style);
    }

    @Override
    public Entity createEntity(Level world, Entity location, ItemStack itemstack) {
        return CustomBoxEntity.fromDroppedItem(world, location, itemstack, getModel());
    }

    @Override
    public InteractionResultHolder<ItemStack> open(Level worldIn, Player playerIn, InteractionHand handIn) {
        return super.open(worldIn, playerIn, handIn);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int ticks) {
        if (!(entity instanceof Player player))
            return;
        int i = this.getUseDuration(stack) - ticks;
        if (i < 0)
            return;

        float f = getPackageVelocity(i);
        if (f < 0.1D)
            return;
        if (world.isClientSide)
            return;

        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW,
                SoundSource.NEUTRAL, 0.5F, 0.5F);

        ItemStack copy = stack.copy();
        if (!player.getAbilities().instabuild)
            stack.shrink(1);

        Vec3 vec = new Vec3(entity.getX(), entity.getY() + entity.getBoundingBox()
                .getYsize() / 2f, entity.getZ());
        Vec3 motion = entity.getLookAngle()
                .scale(f * 2);
        vec = vec.add(motion);

        CustomBoxEntity packageEntity = new CustomBoxEntity(Boxes.CUSTOM_PACKAGE.get() ,world, vec.x, vec.y, vec.z, getModel());
        packageEntity.setBox(copy);
        packageEntity.setDeltaMovement(motion);
        packageEntity.tossedBy = new WeakReference<>(player);
        world.addFreshEntity(packageEntity);
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {

        Vec3 point = context.getClickLocation();
        float h = style.height() / 16f;
        float r = style.width() / 2f / 16f;

        if (context.getClickedFace() == Direction.DOWN)
            point = point.subtract(0, h + .25f, 0);
        else if (context.getClickedFace()
                .getAxis()
                .isHorizontal())
            point = point.add(Vec3.atLowerCornerOf(context.getClickedFace()
                            .getNormal())
                    .scale(r));

        AABB scanBB = new AABB(point, point).inflate(r, 0, r)
                .expandTowards(0, h, 0);
        Level world = context.getLevel();
        if (!world.getEntities(Boxes.CUSTOM_PACKAGE.get(), scanBB, e -> true)
                .isEmpty())
            return super.useOn(context);

        CustomBoxEntity packageEntity = new CustomBoxEntity(Boxes.CUSTOM_PACKAGE.get() ,world, point.x, point.y, point.z, getModel());
        ItemStack itemInHand = context.getItemInHand();
        packageEntity.setBox(itemInHand.copy());
        world.addFreshEntity(packageEntity);
        itemInHand.shrink(1);
        return InteractionResult.SUCCESS;
    }

    private String getModel(){
        String model = ForgeRegistries.ITEMS.getKey(this).toString();
        String[] words = model.split(":");
        return words[1];
    }
}
