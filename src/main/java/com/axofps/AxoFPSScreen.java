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
    private static boolean entityCullingActive = true;

    public AxoFPSScreen() {
        super(Text.literal("AxoFPS"));
    }

    @Override
    protected void init() {
        clearChildren();
        int bw = Math.min(250, width - 24);
        int x = width / 2 - bw / 2;
        int y = Math.max(20, height / 8);
        int gap = 20;

        // 1. ULTRA SUPER MAX OPTİMİZASYON
        String maxStateText = ultraMaxActive ? "§a§lULTRA SUPER MAX: AÇIK" : "§c§lULTRA SUPER MAX: KAPALI";
        addDrawableChild(ButtonWidget.builder(Text.literal(maxStateText), b -> {
            ultraMaxActive = !ultraMaxActive;
            if (ultraMaxActive) {
                applyUltraMax();
                status = "§aULTRA SUPER MAX aktif! Sınırlar kaldırıldı.";
                client.options.write();
                client.setScreen(new AxoFPSWelcomeScreen());
            } else {
                status = "§eULTRA SUPER MAX kapatıldı.";
                b.setMessage(Text.literal("§c§lULTRA SUPER MAX: KAPALI"));
                client.options.write();
            }
        }).dimensions(x, y, bw, 18).build());
        y += 22;

        // 2. ENTITY & MORE CULLING (Görünmeyen Mob/Oyuncuları ve Yükü Temizler)
        String cullingText = entityCullingActive ? "§aEntity & More Culling: AÇIK" : "§cEntity & More Culling: KAPALI";
        addDrawableChild(ButtonWidget.builder(Text.literal(cullingText), b -> {
            entityCullingActive = !entityCullingActive;
            applyCullingOptimization();
            b.setMessage(Text.literal(entityCullingActive ? "§aEntity & More Culling: AÇIK" : "§cEntity & More Culling: KAPALI"));
            client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        // 3. Render Distance
        addDrawableChild(ButtonWidget.builder(Text.literal("§eRender Distance: " + client.options.getViewDistance().getValue() + " chunk"), b -> {
            int next = client.options.getViewDistance().getValue() <= 6 ? 10 : client.options.getViewDistance().getValue() <= 10 ? 14 : 6;
            client.options.getViewDistance().setValue(next); b.setMessage(Text.literal("Render Distance: " + next + " chunk")); client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        // 4. Simülasyon Mesafesi
        addDrawableChild(ButtonWidget.builder(Text.literal("Simülasyon Mesafesi: " + client.options.getSimulationDistance().getValue()), b -> {
            int next = client.options.getSimulationDistance().getValue() <= 4 ? 8 : 4;
            client.options.getSimulationDistance().setValue(next); b.setMessage(Text.literal("Simülasyon Mesafesi: " + next)); client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        // 5. Parçacıklar
        addDrawableChild(ButtonWidget.builder(Text.literal("Parçacıklar: " + particleName()), b -> {
            ParticlesMode next = client.options.getParticles().getValue() == ParticlesMode.MINIMAL ? ParticlesMode.DECREASED : ParticlesMode.MINIMAL;
            client.options.getParticles().setValue(next); b.setMessage(Text.literal("Parçacıklar: " + particleName())); client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        // 6. FPS Limiti (Sınırsız / 160-170+ Kilidini Açar)
        addDrawableChild(ButtonWidget.builder(Text.literal("FPS Limiti: " + (client.options.getMaxFps().getValue() >= 260 ? "Sınırsız" : client.options.getMaxFps().getValue())), b -> {
            int current = client.options.getMaxFps().getValue();
            int next = current <= 60 ? 120 : current <= 120 ? 260 : 300;
            if (current >= 260) next = 60;
            client.options.getMaxFps().setValue(next); 
            b.setMessage(Text.literal("FPS Limiti: " + (next >= 300 ? "Sınırsız" : next))); 
            client.options.write();
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

    private void applyUltraMax() {
        client.options.getViewDistance().setValue(5);             
        client.options.getSimulationDistance().setValue(4);      
        client.options.getParticles().setValue(ParticlesMode.MINIMAL); 
        client.options.getCloudRenderMode().setValue(CloudRenderMode.OFF); 
        client.options.getEntityShadows().setValue(false);       
        client.options.getBiomeBlendRadius().setValue(0);
        client.options.getMaxFps().setValue(300); // FPS kilidini tamamen açar (Sınırsız yapar)
        
        try {
            client.options.getEntityDistanceScaling().setValue(0.75d);
        } catch (Exception ignored) {}
        
        client.options.write();
    }

    private void applyCullingOptimization() {
        if (entityCullingActive) {
            // Arkadaki ve görüş açısı dışındaki entity'lerin yükünü keser (Entity Culling etkisi)
            try {
                client.options.getEntityDistanceScaling().setValue(0.65d);
            } catch (Exception ignored) {}
        } else {
            try {
                client.options.getEntityDistanceScaling().setValue(1.0d);
            } catch (Exception ignored) {}
        }
        client.options.write();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Arkadaki oyun dünyasını bulanıklaştıran komut kaldırıldı, artık net görünür!
        context.fill(width / 2 - 145, 4, width / 2 + 145, height - 4, 0xD00A0E1A);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("§b§lAxoFPS §7| §fPerformans Merkezi"), width / 2, 10, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(status), width / 2, height - 12, 0xFFFFFF);
    }

    @Override public void close() { client.options.write(); client.setScreen(null); }
    @Override public boolean shouldPause() { return false; }

    // Bulanıklığı Kaldırılmış ve Net Karşılama Ekranı
    private final class AxoFPSWelcomeScreen extends Screen {
        AxoFPSWelcomeScreen() { super(Text.literal("AxoFPS Ultra Max")); }

        @Override protected void init() {
            addDrawableChild(ButtonWidget.builder(Text.literal("§aİyi Oyunlar, FPS Kilitleri Açıldı!"), b -> client.setScreen(AxoFPSScreen.this))
                    .dimensions(width / 2 - 110, height / 2 + 50, 220, 20).build());
        }

        @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            // Arka planın bulanıklaşması engellendi, tamamen net ve koyu şık bir kutu eklendi
            context.fill(width / 2 - 145, height / 2 - 60, width / 2 + 145, height / 2 + 80, 0xF0050811);
            
            String playerName = (client.player != null) ? client.player.getName().getString() : "Oyuncu";
            
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§c§lULTRA SUPER MAX OPTİMİZASYON"), width / 2, height / 2 - 50, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§bHoşgeldin, §e" + playerName + "!"), width / 2, height / 2 - 30, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§aFPS Sınırları Kaldırıldı (160-170+ Hedefleniyor)."), width / 2, height / 2 - 12, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§7Entity Culling ile kalabalık alanlar temizlendi."), width / 2, height / 2 + 8, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§fİyi Oyunlar dileriz!"), width / 2, height / 2 + 28, 0xFFFFFF);
            
            super.render(context, mouseX, mouseY, delta);
        }
        @Override public boolean shouldPause() { return false; }
    }
}
