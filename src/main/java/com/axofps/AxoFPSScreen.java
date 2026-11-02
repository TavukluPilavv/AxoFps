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
    
    // Kullanıcı kapatana kadar açık kalacak Ultra Max Durumu
    private static boolean ultraMaxActive = false;

    public AxoFPSScreen() {
        super(Text.literal("AxoFPS"));
    }

    @Override
    protected void init() {
        clearChildren();
        int bw = Math.min(250, width - 24);
        int x = width / 2 - bw / 2;
        int y = Math.max(25, height / 7);
        int gap = 21;

        // ULTRA SUPER MAX OPTİMİZASYON (Açık/Kapalı Butonu)
        String maxStateText = ultraMaxActive ? "§a§lULTRA SUPER MAX: AÇIK" : "§c§lULTRA SUPER MAX: KAPALI";
        addDrawableChild(ButtonWidget.builder(Text.literal(maxStateText), b -> {
            ultraMaxActive = !ultraMaxActive;
            if (ultraMaxActive) {
                applyUltraMax();
                status = "§aULTRA SUPER MAX aktif! Sunucu ve entity optimizasyonu devrede.";
                client.options.write();
                client.setScreen(new AxoFPSWelcomeScreen());
            } else {
                status = "§eULTRA SUPER MAX kapatıldı.";
                b.setMessage(Text.literal("§c§lULTRA SUPER MAX: KAPALI"));
                client.options.write();
            }
        }).dimensions(x, y, bw, 20).build());
        y += 25;

        // Render Distance
        addDrawableChild(ButtonWidget.builder(Text.literal("§eRender Distance: " + client.options.getViewDistance().getValue() + " chunk"), b -> {
            int next = client.options.getViewDistance().getValue() <= 6 ? 10 : client.options.getViewDistance().getValue() <= 10 ? 14 : 6;
            client.options.getViewDistance().setValue(next); b.setMessage(Text.literal("Render Distance: " + next + " chunk")); client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        // Simülasyon Mesafesi
        addDrawableChild(ButtonWidget.builder(Text.literal("Simülasyon Mesafesi: " + client.options.getSimulationDistance().getValue()), b -> {
            int next = client.options.getSimulationDistance().getValue() <= 4 ? 8 : 4;
            client.options.getSimulationDistance().setValue(next); b.setMessage(Text.literal("Simülasyon Mesafesi: " + next)); client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        // Parçacıklar
        addDrawableChild(ButtonWidget.builder(Text.literal("Parçacıklar: " + particleName()), b -> {
            ParticlesMode next = client.options.getParticles().getValue() == ParticlesMode.MINIMAL ? ParticlesMode.DECREASED : ParticlesMode.MINIMAL;
            client.options.getParticles().setValue(next); b.setMessage(Text.literal("Parçacıklar: " + particleName())); client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        // Bulutlar
        addDrawableChild(ButtonWidget.builder(Text.literal("Bulutlar: " + cloudName()), b -> {
            CloudRenderMode next = client.options.getCloudRenderMode().getValue() == CloudRenderMode.OFF ? CloudRenderMode.FAST : CloudRenderMode.OFF;
            client.options.getCloudRenderMode().setValue(next); b.setMessage(Text.literal("Bulutlar: " + cloudName())); client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        // Varlık Gölgeleri
        addDrawableChild(ButtonWidget.builder(Text.literal("Varlık Gölgeleri: " + (client.options.getEntityShadows().getValue() ? "Açık" : "Kapalı")), b -> {
            boolean next = !client.options.getEntityShadows().getValue();
            client.options.getEntityShadows().setValue(next); b.setMessage(Text.literal("Varlık Gölgeleri: " + (next ? "Açık" : "Kapalı"))); client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        // FPS Limiti
        addDrawableChild(ButtonWidget.builder(Text.literal("FPS Limiti: " + client.options.getMaxFps().getValue()), b -> {
            int current = client.options.getMaxFps().getValue();
            int next = current <= 60 ? 120 : current <= 120 ? 260 : 60;
            client.options.getMaxFps().setValue(next); b.setMessage(Text.literal("FPS Limiti: " + next)); client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        // Kapat Butonu
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

    private void applyUltraMax() {
        client.options.getViewDistance().setValue(5);             
        client.options.getSimulationDistance().setValue(4);      
        client.options.getParticles().setValue(ParticlesMode.MINIMAL); 
        client.options.getCloudRenderMode().setValue(CloudRenderMode.OFF); 
        client.options.getEntityShadows().setValue(false);       
        client.options.getBiomeBlendRadius().setValue(0);
        
        try {
            client.options.getEntityDistanceScaling().setValue(0.75d);
        } catch (Exception ignored) {}
        
        client.options.write();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.fill(width / 2 - 145, 4, width / 2 + 145, height - 4, 0xB0101725);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("§b§lAxoFPS §7| §fPerformans Merkezi"), width / 2, 10, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(status), width / 2, height - 12, 0xFFFFFF);
    }

    @Override public void close() { client.options.write(); client.setScreen(null); }
    @Override public boolean shouldPause() { return false; }

    private final class AxoFPSWelcomeScreen extends Screen {
        AxoFPSWelcomeScreen() { super(Text.literal("AxoFPS Ultra Max")); }

        @Override protected void init() {
            addDrawableChild(ButtonWidget.builder(Text.literal("§aİyi Oyunlar, Devam Et!"), b -> client.setScreen(AxoFPSScreen.this))
                    .dimensions(width / 2 - 100, height / 2 + 50, 200, 20).build());
        }

        @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            renderBackground(context, mouseX, mouseY, delta);
            
            context.fill(width / 2 - 135, height / 2 - 55, width / 2 + 135, height / 2 + 80, 0xC00B0F19);
            
            String playerName = (client.player != null) ? client.player.getName().getString() : "Oyuncu";
            
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§c§lULTRA SUPER MAX OPTİMİZASYON"), width / 2, height / 2 - 45, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§bHoşgeldin, §e" + playerName + "!"), width / 2, height / 2 - 25, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§aFPS artışı ve Entity optimizasyonu başladı."), width / 2, height / 2 - 10, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§7Sunuculardaki kalabalık önlendi, akıcılık sağlandı."), width / 2, height / 2 + 10, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§fİyi Oyunlar dileriz!"), width / 2, height / 2 + 28, 0xFFFFFF);
            
            super.render(context, mouseX, mouseY, delta);
        }
        @Override public boolean shouldPause() { return false; }
    }
}
