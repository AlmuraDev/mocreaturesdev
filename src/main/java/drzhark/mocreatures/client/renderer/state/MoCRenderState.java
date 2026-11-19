/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.client.renderer.state;
import net.minecraft.client.renderer.item.ItemStackRenderState;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;

/**
 * Render state for Mo' Creatures mobs.
 * <p>
 * The models were written against the 1.20 renderer, which read everything straight from the entity. Rather than copying
 * each creature's dozens of animation fields into the state, the state carries the entity itself; the models and
 * renderers read it on the render thread while the frame is drawn, the same way they did before.
 */
public class MoCRenderState extends LivingEntityRenderState {
    public @Nullable Entity entity;
    /** Mirrors the 1.20 EntityModel fields the models still read. */
    public boolean isPassenger;
    public float attackTime;
    /** An item the creature holds, for renderers that draw one (the filch lizard's loot). */
    public final ItemStackRenderState heldItem = new ItemStackRenderState();
}
