package com.Portality.ccomunityboxes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = Ccomunityboxes.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> STRING_ARRAY;
    private static List<String> cachedList;
    private static boolean loaded = false;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        STRING_ARRAY = builder
                .comment("allBoxes")
                .defineList("string_array",
                        Arrays.asList(
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
                                "template1",
                                "template2",
                                "template3",
                                "template4",
                                "template5"),
                        entry -> entry instanceof String && ((String) entry).matches("[a-z0-9_]+")
                );

        SPEC = builder.build();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    }

    public static void updateCache() {
        cachedList = Collections.unmodifiableList(STRING_ARRAY.get());
    }

    public static List<String> getStringList() {
        return cachedList;
    }

    public static List<String> getStringArraySafe() {
        if (!loaded) {
            throw new IllegalStateException("Config accessed before loading!");
        }
        return Collections.unmodifiableList(STRING_ARRAY.get());
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == SPEC) {
            loaded = true;
        }
    }

    @SubscribeEvent
    public static void onConfigChanged(ModConfigEvent event) {
        if (event.getConfig().getSpec() == Config.SPEC) {
            String[] updatedArray = Config.STRING_ARRAY.get().toArray(new String[0]);
            // Обновить логику мода
        }
    }
}
