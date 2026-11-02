package com.axofps;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.option.ParticlesMode;
import net.minecraft.text.Text;

public final class AxoFPSScreen extends Screen {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private String status = "AxoFPS hazır • Ayarlar anında uygulanır";

    public AxoFPSScreen() {
        super(Text.literal("AxoFPS"));
    }

    @Override
    protected void init() {
        clearChildren();
        int bw = Math.min(250, width - 24);
        int x = width / 2 - bw / 2;
        int y = Math.max(30, height / 6);
        int gap = 22;

        addDrawableChild(ButtonWidget.builder(Text.literal("§c§lULTRA SÜPER OPTİMİZASYON"), b -> {
            applyUltra();
            status = "§aULTRA SÜPER OPTİMİZASYON aktif! Ayarlar uygulandı.";
            client.options.write();
            client.setScreen(new AxoFPSConfirmScreen());
        }).dimensions(x, y, bw, 20).build());
        y += 26;

        addDrawableChild(ButtonWidget.builder(Text.literal("§eRender Distance: " + client.options.getViewDistance().getValue() + " chunk"), b -> {
            int next = client.options.getViewDistance().getValue() <= 6 ? 10 : client.options.getViewDistance().getValue() <= 10 ? 14 : 6;
            client.options.getViewDistance().setValue(next); b.setMessage(Text.literal("Render Distance: " + next + " chunk")); client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        addDrawableChild(ButtonWidget.builder(Text.literal("Simülasyon Mesafesi: " + client.options.getSimulationDistance().getValue()), b -> {
            int next = client.options.getSimulationDistance().getValue() <= 5 ? 8 : 5;
            client.options.getSimulationDistance().setValue(next); b.setMessage(Text.literal("Simülasyon Mesafesi: " + next)); client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        addDrawableChild(ButtonWidget.builder(Text.literal("Parçacıklar: " + particleName()), b -> {
            ParticlesMode next = client.options.getParticles().getValue() == ParticlesMode.MINIMAL ? ParticlesMode.DECREASED : ParticlesMode.MINIMAL;
            client.options.getParticles().setValue(next); b.setMessage(Text.literal("Parçacıklar: " + particleName())); client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        addDrawableChild(ButtonWidget.builder(Text.literal("Bulutlar: " + cloudName()), b -> {
            CloudRenderMode next = client.options.getCloudRenderMode().getValue() == CloudRenderMode.OFF ? CloudRenderMode.FAST : CloudRenderMode.OFF;
            client.options.getCloudRenderMode().setValue(next); b.setMessage(Text.literal("Bulutlar: " + cloudName())); client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        addDrawableChild(ButtonWidget.builder(Text.literal("Varlık Gölgeleri: " + (client.options.getEntityShadows().getValue() ? "Açık" : "Kapalı")), b -> {
            boolean next = !client.options.getEntityShadows().getValue();
            client.options.getEntityShadows().setValue(next); b.setMessage(Text.literal("Varlık Gölgeleri: " + (next ? "Açık" : "Kapalı"))); client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        addDrawableChild(ButtonWidget.builder(Text.literal("FPS Limiti: " + client.options.getMaxFps().getValue()), b -> {
            int current = client.options.getMaxFps().getValue();
            int next = current <= 60 ? 120 : current <= 120 ? 260 : 60;
            client.options.getMaxFps().setValue(next); b.setMessage(Text.literal("FPS Limiti: " + next)); client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        addDrawableChild(ButtonWidget.builder(Text.literal("§bAxoFPS • Kapat"), b -> close()).dimensions(x, y, bw, 18).build());
    }

    private String particleName() {
        return switch (client.options.getParticles().getValue()) {
            case MINIMAL -> "Minimum";
            case DECREASED -> "Azaltılmış";
            default -> "Tümü";
        };
    }

    private String cloudName() {
        return client.options.getCloudRenderMode().getValue() == CloudRenderMode.OFF ? "Kapalı" : "Açık";
    }

    private void applyUltra() {
        client.options.getViewDistance().setValue(6);
        client.options.getSimulationDistance().setValue(5);
        client.options.getParticles().setValue(ParticlesMode.MINIMAL);
        client.options.getCloudRenderMode().setValue(CloudRenderMode.OFF);
        client.options.getEntityShadows().setValue(false);
        client.options.getBiomeBlendRadius().setValue(0);
        client.options.write();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.fill(width / 2 - 145, 6, width / 2 + 145, height - 6, 0xB0101725);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("§b§lAxoFPS §7| §fPerformans Merkezi"), width / 2, 12, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(status), width / 2, height - 15, 0xFFFFFF);
    }

    @Override public void close() { client.options.write(); client.setScreen(null); }
    @Override public boolean shouldPause() { return false; }

    private final class AxoFPSConfirmScreen extends Screen {
        AxoFPSConfirmScreen() { super(Text.literal("AxoFPS Ultra")); }

        @Override protected void init() {
            addDrawableChild(ButtonWidget.builder(Text.literal("§aTamam, devam!"), b -> client.setScreen(AxoFPSScreen.this))
                    .dimensions(width / 2 - 100, height / 2 + 35, 200, 20).build());
        }

        @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            renderBackground(context, mouseX, mouseY, delta);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§c§lULTRA SÜPER OPTİMİZASYON"), width / 2, height / 2 - 35, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§aAyarlar başarıyla uygulandı!"), width / 2, height / 2 - 15, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§bRender 6 • Simülasyon 5 • Parçacık minimum"), width / 2, height / 2 + 5, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("Sodium ile birlikte 10-15+ FPS kazancı sağlanacaktır."), width / 2, height / 2 + 20, 0xFFFFFF);
            super.render(context, mouseX, mouseY, delta);
        }
        @Override public boolean shouldPause() { return false; }
    }
}

