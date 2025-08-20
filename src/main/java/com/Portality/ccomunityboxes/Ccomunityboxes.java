package com.Portality.ccomunityboxes;

import com.Portality.ccomunityboxes.boxes.Boxes;
import com.Portality.ccomunityboxes.boxes.recipes.BoxRecipes;
import com.Portality.ccomunityboxes.boxes.recipes.NBTStonecutterRecipe;
import com.Portality.ccomunityboxes.boxes.summonItems.BoxItems;
import com.Portality.ccomunityboxes.fluid.ModFluids;
import com.Portality.ccomunityboxes.painter.BoxBlockEntity;
import com.Portality.ccomunityboxes.painter.BoxBlocks;
import com.Portality.ccomunityboxes.painter.PainterBE;
import com.Portality.ccomunityboxes.ponder.BoxPonderPlugin;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.util.Random;

import static com.Portality.ccomunityboxes.BoxTab.getItemFromName;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Ccomunityboxes.MODID)
public class Ccomunityboxes {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "ccomunityboxes";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final CreateRegistrate BOX_REGISTRATE = CreateRegistrate.create(Ccomunityboxes.MODID);
    public static String[] BOXES;
    public static String[] RARE_BOXES;

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, MODID);

    public Ccomunityboxes(IEventBus modEventBus, ModContainer modContainer) {
        arraysRegister();

        // Сначала регистрируем все базовые компоненты
        BoxItems.register(modEventBus);
        BoxBlocks.register(modEventBus);
        BoxBlockEntity.register(); // Убедитесь, что это регистрирует BlockEntityType

        // Затем регистрируем обработчики Registrate ДО capabilities
        BOX_REGISTRATE.registerEventListeners(modEventBus);

        // Теперь регистрируем capabilities
        modEventBus.addListener(PainterBE::registerCapabilities);

        // Остальная регистрация
        RECIPE_SERIALIZERS.register(modEventBus);
        ModFluids.register(modEventBus);
        BoxRecipes.register(modEventBus);

        Boxes.register(modEventBus);
        modEventBus.addListener(Boxes::registerEntityAttributes);

        BoxModels.register();
        BoxTab.register(modEventBus);
    }

    private void arraysRegister() {
        BOXES = new String[]{
                "box",
                "dzadok",
                "ulter",
                "choko",
                "slime",
                "csqrb",
                "roulete",
                "brass",
                "kiril",
                "kiril2",
                "breaze",
                "love",
                "qves",
                "pelme",
                "some",
                "cold",
                "tomat",
                "tochn",
                "portal",
                "maxi",
                "card",
                "nikok",
                "bearing",
                "dodobox",
                "mangtea",
                "chell",
                "ccomunity",
                "red",
                "fox",
                "schem",
                "lime",
                "nosome",
                "copp",
                "crafter_package",
                "template1",
                "template2",
                "template3"
        };

        RARE_BOXES = new String[]{
                "create:rare_creeper_package",
                "create:rare_darcy_package",
                "create:rare_evan_package",
                "create:rare_jinx_package",
                "create:rare_kryppers_package",
                "create:rare_simi_package",
                "create:rare_starlotte_package",
                "create:rare_thunder_package",
                "create:rare_up_package",
                "create:rare_vector_package"
        };
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            PonderIndex.addPlugin(new BoxPonderPlugin());
        }
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static ItemStack getRandomBox(RandomSource random){
        return getItemFromName(Ccomunityboxes.BOXES[random.nextInt(0, Ccomunityboxes.BOXES.length-1)]);
    }

    public static ItemStack getItemFromNameClear(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            return null;
        }

        ResourceLocation resLoc = ResourceLocation.tryParse(itemName);
        if (resLoc == null) {
            return null;
        }

        return new ItemStack(BuiltInRegistries.ITEM.get(resLoc));
    }

    public static ItemStack getRandomRareBox(RandomSource random){
        return getItemFromNameClear(Ccomunityboxes.RARE_BOXES[random.nextInt(0, Ccomunityboxes.RARE_BOXES.length-1)]);
    }
}
