package com.Portality.ccomunityboxes.boxes;

import com.Portality.ccomunityboxes.BoxModels;
import com.Portality.ccomunityboxes.Ccomunityboxes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.content.logistics.box.PackageStyles;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CustomBoxRenderer extends EntityRenderer<CustomBoxEntity> {

    protected CustomBoxRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 0.5f;
    }

    @Override
    public void render(CustomBoxEntity entity, float yaw, float pt, PoseStack ms, MultiBufferSource buffer, int light) {
        if (!VisualizationManager.supportsVisualization(entity.level())) {
            ItemStack box = entity.box;

            if (box.isEmpty() || !PackageItem.isPackage(box))
                box = AllBlocks.CARDBOARD_BLOCK.asStack();

            String modelName = String.valueOf(BuiltInRegistries.ITEM.getKey(box.getItem()));
            String[] words = modelName.split(":");
            modelName = words[1];
            PartialModel model = BoxModels.BOXES.get(modelName);

            renderBox(entity, yaw, ms, buffer, light, model);
        }
        super.render(entity, yaw, pt, ms, buffer, light);
    }

    public static void renderBox(Entity entity, float yaw, PoseStack ms, MultiBufferSource buffer, int light,
                                 PartialModel model) {
        if (model == null)
            return;
        SuperByteBuffer sbb = CachedBuffers.partial(model, Blocks.AIR.defaultBlockState());
        sbb.translate(-.5, 0, -.5)
                .rotateCentered(-AngleHelper.rad(yaw + 90), Direction.UP)
                .light(light)
                .nudge(entity.getId());
        sbb.renderInto(ms, buffer.getBuffer(RenderType.solid()));
    }

    @Override
    public ResourceLocation getTextureLocation(CustomBoxEntity customBoxEntity) {
        return null;
    }
}
