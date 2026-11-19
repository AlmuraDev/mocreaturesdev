package drzhark.mocreatures.client.gui;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import drzhark.mocreatures.client.MoCClientPackets;
import com.mojang.blaze3d.vertex.PoseStack;
import drzhark.mocreatures.MoCConstants;
import drzhark.mocreatures.MoCreatures;
import drzhark.mocreatures.entity.IMoCEntity;
import drzhark.mocreatures.entity.tameable.IMoCTameable;
import drzhark.mocreatures.network.MoCMessageHandler;
import drzhark.mocreatures.network.message.MoCMessageUpdatePetName;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Mob;

public class MoCGUIEntityNamer extends Screen {

    @SuppressWarnings("removal")
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "textures/gui/pet_naming.png");

    private final IMoCEntity namedEntity;
    private EditBox nameInput;
    private final int imageWidth = 256;
    private final int imageHeight = 181;

    public MoCGUIEntityNamer(IMoCEntity entity, String defaultName) {
        super(Component.literal("Choose your Pet's name:"));
        this.namedEntity = entity;
        this.nameInput = new EditBox(this.font, 0, 0, 200, 20, Component.literal("Pet Name"));
        this.nameInput.setValue(defaultName);
    }

    @Override
    protected void init() {
        super.init();
        int centerX = (this.width - imageWidth) / 2;
        int centerY = (this.height - imageHeight) / 2;

        this.nameInput = new EditBox(this.font, this.width / 2 - 75, centerY + 70, 150, 20, Component.literal("Pet Name"));
        this.nameInput.setMaxLength(30);
        this.nameInput.setFocused(true);
        this.addRenderableWidget(nameInput);

        //this.addRenderableWidget(new Button(this.width / 2 - 75, centerY + 100, 150, 20, Component.literal("Done"), btn -> updateName(), DEFAULT_NARRATION));
        this.addRenderableWidget(
                Button.builder(Component.literal("Done"), btn -> updateName())
                        .bounds(this.width / 2 - 75, centerY + 100, 150, 20)
                        .build()
        );

    }

    private void updateName() {
        String petName = this.nameInput.getValue().trim();
        if (!petName.isEmpty()) {
            this.namedEntity.setPetName(petName);
            if (this.namedEntity instanceof Mob) {
                MoCClientPackets.sendToServer(new MoCMessageUpdatePetName(((Mob) this.namedEntity).getId(), petName));
            }
        }
        this.onClose();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        int centerX = (this.width - imageWidth) / 2;
        int centerY = (this.height - imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, centerX, centerY, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
        int centerY = (this.height - imageHeight) / 2;
        graphics.centeredText(this.font, this.title, this.width / 2, centerY + 30, 0xFFFFFFFF);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == 257 || event.key() == 335) { // Enter or Numpad Enter
            updateName();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public void onClose() {
        super.onClose();
        if (this.namedEntity instanceof IMoCTameable tamed) {
            tamed.playTameEffect(true);
        }
    }
}
