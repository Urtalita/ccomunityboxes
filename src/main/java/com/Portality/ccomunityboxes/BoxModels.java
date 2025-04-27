package com.Portality.ccomunityboxes;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

import static com.Portality.ccomunityboxes.Ccomunityboxes.MODID;

public class BoxModels {
    public static Map<String, PartialModel> BOXES = new HashMap<>();

    private static PartialModel block(String path) {
        return PartialModel.of(Ccomunityboxes.asResource(path));
    }
    public static void register(){
        for(int i = 0; i < Ccomunityboxes.BOXES.length; i++){
            BOXES.put(Ccomunityboxes.BOXES[i], block(Ccomunityboxes.BOXES[i]));
        }
    }
}
