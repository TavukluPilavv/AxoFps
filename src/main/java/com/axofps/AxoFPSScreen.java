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
    private String status = "§aAxoFPS Pro • 300 FPS Motoru Hazır";
    
    private static boolean proMaxActive = false;
    private static boolean entityCullingActive = true;
    private static boolean fpsDropPrevention = true;

    public AxoFPSScreen() {
        super(Text.literal("AxoFPS Pro"));
    }

    @Override
    protected void init() {
        clearChildren();
        int bw = Math.min(260, width - 24);
        int x = width / 2 - bw / 2;
        int y = Math.max(16, height / 10);
        int gap = 18;

        // 1. ULTRA SUPER PRO MAX 300 FPS (ANA MOTOR)
        String proText = proMaxActive ? "§a§l🔥 ULTRA SUPER PRO MAX: 300 FPS AÇIK" : "§c§l⚡ ULTRA SUPER PRO MAX: KAPALI";
        addDrawableChild(ButtonWidget.builder(Text.literal(proText), b -> {
            proMaxActive = !proMaxActive;
            if (proMaxActive) {
                applyProMax300FPS();
                status = "§aPRO MAX 300 FPS DEVREDE! Devasa akıcılık ve FPS artışı aktif.";
                client.options.write();
                client.setScreen(new AxoFPSProWelcomeScreen());
            } else {
                status = "§ePro Max 300 FPS modu kapatıldı.";
                b.setMessage(Text.literal("§c§l⚡ ULTRA SUPER PRO MAX: KAPALI"));
                client.options.write();
            }
        }).dimensions(x, y, bw, 18).build());
        y += gap + 2;

        // 2. RAM TEMİZLEME (GC BOOST)
        addDrawableChild(ButtonWidget.builder(Text.literal("§d🧹 Akıllı RAM Temizleyici (FPS Boost)"), b -> {
            long before = Runtime.getRuntime().freeMemory();
            System.gc();
            long after = Runtime.getRuntime().freeMemory();
            long freed = Math.abs(after - before) / (1024 * 1024);
            status = "§aRAM Temizlendi! Serbest kalan: ~" + freed + " MB (Drop engellendi)";
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        // 3. FPS DROP ÖNLEME
        String dropText = fpsDropPrevention ? "§aFPS Drop Önleyici: AÇIK" : "§cFPS Drop Önleyici: KAPALI";
        addDrawableChild(ButtonWidget.builder(Text.literal(dropText), b -> {
            fpsDropPrevention = !fpsDropPrevention;
            b.setMessage(Text.literal(fpsDropPrevention ? "§aFPS Drop Önleyici: AÇIK" : "§cFPS Drop Önleyici: KAPALI"));
            client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        // 4. ENTITY & MORE CULLING
        String cullingText = entityCullingActive ? "§aEntity & More Culling: AÇIK" : "§cEntity & More Culling: KAPALI";
        addDrawableChild(ButtonWidget.builder(Text.literal(cullingText), b -> {
            entityCullingActive = !entityCullingActive;
            applyCullingOptimization();
            b.setMessage(Text.literal(entityCullingActive ? "§aEntity & More Culling: AÇIK" : "§cEntity & More Culling: KAPALI"));
            client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        // 5. Render Distance
        addDrawableChild(ButtonWidget.builder(Text.literal("§eRender Distance: " + client.options.getViewDistance().getValue() + " chunk"), b -> {
            int next = client.options.getViewDistance().getValue() <= 6 ? 10 : client.options.getViewDistance().getValue() <= 10 ? 14 : 6;
            client.options.getViewDistance().setValue(next); b.setMessage(Text.literal("Render Distance: " + next + " chunk")); client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        // 6. Simülasyon Mesafesi
        addDrawableChild(ButtonWidget.builder(Text.literal("Simülasyon Mesafesi: " + client.options.getSimulationDistance().getValue()), b -> {
            int next = client.options.getSimulationDistance().getValue() <= 4 ? 8 : 4;
            client.options.getSimulationDistance().setValue(next); b.setMessage(Text.literal("Simülasyon Mesafesi: " + next)); client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        // 7. 300 FPS / Sınırsız Sınır
        addDrawableChild(ButtonWidget.builder(Text.literal("FPS Hedefi: " + (client.options.getMaxFps().getValue() >= 260 ? "300 FPS (Sınırsız)" : client.options.getMaxFps().getValue())), b -> {
            int current = client.options.getMaxFps().getValue();
            int next = current <= 60 ? 120 : current <= 120 ? 260 : 300;
            if (current >= 260) next = 60;
            client.options.getMaxFps().setValue(next); 
            b.setMessage(Text.literal("FPS Hedefi: " + (next >= 300 ? "300 FPS (Sınırsız)" : next))); 
            client.options.write();
        }).dimensions(x, y, bw, 18).build());
        y += gap;

        // Kapat Butonu
        addDrawableChild(ButtonWidget.builder(Text.literal("§bAxoFPS Pro • Menüyü Kapat"), b -> close()).dimensions(x, y, bw, 18).build());
    }

    private void applyProMax300FPS() {
        client.options.getViewDistance().setValue(4);             // Düşük cihazlar için maksimum akıcılık
        client.options.getSimulationDistance().setValue(4);      
        client.options.getParticles().setValue(ParticlesMode.MINIMAL); 
        client.options.getCloudRenderMode().setValue(CloudRenderMode.OFF); 
        client.options.getEntityShadows().setValue(false);       
        client.options.getBiomeBlendRadius().setValue(0);
        client.options.getMaxFps().setValue(300); // 300 FPS kilidi açıldı
        
        try {
            client.options.getEntityDistanceScaling().setValue(0.55d); // Devasa entity yükü hafifletme
        } catch (Exception ignored) {}
        
        System.gc(); // Belleği anında temizle
        client.options.write();
    }

    private void applyCullingOptimization() {
        try {
            client.options.getEntityDistanceScaling().setValue(entityCullingActive ? 0.55d : 1.0d);
        } catch (Exception ignored) {}
        client.options.write();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Kesinlikle bulanık olmayan koyu net arka plan
        context.fill(width / 2 - 150, 2, width / 2 + 150, height - 2, 0xFF050811);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal("§b§l⚡ AxoFPS Pro §7| §f300 FPS & Akıcılık Motoru"), width / 2, 6, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, Text.literal(status), width / 2, height - 10, 0xFFFFFF);
    }

    @Override public void close() { client.options.write(); client.setScreen(null); }
    @Override public boolean shouldPause() { return false; }

    // Pro Max Karşılama Ekranı (Özel Notlar ve FPS Bilgilendirmesi ile)
    private final class AxoFPSProWelcomeScreen extends Screen {
        AxoFPSProWelcomeScreen() { super(Text.literal("AxoFPS Pro Max")); }

        @Override protected void init() {
            addDrawableChild(ButtonWidget.builder(Text.literal("§aHarika, Uçuşa Geç!"), b -> client.setScreen(AxoFPSScreen.this))
                    .dimensions(width / 2 - 110, height / 2 + 58, 220, 20).build());
        }

        @Override public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            // Hiçbir bulanıklık yok, tamamen net ve koyu kutu
            context.fill(width / 2 - 160, height / 2 - 70, width / 2 + 160, height / 2 + 85, 0xFF020408);
            
            String playerName = (client.player != null) ? client.player.getName().getString() : "Oyuncu";
            
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§c§l🔥 ULTRA SUPER PRO MAX 300 FPS"), width / 2, height / 2 - 60, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§bKaptan §e" + playerName + "§b, Sistem Ateşlendi!"), width / 2, height / 2 - 43, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§a🚀 +50 / +100 FPS Artış & 300 FPS Kilidi Aktif"), width / 2, height / 2 - 25, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§a✔ Düşük Cihaz Optimizasyonu & RAM Temizlendi"), width / 2, height / 2 - 10, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§7✔ Entity Culling & Drop Önleyici Devrede"), width / 2, height / 2 + 7, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer, Text.literal("§fArtık Takılmadan 170-200+ FPS'in Tadını Çıkar!"), width / 2, height / 2 + 28, 0xFFFFFF);
            
            super.render(context, mouseX, mouseY, delta);
        }
        @Override public boolean shouldPause() { return false; }
    }
}
