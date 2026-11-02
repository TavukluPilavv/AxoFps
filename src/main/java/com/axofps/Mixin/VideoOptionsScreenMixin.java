package com.axofps.mixin;

import com.axofps.AxoFPSScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoOptionsScreen.class)
public class VideoOptionsScreenMixin extends Screen {

    protected VideoOptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void addAxoFPSButton(CallbackInfo ci) {
        int bw = 150;
        int bh = 20;

        // Görüntü ayarları ekranına AxoFPS menüsünü açan buton ekler
        this.addDrawableChild(ButtonWidget.builder(Text.literal("§b§lAxoFPS Ayarları"), b -> {
            if (this.client != null) {
                this.client.setScreen(new AxoFPSScreen());
            }
        }).dimensions(this.width / 2 - 75, 4, bw, bh).build());
    }
}
