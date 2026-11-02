package com.axofps;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.text.Text;

public final class AxoFPSScreen extends Screen {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private boolean performanceProfile = true;

    public AxoFPSScreen() {
        super(Text.literal("AxoFPS Settings"));
    }

    @Override
    protected void init() {
        int x = width / 2 - 100;
        int y = height / 4;

        addDrawableChild(ButtonWidget.builder(profileLabel(), button -> {
            performanceProfile = !performanceProfile;
            applyProfile();
            button.setMessage(profileLabel());
        }).dimensions(x, y, 200, 20).build());

        boolean minimal = client.options.getParticles().getValue() == ParticlesMode.MINIMAL;
        addDrawableChild(ButtonWidget.builder(particleLabel(minimal), button -> {
            boolean nextMinimal = client.options.getParticles().getValue() != ParticlesMode.MINIMAL;
            client.options.getParticles().setValue(nextMinimal ? ParticlesMode.MINIMAL : ParticlesMode.ALL);
            button.setMessage(particleLabel(nextMinimal));
        }).dimensions(x, y + 26, 200, 20).build());

        boolean cloudsOff = client.options.getCloudRenderMode().getValue() == CloudRenderMode.OFF;
        addDrawableChild(ButtonWidget.builder(cloudLabel(cloudsOff), button -> {
            boolean nextOff = client.options.getCloudRenderMode().getValue() != CloudRenderMode.OFF;
            client.options.getCloudRenderMode().setValue(nextOff ? CloudRenderMode.OFF : CloudRenderMode.FANCY);
            button.setMessage(cloudLabel(nextOff));
        }).dimensions(x, y + 52, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> close())
                .dimensions(x, y + 82, 200, 20).build());
    }

    private Text profileLabel() {
        return Text.literal("Performance Profile: " + (performanceProfile ? "ON" : "OFF"));
    }

    private Text particleLabel(boolean minimal) {
        return Text.literal("Particles: " + (minimal ? "Minimal" : "All"));
    }

    private Text cloudLabel(boolean off) {
        return Text.literal("Clouds: " + (off ? "Off" : "Fancy"));
    }

    private void applyProfile() {
        client.options.getParticles().setValue(performanceProfile ? ParticlesMode.MINIMAL : ParticlesMode.ALL);
        client.options.getCloudRenderMode().setValue(performanceProfile ? CloudRenderMode.OFF : CloudRenderMode.FANCY);
    }

    @Override
    public void close() {
        client.setScreen(null);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
