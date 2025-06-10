package com.Portality.ccomunityboxes.painter;

import com.simibubi.create.content.logistics.packager.PackagerRenderer;
import com.simibubi.create.content.logistics.packager.PackagerVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.Portality.ccomunityboxes.Ccomunityboxes.BOX_REGISTRATE;

public class BoxBlockEntity {
    public static final BlockEntityEntry<PainterBE> PAINTER = BOX_REGISTRATE
            .blockEntity("painter", PainterBE::new)
            .validBlocks(BoxBlocks.PAINTER)
            .renderer(() -> PainterRenderer::new)
            .register();

    public static void register(){}
}
