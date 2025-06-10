package com.Portality.ccomunityboxes.mixins;

import com.Portality.ccomunityboxes.BoxModels;
import com.Portality.ccomunityboxes.Ccomunityboxes;
import com.Portality.ccomunityboxes.boxes.summonItems.SummonBoxItem;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorBlockEntity;
import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorPackage;
import com.simibubi.create.content.kinetics.chainConveyor.ChainConveyorVisual;
import com.simibubi.create.content.logistics.box.PackageItem;
import dev.engine_room.flywheel.api.instance.InstancerProvider;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.AbstractVisual;
import dev.engine_room.flywheel.lib.visual.util.SmartRecycler;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(ChainConveyorVisual.class)
public class RenderBoxMixin {
    @Shadow @Final private SmartRecycler<ResourceLocation, TransformedInstance> rigging;
    private SmartRecycler<ResourceLocation, TransformedInstance> custom_boxes;
    private TransformedInstance instance;
    /*
    @Inject(
            method = "<init>",
            at = @At(
                    value = "TAIL", // Инжект в конец конструктора
                    target = "Lcom/simibubi/create/content/kinetics/chainConveyor/ChainConveyorVisual;setupGuards()V"
            ),
            remap = false
    )
    private void injectedConstructor(
            VisualizationContext context,
            ChainConveyorBlockEntity blockEntity,
            float partialTick,
            CallbackInfo ci
    ) {
        custom_boxes = new SmartRecycler<>(key -> context.instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(BoxModels.BOXES.get(key))).createInstance());
        instance = context.instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(BoxModels.BOXES.get("portal")))
                .createInstance();
    }

    @Inject(
            method = "setupBoxVisual",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )

    private void onBoxVisual(ChainConveyorBlockEntity be, ChainConveyorPackage box, float partialTicks, CallbackInfo ci) {
        if(box.item.getItem() instanceof SummonBoxItem){
            SetupCustomBoxVisual(be, box, partialTicks);
            ci.cancel();
        }
    }

    private void SetupCustomBoxVisual(ChainConveyorBlockEntity be, ChainConveyorPackage box, float partialTicks){
        if (box.worldPosition == null)
            return;
        if (box.item == null || box.item.isEmpty())
            return;
        BlockPos pos = be.getBlockPos();
        ChainConveyorPackage.ChainConveyorPackagePhysicsData physicsData = box.physicsData(be.getLevel());
        if (physicsData.prevPos == null)
            return;

        Vec3 position = physicsData.prevPos.lerp(physicsData.pos, partialTicks);
        Vec3 targetPosition = physicsData.prevTargetPos.lerp(physicsData.targetPos, partialTicks);
        float yaw = AngleHelper.angleLerp(partialTicks, physicsData.prevYaw, physicsData.yaw);
        Vec3 offset =
                new Vec3(targetPosition.x - pos.getX(), targetPosition.y - pos.getY(), targetPosition.z - pos.getZ());

        BlockPos containingPos = BlockPos.containing(position);
        Level level = be.getLevel();
        int light = LightTexture.pack(level.getBrightness(LightLayer.BLOCK, containingPos),
                level.getBrightness(LightLayer.SKY, containingPos));

        if (physicsData.modelKey == null) {
            ResourceLocation key = ForgeRegistries.ITEMS.getKey(box.item.getItem());
            if (key == null)
                return;

            physicsData.modelKey = key;
        }

        TransformedInstance rigBuffer = rigging.get(physicsData.modelKey);
        TransformedInstance boxBuffer = instance;

        Vec3 dangleDiff = VecHelper.rotate(targetPosition.add(0, 0.5, 0)
                .subtract(position), -yaw, Direction.Axis.Y);
        float zRot = Mth.wrapDegrees((float) Mth.atan2(-dangleDiff.x, dangleDiff.y) * Mth.RAD_TO_DEG) / 2;
        float xRot = Mth.wrapDegrees((float) Mth.atan2(dangleDiff.z, dangleDiff.y) * Mth.RAD_TO_DEG) / 2;
        zRot = Mth.clamp(zRot, -25, 25);
        xRot = Mth.clamp(xRot, -25, 25);

        for (TransformedInstance buf : new TransformedInstance[] { rigBuffer, boxBuffer }) {
            buf.setIdentityTransform();
            buf.translate(pos);
            buf.translate(offset);
            buf.translate(0, 10 / 16f, 0);
            buf.rotateYDegrees(yaw);

            buf.rotateZDegrees(zRot);
            buf.rotateXDegrees(xRot);

            if (physicsData.flipped && buf == rigBuffer)
                buf.rotateYDegrees(180);

            buf.uncenter();
            buf.translate(0, -PackageItem.getHookDistance(box.item) + 7 / 16f, 0);

            buf.light(light);

            buf.setChanged();
        }
    }

     */
}
