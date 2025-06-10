package com.Portality.ccomunityboxes.painter;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class PainterVisual extends AbstractBlockEntityVisual<PainterBE> implements SimpleDynamicVisual {
    public PainterVisual(VisualizationContext ctx, PainterBE blockEntity, float partialTick) {
        super(ctx, blockEntity, partialTick);
        animate(partialTick);
    }

    @Override
    public void beginFrame(Context context) {animate(context.partialTick());}

    public void animate(float partialTick) {

    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {

    }

    @Override
    public void updateLight(float v) {

    }

    @Override
    protected void _delete() {

    }
}
