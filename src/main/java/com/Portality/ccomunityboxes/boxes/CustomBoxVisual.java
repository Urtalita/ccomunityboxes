package com.Portality.ccomunityboxes.boxes;

import com.Portality.ccomunityboxes.BoxModels;
import com.Portality.ccomunityboxes.Ccomunityboxes;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.AbstractEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomBoxVisual extends AbstractEntityVisual<CustomBoxEntity> implements SimpleDynamicVisual {
    public Map<String, TransformedInstance> instances = new HashMap<>();

    public CustomBoxVisual(VisualizationContext ctx, CustomBoxEntity entity, float partialTick) {
        super(ctx, entity, partialTick);

        for(int i = 0; i < Ccomunityboxes.BOXES.length; i++){
            PartialModel model = BoxModels.BOXES.get(Ccomunityboxes.BOXES[i]);
            instances.put(Ccomunityboxes.BOXES[i] ,instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(model))
                    .createInstance());
            instances.get(Ccomunityboxes.BOXES[i]).setVisible(false);
        }

        animate(partialTick);
    }


    @Override
    public void beginFrame(Context ctx) {
        animate(ctx.partialTick());
    }

    private void animate(float partialTick) {
        float yaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());

        Vec3 pos = CustomBoxVisual.this.entity.position();
        var renderOrigin = renderOrigin();
        var x = (float) (Mth.lerp(partialTick, this.entity.xo, pos.x) - renderOrigin.getX());
        var y = (float) (Mth.lerp(partialTick, this.entity.yo, pos.y) - renderOrigin.getY());
        var z = (float) (Mth.lerp(partialTick, this.entity.zo, pos.z) - renderOrigin.getZ());

        long randomBits = (long) entity.getId() * 31L * 493286711L;
        randomBits = randomBits * randomBits * 4392167121L + randomBits * 98761L;
        float xNudge = (((float) (randomBits >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float yNudge = (((float) (randomBits >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float zNudge = (((float) (randomBits >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;

        if(entity.model != null){
            instances.get(entity.model).setVisible(true);
            instances.get(entity.model).setIdentityTransform()
                    .translate(x - 0.5 + xNudge, y + yNudge, z - 0.5 + zNudge)
                    .rotateYCenteredDegrees(-yaw - 90)
                    .light(computePackedLight(partialTick))
                    .setChanged();
        }
    }

    @Override
    protected void _delete() {
        for(int i = 0; i < Ccomunityboxes.BOXES.length; i++){
            instances.get(Ccomunityboxes.BOXES[i]).delete();
        }
    }
}
