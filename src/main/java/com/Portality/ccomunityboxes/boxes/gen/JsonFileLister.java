package com.Portality.ccomunityboxes.boxes.gen;
import com.Portality.ccomunityboxes.Ccomunityboxes;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class JsonFileLister {
    private static final Logger LOGGER = LogManager.getLogger();

    @OnlyIn(Dist.CLIENT)
    public static String[] getBlockTextureFiles() {
        List<String> fileNames = new ArrayList<>();
        String modId = Ccomunityboxes.MODID;

        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

        String texturePath = "textures/block";

        Predicate<ResourceLocation> filter =
                (resourceLocation) -> resourceLocation.getNamespace().equals(modId);

        Map<ResourceLocation, Resource> resources = resourceManager.listResources(texturePath, filter);
        resources.forEach((resourceLocation, resource) -> {
            String[] pathParts = resourceLocation.getPath().split("/");
            if (pathParts.length > 0) {
                fileNames.add(pathParts[pathParts.length - 1].split("\\.")[0]);
            }
        });

        return fileNames.toArray(new String[0]);
    }

    // Обёртка для безопасного вызова на клиенте
    public static String[] getBlockTextureFilesSafe() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return getBlockTextureFiles();
        }
        return new String[]{};
    }
}
