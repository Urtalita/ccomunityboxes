package com.Portality.ccomunityboxes.mixins;

import com.Portality.ccomunityboxes.boxes.summonItems.SummonBoxItem;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Mixin(targets = "com.simibubi.create.AllCreativeModeTabs$RegistrateDisplayItemsGenerator")
public abstract class CreativeTabMixin {

    @Inject(
            method = "accept",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/AllCreativeModeTabs$RegistrateDisplayItemsGenerator;outputAll(Lnet/minecraft/world/item/CreativeModeTab$Output;Ljava/util/List;Ljava/util/function/Function;Ljava/util/function/Function;)V",
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void beforeOutput(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output,
                              CallbackInfo ci, Predicate exclusionPredicate, List orderings, Function stackFunc,
                              Function visibilityFunc, List items) {

        items.removeIf(item -> item instanceof SummonBoxItem);
    }
}
