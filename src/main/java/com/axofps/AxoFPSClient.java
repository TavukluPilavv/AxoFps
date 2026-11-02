package com.axofps;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public final class AxoFPSClient implements ClientModInitializer {
    private KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.axofps.menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_CONTROL,
                "category.axofps"
        ));

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(literal("axofps").executes(context -> {
                    MinecraftClientHolder.openMenu();
                    context.getSource().sendFeedback(Text.literal("§bAxoFPS §7menüsü açılıyor..."));
                    return 1;
                }))
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new AxoFPSScreen());
                }
            }
        });
    }

    private static final class MinecraftClientHolder {
        static void openMenu() {
            net.minecraft.client.MinecraftClient.getInstance().setScreen(new AxoFPSScreen());
        }
    }
}
