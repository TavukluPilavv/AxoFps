package com.axofps.mixin;

import com.axofps.AxoFPSScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Sodium'un ana video/grafik ayarları ekranını hedef alır
@Mixin(net.caffeinemc.mods.sodium.client.gui.SodiumOptionsGUI.class)
public class VideoOptionsScreenMixin extends Screen {

    protected VideoOptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void addAxoFPSButton(CallbackInfo ci) {
        int bw = 120;
        int bh = 18;

        // Sodium ekranının üst kısmına veya uygun bir yerine AxoFPS butonunu yerleştirir
        this.addDrawableChild(net.minecraft.client.gui.widget.ButtonWidget.builder(
            Text.literal("§b§lAxoFPS Ayarları"), 
            b -> {
                if (this.client != null) {
                    this.client.setScreen(new AxoFPSScreen());
                }
            }
        ).dimensions(this.width / 2 - 60, 4, bw, bh).build());
    }
}
