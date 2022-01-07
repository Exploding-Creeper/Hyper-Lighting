package me.hypherionmc.hyperlighting.util;

import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.IEnvironment;
import me.hypherionmc.hyperlighting.HyperLighting;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/** Hacks and utils for Optifu.... I mean optifine */
public class OptiHacks {

    private static boolean hasOptiFine = false;

    public static void checkOptifine() {
        List<Map<String, String>> modlist = Launcher.INSTANCE.environment().getProperty(IEnvironment.Keys.MODLIST.get()).orElseThrow(Error::new);

        for (Map<String, String> data : modlist) {
            String type = data.get("type");
            if (type == null || !type.equals("TRANSFORMATIONSERVICE")) {
                continue;
            }
            String name = data.get("name");
            if (name == null || !name.equals("OptiFine")) {
                continue;
            }
            hasOptiFine = true;

            HyperLighting.logger.warn("***********************************************************************");
            HyperLighting.logger.warn("*     Optifine Detected! Attempting to support it as best possible    *");
            HyperLighting.logger.warn("***********************************************************************");

        }
    }

    public static boolean isRenderRegions() {
        try {
            Class ofConfigClass = Class.forName("net.optifine.Config");
            Method rrField = ofConfigClass.getMethod("isRenderRegions");
            return (boolean) rrField.invoke(null);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            // Optifine is probably not present. Ignore the error
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasOptiFine() {
        return hasOptiFine;
    }
}
