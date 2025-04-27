package com.Portality.ccomunityboxes.boxes.gen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;

public class JsonFileLister {
    private static final Logger LOGGER = LogManager.getLogger();

    public static String[] getJsonFilesInDirectory(String modId, String directory) {
        ArrayList<String> fileNames = new ArrayList<>();
        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();

        // Нормализация пути
        String targetPath = (directory.endsWith("/") ? directory : directory + "/").toLowerCase();
        String resourceNamespace = modId.toLowerCase();

        LOGGER.info("Searching in: {}:{}", resourceNamespace, targetPath);

        try {
            // Получаем все ресурсы в неймспейсе мода
            Collection<ResourceLocation> allResources = resourceManager.listResources(resourceNamespace, rl -> true).keySet();

            for (ResourceLocation rl : allResources) {
                String path = rl.getPath().toLowerCase();
                LOGGER.debug("Checking: {}", path);

                if (path.startsWith(targetPath) && path.endsWith(".png")) {
                    String fileNameWithExt = path.substring(path.lastIndexOf('/') + 1);
                    String fileName = fileNameWithExt.substring(0, fileNameWithExt.lastIndexOf('.'));
                    LOGGER.info("Found file: {}", fileName);
                    fileNames.add(fileName);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error listing resources", e);
        }

        LOGGER.info("Found {} files", fileNames.size());
        return fileNames.toArray(new String[0]);
    }
}
