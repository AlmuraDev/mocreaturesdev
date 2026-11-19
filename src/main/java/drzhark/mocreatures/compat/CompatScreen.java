package drzhark.mocreatures.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class CompatScreen extends Screen {
    public static boolean showScreen = true;
    private final List<Component> messages = new ArrayList<>();
    private int textHeight;

    public CompatScreen() {
        super(Component.empty());
        this.messages.add(Component.translatable("msg.mocreatures.compat.cms"));
        this.messages.add(Component.empty());
        this.messages.add(Component.empty());
        this.messages.add(Component.translatable("msg.mocreatures.compat.cms1"));
        this.messages.add(Component.empty());
        this.messages.add(Component.translatable("msg.mocreatures.compat.cms2"));
        this.messages.add(Component.empty());
    }

    @Override
    protected void init() {
        this.clearWidgets();
        this.textHeight = this.messages.size() * this.font.lineHeight;

        int buttonY = Math.min(this.height / 2 + this.textHeight / 2 + this.font.lineHeight, this.height - 30);
        this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), btn -> {
            this.minecraft.gui.setScreen(new SelectWorldScreen(this));
        }).bounds(this.width / 2 - 100, buttonY, 200, 20).build());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
        int i = this.height / 2 - this.textHeight / 2;
        for (Component line : this.messages) {
            graphics.centeredText(this.font, line, this.width / 2, i, 0xFFFFFFFF);
            i += this.font.lineHeight;
        }
    }
}
