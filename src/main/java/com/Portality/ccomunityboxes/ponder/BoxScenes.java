package com.Portality.ccomunityboxes.ponder;

import com.Portality.ccomunityboxes.Ccomunityboxes;
import com.Portality.ccomunityboxes.painter.PainterBE;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class BoxScenes {
    private static final RandomSource RANDOM = RandomSource.create();

    public static void painter(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("painting", "painting boxes using industrial painter");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.showBasePlate();

        scene.rotateCameraY(180);
        scene.idle(10);

        for (int i = 4; i >= 0; i--) {
            scene.idle(5);
            scene.world().showSection(util.select().position(i, 1, 2), Direction.DOWN);
        }
        scene.idle(5);
        scene.world().showSection(util.select().position(2, 2, 2), Direction.DOWN);

        scene.idle(15);
        scene.overlay().showText(20)
                .placeNearTarget()
                .text("")
                .attachKeyFrame()
                .pointAt(util.vector().of(2, 2, 2));
        scene.idle(30);

        scene.world().showSection(util.select().position(2, 3, 2), Direction.DOWN);
        scene.idle(15);
        scene.overlay().showText(20)
                .placeNearTarget()
                .text("")
                .pointAt(util.vector().of(2, 3, 2));
        scene.idle(30);

        scene.world().showSection(util.select().position(1, 1, 1), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(2, 1, 1), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(3, 1, 1), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(2, 2, 1), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(2, 3, 1), Direction.DOWN);
        scene.idle(5);

        scene.idle(15);
        scene.overlay().showText(20)
                .placeNearTarget()
                .text("")
                .attachKeyFrame()
                .pointAt(util.vector().of(2, 3, 1));
        scene.idle(30);

        scene.world().showSection(util.select().position(1, 2, 2), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(3, 2, 2), Direction.DOWN);
        scene.idle(5);

        scene.idle(15);
        scene.overlay().showText(20)
                .placeNearTarget()
                .text("")
                .attachKeyFrame()
                .pointAt(util.vector().of(2, 2, 2));
        scene.idle(30);

        ItemStack filter = getRandomBox();
        scene.world().modifyBlockEntityNBT(util.select().position(2, 2, 2), PainterBE.class,
                nbt -> nbt.put("Filter", filter.serializeNBT()),false);

        scene.idle(30);
        scene.rotateCameraY(45);
        scene.idle(20);

        ItemStack box = getRandomBox();
        scene.world().createItemOnBelt(util.grid().at(0, 1, 2), Direction.DOWN, box);
        scene.idle(27);
        scene.world().removeItemsFromBelt(util.grid().at(1, 1, 2));

        scene.world().modifyBlockEntityNBT(util.select().position(2, 2, 2), PainterBE.class,
                nbt -> nbt.put("heldStack", box.serializeNBT()),false);

        for (int i = 0; i <= 30; i++) {
            int finalI = i;

            if(i == 5){
                scene.world().modifyBlockEntityNBT(util.select().position(2, 3, 2), SpoutBlockEntity.class, nbt -> nbt.putInt("ProcessingTicks", 20));
            }

            if(i == 15){
                scene.world().modifyBlockEntityNBT(util.select().position(2, 2, 2), PainterBE.class,
                        nbt -> nbt.put("heldStack", filter.serializeNBT()),false);
            }

            scene.world().modifyBlockEntityNBT(util.select().position(2, 2, 2), PainterBE.class,
                    nbt -> nbt.putInt("craftProgress", finalI),false);
            scene.idle(1);
        }
        scene.world().modifyBlockEntityNBT(util.select().position(2, 2, 2), PainterBE.class,
                nbt -> nbt.put("heldStack", ItemStack.EMPTY.serializeNBT()),false);

        scene.world().createItemOnBelt(util.grid().at(3, 1, 2), Direction.DOWN, filter);

        scene.idle(40);
        scene.world().modifyBlockEntityNBT(util.select().position(2, 2, 2), PainterBE.class,
                nbt -> nbt.putInt("craftProgress", 0),false);
        scene.world().removeItemsFromBelt(util.grid().at(4, 1, 2));


        scene.world().modifyBlockEntityNBT(util.select().position(2, 2, 2), PainterBE.class,
                nbt -> nbt.put("Filter", ItemStack.EMPTY.serializeNBT()),false);
        scene.rotateCameraY(-45);

        scene.idle(15);
        scene.overlay().showText(40)
                .placeNearTarget()
                .text("")
                .attachKeyFrame()
                .pointAt(util.vector().of(2, 2, 2));
        scene.idle(50);

        handlePainterLogicNoFilter(builder, util, scene, ItemStack.EMPTY, getRandomBox(), false);
        scene.markAsFinished();
    }

    public static void painterRare(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("paintingrare", "painting boxes using industrial painter");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.showBasePlate();

        Selection painter = util.select().position(2, 2, 2);
        Selection spout = util.select().position(2, 3, 2);

        Selection belt1Start = util.select().position(0, 3, 2);
        Selection belt1End = util.select().position(1, 1, 2);

        Selection belt2Start = util.select().position(3, 3, 2);
        Selection belt2End = util.select().position(4, 1, 2);

        scene.rotateCameraY(180);
        scene.idle(10);

        for (int i = 4; i >= 0; i--) {
            scene.idle(3);
            scene.world().showSection(util.select().position(i, 1, 2), Direction.DOWN);
        }

        scene.idle(3);
        scene.world().showSection(painter, Direction.DOWN);

        scene.idle(3);
        scene.world().showSection(spout, Direction.DOWN);

        scene.idle(3);
        scene.world().showSection(util.select().position(1, 2, 2), Direction.DOWN);

        scene.idle(3);
        scene.world().showSection(util.select().position(3, 2, 2), Direction.DOWN);

        scene.idle(15);
        scene.overlay().showText(40)
                .placeNearTarget()
                .text("")
                .attachKeyFrame()
                .pointAt(util.vector().of(2, 2, 2));
        scene.idle(50);

        handlePainterLogic(builder, util, scene, getRandomRareBox(), getRandomBox(), true);

        scene.idle(15);
        scene.overlay().showText(40)
                .placeNearTarget()
                .text("")
                .attachKeyFrame()
                .pointAt(util.vector().of(2, 2, 2));
        scene.idle(50);

        scene.world().modifyBlockEntityNBT(util.select().position(2, 2, 2), PainterBE.class,
                nbt -> nbt.put("Filter", ItemStack.EMPTY.serializeNBT()),false);

        handlePainterLogicNoFilter(builder, util, scene, getRandomRareBox(), getRandomBox(), true);
        scene.idle(15);
        scene.markAsFinished();
    }

    public static CompoundTag PutInk(CompoundTag nbt){
        CompoundTag tankContent = nbt.getCompound("TankContent");
        tankContent.putString("FluidName", "ccomunityboxes:ink_fluid");
        nbt.put("TankContent", tankContent);
        return nbt;
    }

    public static void handlePainterLogic(SceneBuilder builder, SceneBuildingUtil util, CreateSceneBuilder scene, ItemStack filter, ItemStack box, boolean rareAlowed){
        scene.world().modifyBlockEntityNBT(util.select().position(2, 2, 2), PainterBE.class,
                nbt -> nbt.put("Filter", filter.serializeNBT()),false);

        handlePainterLogicNoFilter(builder, util, scene, filter, box, rareAlowed);
    }

    public static void handlePainterLogicNoFilter(SceneBuilder builder, SceneBuildingUtil util, CreateSceneBuilder scene, ItemStack filter, ItemStack box, boolean rareAlowed){
        scene.rotateCameraY(45);
        scene.idle(20);
        ItemStack randomBox = getRandomBox();

        scene.world().createItemOnBelt(util.grid().at(0, 1, 2), Direction.DOWN, box);
        scene.idle(27);
        scene.world().removeItemsFromBelt(util.grid().at(1, 1, 2));

        scene.world().modifyBlockEntityNBT(util.select().position(2, 2, 2), PainterBE.class,
                nbt -> nbt.put("heldStack", box.serializeNBT()),false);

        for (int i = 0; i <= 30; i++) {
            int finalI = i;

            if(i == 5){
                scene.world().modifyBlockEntityNBT(util.select().position(2, 3, 2), SpoutBlockEntity.class, nbt -> nbt.putInt("ProcessingTicks", 20));
            }

            if(i == 15){
                if(rareAlowed){
                    scene.world().modifyBlockEntityNBT(util.select().position(2, 2, 2), PainterBE.class,
                            nbt -> nbt.put("heldStack", filter.serializeNBT()),false);
                } else {
                    scene.world().modifyBlockEntityNBT(util.select().position(2, 2, 2), PainterBE.class,
                            nbt -> nbt.put("heldStack", randomBox.serializeNBT()),false);
                }
            }

            scene.world().modifyBlockEntityNBT(util.select().position(2, 2, 2), PainterBE.class,
                    nbt -> nbt.putInt("craftProgress", finalI),false);
            scene.idle(1);
        }
        scene.world().modifyBlockEntityNBT(util.select().position(2, 2, 2), PainterBE.class,
                nbt -> nbt.put("heldStack", ItemStack.EMPTY.serializeNBT()),false);

        if(rareAlowed){
            scene.world().createItemOnBelt(util.grid().at(3, 1, 2), Direction.DOWN, filter);
        } else {
            scene.world().createItemOnBelt(util.grid().at(3, 1, 2), Direction.DOWN, randomBox);
        }

        scene.idle(40);
        scene.world().modifyBlockEntityNBT(util.select().position(2, 2, 2), PainterBE.class,
                nbt -> nbt.putInt("craftProgress", 0),false);
        scene.world().removeItemsFromBelt(util.grid().at(4, 1, 2));
        scene.idle(10);

        scene.rotateCameraY(-45);
    }

    public static ItemStack getRandomBox(){
        return getItemFromName(Ccomunityboxes.BOXES[RANDOM.nextInt(0, Ccomunityboxes.BOXES.length-1)]);
    }

    public static ItemStack getRandomRareBox(){
        return getItemFromNameClear(Ccomunityboxes.RARE_BOXES[RANDOM.nextInt(0, Ccomunityboxes.RARE_BOXES.length-1)]);
    }

    public static ItemStack getItemFromNameClear(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            return null;
        }

        ResourceLocation resLoc = ResourceLocation.tryParse(itemName);
        if (resLoc == null) {
            return null;
        }

        return new ItemStack(ForgeRegistries.ITEMS.getValue(resLoc));
    }

    public static ItemStack getItemFromName(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            return null;
        }

        itemName = Ccomunityboxes.MODID + ":" + itemName;

        ResourceLocation resLoc = ResourceLocation.tryParse(itemName);
        if (resLoc == null) {
            return null;
        }

        return new ItemStack(ForgeRegistries.ITEMS.getValue(resLoc));
    }
}
