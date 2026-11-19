/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.item;

import drzhark.mocreatures.MoCConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;

import java.util.function.Supplier;

/** The zebra's music disc. Its song is data/mocreatures/jukebox_song/shuffling.json. */
public class MoCItemRecord extends Item {

    public static final Identifier RECORD_SHUFFLE_RESOURCE = Identifier.fromNamespaceAndPath(MoCConstants.MOD_ID, "shuffling");
    public static final ResourceKey<JukeboxSong> SHUFFLING = ResourceKey.create(Registries.JUKEBOX_SONG, RECORD_SHUFFLE_RESOURCE);

    /** The comparator value and sound now come from the song file; the arguments are kept for the registration. */
    public MoCItemRecord(int comparatorValueIn, Supplier<SoundEvent> soundSupplier, Item.Properties properties) {
        super(properties.jukeboxPlayable(SHUFFLING));
    }
}
