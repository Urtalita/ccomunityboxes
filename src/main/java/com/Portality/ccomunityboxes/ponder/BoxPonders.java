package com.Portality.ccomunityboxes.ponder;

import com.Portality.ccomunityboxes.painter.BoxBlocks;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class BoxPonders {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(BoxBlocks.PAINTER)
                .addStoryBoard("painter", BoxScenes::painter, AllCreatePonderTags.KINETIC_RELAYS);
        HELPER.forComponents(BoxBlocks.PAINTER)
                .addStoryBoard("painterrare", BoxScenes::painterRare, AllCreatePonderTags.KINETIC_RELAYS);
    }
}
