package com.Portality.ccomunityboxes.util;

import com.Portality.ccomunityboxes.Ccomunityboxes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BoxTag {
    public static class Items{
        public static final TagKey<Item> METAL_DETECTOR_VALUABLES = tag("boxes");
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(Ccomunityboxes.MODID, name));
        }
    }
}
