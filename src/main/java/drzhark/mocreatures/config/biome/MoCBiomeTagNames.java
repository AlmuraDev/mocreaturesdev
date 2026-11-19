/*
 * GNU GENERAL PUBLIC LICENSE Version 3
 */
package drzhark.mocreatures.config.biome;

import java.util.Map;

/**
 * Maps the Forge biome tag ids the spawn config was written with (1.20.1 {@code forge:is_*}) to NeoForge 26.3's common
 * {@code c:} tags. Forge's tags no longer exist, so a {@code forge:} entry would match no biome. Existing configs keep
 * working: every tag id is passed through here when it is read.
 */
public final class MoCBiomeTagNames {

    /** Tags whose path changed as well as the namespace; the rest only move from forge: to c:. */
    private static final Map<String, String> RENAMED = Map.of(
            "is_coniferous", "is_tree/coniferous",
            "is_water", "is_aquatic",
            "is_steep", "is_mountain",
            "is_sparse", "is_sparse_vegetation",
            "is_sparse/overworld", "is_sparse_vegetation/overworld",
            "is_dense", "is_dense_vegetation",
            "is_dense/overworld", "is_dense_vegetation/overworld");

    private MoCBiomeTagNames() {
    }

    public static String modernize(String tagId) {
        if (tagId == null) {
            return null;
        }
        String id = tagId.trim();
        if (id.startsWith("#")) {
            return "#" + modernize(id.substring(1));
        }
        if (!id.startsWith("forge:")) {
            return id;
        }
        String path = id.substring("forge:".length());
        return "c:" + RENAMED.getOrDefault(path, path);
    }
}
