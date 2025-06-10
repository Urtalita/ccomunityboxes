package com.Portality.ccomunityboxes;

import com.Portality.ccomunityboxes.boxes.Boxes;
import com.Portality.ccomunityboxes.boxes.gen.ItemModelGenerator;
import com.Portality.ccomunityboxes.boxes.recipes.NBTStonecutterRecipe;
import com.Portality.ccomunityboxes.boxes.summonItems.BoxItems;
import com.Portality.ccomunityboxes.fluid.ModFluids;
import com.Portality.ccomunityboxes.painter.BoxBlockEntity;
import com.Portality.ccomunityboxes.painter.BoxBlocks;
import com.Portality.ccomunityboxes.ponder.BoxPonderPlugin;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.Minecraft;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

import static com.Portality.ccomunityboxes.boxes.summonItems.BoxItems.ITEMS;

// The value here should match an entry in the META-INF/mods.toml file
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
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

    public static final RegistryObject<RecipeSerializer<?>> NBT_STONECUTTING =
            RECIPE_SERIALIZERS.register("nbt_stonecutting", () -> NBTStonecutterRecipe.Serializer.INSTANCE);

    public Ccomunityboxes() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "ccomunityboxes-common.toml");

        RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BoxBlocks.register(modEventBus);
        BoxBlockEntity.register();
        BoxTab.register(modEventBus);
        PonderIndex.addPlugin(new BoxPonderPlugin());

        commonSetup();

        BoxItems.register(modEventBus);
        Boxes.register(modEventBus);

        ModFluids.FLUIDS.register(modEventBus);
        ModFluids.FLUID_TYPES.register(modEventBus);

        modEventBus.addListener(Boxes::registerEntityAttributes);
        BoxModels.register();

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::onGatherData);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        BOX_REGISTRATE.registerEventListeners(modEventBus);
    }

    private static void commonSetup() {
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

    private void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();

        // Генерируем модели предметов
        generator.addProvider(
                event.includeClient(),
                new ItemModelGenerator(
                        packOutput,
                        "ccomunityboxes",
                        helper,
                        (Set<RegistryObject<Item>>) ITEMS.getEntries() // Все зарегистрированные предметы
                )
        );
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
