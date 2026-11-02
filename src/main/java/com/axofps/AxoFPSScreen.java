package com.axofps;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.text.Text;

public final class AxoFPSScreen extends Screen {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private boolean superMode = false;
    private boolean entityCullingInfo = false;
    private String status = "Hazır";

    public AxoFPSScreen() {
        super(Text.literal("AxoFPS • FPS Optimizasyonu"));
    }

    @Override
    protected void init() {
        int x = width / 2 - 105;
        int y = Math.max(24, height / 6);

        addDrawableChild(ButtonWidget.builder(Text.literal("§aSüper Optimizasyon: " + (superMode ? "AÇIK" : "KAPALI")), b -> {
            superMode = !superMode;
            if (superMode) {
                client.options.getParticles().setValue(ParticlesMode.MINIMAL);
                client.options.getCloudRenderMode().setValue(CloudRenderMode.OFF);
                status = "Süper optimizasyon açıldı! FPS artışı bekleniyor.";
                client.setScreen(new AxoFPSConfirmScreen());
            } else {
                client.options.getParticles().setValue(ParticlesMode.ALL);
                client.options.getCloudRenderMode().setValue(CloudRenderMode.FANCY);
                status = "Süper optimizasyon kapatıldı.";
                init();
            }
        }).dimensions(x, y, 210, 20).build());

        y += 26;
        addDrawableChild(ButtonWidget.builder(Text.literal("Parçacıklar: " + (client.options.getParticles().getValue() == ParticlesMode.MINIMAL ? "Az" : "Tümü")), b -> {
            boolean minimal = client.options.getParticles().getValue() != ParticlesMode.MINIMAL;
            client.options.getParticles().setValue(minimal ? ParticlesMode.MINIMAL : ParticlesMode.ALL);
            b.setMessage(Text.literal("Parçacıklar: " + (minimal ? "Az" : "Tümü")));
        }).dimensions(x, y, 210, 20).build());

        y += 26;
        addDrawableChild(ButtonWidget.builder(Text.literal("Bulutlar: " + (client.options.getCloudRenderMode().getValue() == CloudRenderMode.OFF ? "Kapalı" : "Açık")), b -> {
            boolean off = client.options.getCloudRenderMode().getValue() != CloudRenderMode.OFF;
            client.options.getCloudRenderMode().setValue(off ? CloudRenderMode.OFF : CloudRenderMode.FANCY);
            b.setMessage(Text.literal("Bulutlar: " + (off ? "Kapalı" : "Açık")));
        }).dimensions(x, y, 210, 20).build());

        y += 26;
        addDrawableChild(ButtonWidget.builder(Text.literal("Entity Culling hakkında"), b -> {
            entityCullingInfo = !entityCullingInfo;
            status = entityCullingInfo ? "Entity Culling gerçek görünürlük optimizasyonu için ayrı mod gerektirir." : "Hazır";
        }).dimensions(x, y, 210, 20).build());

        y += 26;
        addDrawableChild(ButtonWidget.builder(Text.literal("§eModüller / optimizasyon bilgisi"), b -> {
            status = "AxoFPS ayarları istemci tarafında uygulanır.";
        }).dimensions(x, y, 210, 20).build());

        y += 26;
        addDrawableChild(ButtonWidget.builder(Text.literal("Kapat"), b -> close())
                .dimensions(x, y, 210, 20).build());
    }

    @Override
    public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("AxoFPS • Türkçe Optimizasyon Menüsü"), width / 2, 12, 0x55FFFF);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(status), width / 2, height - 28, 0xFFFFFF);
    }

    @Override
    public void close() {
        client.setScreen(null);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private final class AxoFPSConfirmScreen extends Screen {
        AxoFPSConfirmScreen() {
            super(Text.literal("Süper Optimizasyon"));
        }

        @Override
        protected void init() {
            int x = width / 2 - 115;
            addDrawableChild(ButtonWidget.builder(Text.literal("Tamam"), b -> {
                client.setScreen(AxoFPSScreen.this);
                status = "İyi oyunlar TavukluPilavvV2! Süper optimizasyon açıldı, FPS artışı bekleniyor.";
                AxoFPSScreen.this.init();
            }).dimensions(x, height / 2 + 20, 230, 20).build());
        }

        @Override
        public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
            renderBackground(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§aSüper optimizasyon açıldı!"), width / 2, height / 2 - 30, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("FPS artışı bekleniyor."), width / 2, height / 2 - 10, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("İyi oyunlar TavukluPilavvV2"), width / 2, height / 2 + 5, 0x55FFFF);
            super.render(context, mouseX, mouseY, delta);
        }

        @Override
        public boolean shouldPause() { return false; }
    }
}
