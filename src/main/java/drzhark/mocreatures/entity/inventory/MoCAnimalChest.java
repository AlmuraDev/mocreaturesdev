/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.entity.inventory;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.LockCode;

/**
 * Container for animal inventories
 */
public class MoCAnimalChest extends SimpleContainer {

    private LockCode lockCode = LockCode.NO_LOCK;
    private Component name;
    private Size size;

    public MoCAnimalChest(String name, Size size) {
        super(size.numSlots);
        this.name = Component.literal(name);
        this.size = size;
    }

    public Component getName() {
        return this.name;
    }

    public void write(ValueOutput nbttagcompound) {
        this.lockCode.addToTag(nbttagcompound);
    }
    
    public void read(ValueInput nbttagcompound) {
        this.lockCode = LockCode.fromTag(nbttagcompound);
    }

    public Size getSize() {
        return size;
    }

    public static enum Size {

        tiny(9, 1),
        small(18, 2),
        medium(27, 3),
        large(36, 4),
        huge(45, 5),
        gigantic(54, 6);
        
        private final int numSlots;
        private final int rows;

        private Size(int numSlots, int rows){
            this.numSlots = numSlots;
            this.rows = rows;
        }

        public int getNumSlots() {
            return numSlots;
        }

        public int getRows() {
            return rows;
        }
    }
}
