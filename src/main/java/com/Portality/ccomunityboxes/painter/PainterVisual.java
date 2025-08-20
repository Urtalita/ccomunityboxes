package com.Portality.ccomunityboxes.painter;

import com.simibubi.create.content.logistics.packager.PackagerBlock;
import com.simibubi.create.content.logistics.packager.PackagerRenderer;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class PainterVisual extends AbstractBlockEntityVisual<PainterBE> implements SimpleDynamicVisual {

    public final TransformedInstance tray;

    public float lastTrayOffset = Float.NaN;

    public PainterVisual(VisualizationContext ctx, PainterBE blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);

        tray = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(PackagerRenderer.getTrayModel(blockState)))
                .createInstance();

        Direction facing = blockState.getValue(PackagerBlock.FACING)
                .getOpposite();

        animate(partialTick);
    }

    @Override
    public void beginFrame(Context context) {animate(context.partialTick());}

    public void animate(float partialTick) {
        float trayOffset = blockEntity.getInOutProgress(partialTick);

        if (trayOffset != lastTrayOffset) {
            Direction facing = blockState.getValue(PackagerBlock.FACING)
                    .getOpposite();

            var lowerCorner = Vec3.atLowerCornerOf(facing.getNormal());

            tray.setIdentityTransform()
                    .translate(getVisualPosition())
                    .translate(lowerCorner.scale(trayOffset))
                    .rotateYCenteredDegrees(facing.toYRot())
                    .setChanged();

            lastTrayOffset = trayOffset;
        }
    }

    @Override
    public void updateLight(float v) {
        relight(tray);
    }

    @Override
    protected void _delete() {
        tray.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {

    }
}
