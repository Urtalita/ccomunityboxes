package com.Portality.ccomunityboxes.boxes;

import com.Portality.ccomunityboxes.BoxModels;
import com.Portality.ccomunityboxes.boxes.summonItems.SummonBoxItem;
import com.simibubi.create.AllEntityTypes;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.logistics.box.PackageEntity;
import com.simibubi.create.content.logistics.chute.ChuteBlock;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

public class CustomBoxEntity extends PackageEntity {
    public String model;

    public CustomBoxEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public CustomBoxEntity(EntityType<?> entityType, Level world, double x, double y, double z, String model) {
        super(entityType, world);
        this.setPos(x, y, z);
        this.model = model;
        this.refreshDimensions();
    }

    public PartialModel getModel(){
        return BoxModels.BOXES.get(model);
    }

    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        @SuppressWarnings("unchecked")
        EntityType.Builder<CustomBoxEntity> boxBuilder = (EntityType.Builder<CustomBoxEntity>) builder;
        return boxBuilder.setCustomClientFactory(CustomBoxEntity::spawn)
                .sized(1, 1);
    }
    public static CustomBoxEntity spawn(PlayMessages.SpawnEntity spawnPacket, Level world) {
        FriendlyByteBuf buf = spawnPacket.getAdditionalData();
        String model = buf.readUtf();
        CustomBoxEntity entity = new CustomBoxEntity(
                Boxes.CUSTOM_PACKAGE.get(),
                world,
                spawnPacket.getPosX(),
                spawnPacket.getPosY(),
                spawnPacket.getPosZ(),
                model
        );
        return entity;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        // Принудительная синхронизация данных
        this.setPos(this.getX(), this.getY(), this.getZ());
    }

    public static PackageEntity fromItemStack(Level world, Vec3 position, ItemStack itemstack) {
        PackageEntity packageEntity = Boxes.CUSTOM_PACKAGE
                .create(world);
        packageEntity.setPos(position);
        packageEntity.setBox(itemstack);
        return packageEntity;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeUtf(model != null ? model : "box");
        super.writeSpawnData(buffer);
    }

    public static CustomBoxEntity fromDroppedItem(Level world, Entity originalEntity, ItemStack itemstack, String model) {
        CustomBoxEntity packageEntity = Boxes.CUSTOM_PACKAGE.get()
                .create(world);
        packageEntity.model = model;

        Vec3 position = originalEntity.position();
        packageEntity.setPos(position);
        packageEntity.setBox(itemstack);
        packageEntity.setDeltaMovement(originalEntity.getDeltaMovement()
                .scale(1.5f));

        if (world != null && !world.isClientSide)
            if (ChuteBlock.isChute(world.getBlockState(BlockPos.containing(position.x, position.y + .5f, position.z))))
                packageEntity.setYRot(((int) packageEntity.getYRot()) / 90 * 90);

        return packageEntity;
    }

    public boolean isOnConveyor() {
        return this.getVehicle() instanceof AbstractContraptionEntity;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("model", model);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        model = compound.getString("model");
    }
}
