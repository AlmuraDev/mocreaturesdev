# Mo' Creatures 26.3 port

Branch `26.3`, based on multision's Mo' Creatures: Nostalgia Edition for 1.20.1 (Forge 47.4.1, `multision/main` @ 00738ca).
Target: Minecraft 26.3, NeoForge 26.3.0.83, ModDevGradle 2.0.148, Gradle 9.7.1, JDK 25 — the same setup as the
furniture mod's `4.0` branch.

## Done

- **Build:** ModDevGradle build (`build.gradle`, `settings.gradle`, `gradle.properties`, wrapper),
  `META-INF/neoforge.mods.toml`. The empty 1.16 access transformer and the 1.12 `mcmod.info` are gone. The 1.20.1
  regression checks and game tests in `src/test` are switched off until the main code compiles.
- **Forge to NeoForge:** all `net.minecraftforge` imports moved (`MinecraftForge.EVENT_BUS` to `NeoForge.EVENT_BUS`,
  `@Mod.EventBusSubscriber` to `@EventBusSubscriber` without `bus`, `RegistryObject` to `DeferredHolder`,
  `ForgeRegistries` to `BuiltInRegistries`/`Registries`). `ResourceLocation` is `Identifier`. Classes Mojang moved to
  other packages were re-imported from the 26.3 sources.
- **Registration:** `DeferredHolder<R, T>` everywhere. Items and blocks get their registry id through
  `MoCItems.props(id)` and `MoCBlocks.props(id)`, which 26.3 needs before an item or block is constructed. Spawn eggs
  are plain `SpawnEggItem`s with `.spawnEgg(type)`. They are no longer tinted, so they need textures (see below).
- **Rendering bridge.** 26.3 renders from a render state, and the renderer can't see the entity while drawing. The
  models still read everything from the entity and draw part by part, so three classes bridge the gap:
  - `client/renderer/state/MoCRenderState` carries the entity (read on the render thread during the frame).
  - `client/model/MoCEntityModel<T>` is the base for all models. It forwards `setupAnim(state)` to the 1.20
    `setupAnim(entity, limbSwing, ...)`/`prepareMobModel`, keeps `young`, `riding` and `attackTime`, and keeps the
    model's own `renderToBuffer(..., r, g, b, a)` draw routine. `ModelPart.render` calls now take a packed ARGB color.
  - `client/renderer/entity/MoCLivingRenderer<T, M>` is the base for the mob renderers. It keeps the 1.20 hooks
    (`scale`, `setupRotations`, `getTextureLocation` taking the entity) and draws the body from its first layer as
    custom geometry that calls the model's draw routine. Body alpha and color, ghost translucency and
    `IPartialTransparencyModel` go through `getBodyAlpha`/`getBodyColor`.
    Known gap: glowing outlines aren't drawn for Mo' Creatures mobs, because custom geometry has no outline pass.
- **Entity API (mechanical):** `defineSynchedData(builder)`; save and load through `ValueOutput`/`ValueInput` with
  `get*Or` defaults; `MobSpawnType` is `EntitySpawnReason`; `finalizeSpawn` without the NBT argument; `hurtServer`,
  `hurtOrSimulate`, `doHurtTarget(ServerLevel, ...)`, `dropCustomDeathLoot(ServerLevel, ...)`,
  `getBaseExperienceReward`; `snapTo`, `igniteForSeconds`, `isBrightOutside`, step height as an attribute; renamed
  `MobEffects`.

## Remaining

**The whole mod compiles** and `./gradlew build` produces `build/libs/mocreatures-neoforge-26.3-1.0.0.jar`. Nothing
has been run in a game yet. The resources are converted to the 26.3 layout (see the resources batch below) but not yet loaded in a game.

| Area | Errors | Main work |
|---|---|---|
| Entities (`entity/**`) | 0 | done, see below; needs in-game testing |
| Client rendering (`client/**`) | 0 | done, see below; needs in-game testing |
| Registration (`init/**`) | 0 | done, see below |
| Items (`item/**`) | 0 | done, see below; needs in-game testing |
| World / worldgen / dimension | 0 | done, code and data, see below; needs in-game testing |
| Commands (`network/command/**`) | 0 | done, see below |
| Blocks (`block/**`) | 0 | done, see below |
| Other (`MoCTools`, events, compat) | 0 | done, see below |
| Resources (`src/main/resources`) | – | converted, see below; needs a game load to validate |

### Entities batch: how the 1.20 behaviour was kept

- **Packets:** the messages are NeoForge payloads (`TYPE` + `STREAM_CODEC`), registered in `MoCMessageHandler`
  (client-bound and server-bound separately). Servers send through `MoCMessageHandler.sendToNear/sendToTracking/
  sendToPlayer/sendToAll`; the client sends through `client/MoCClientPackets`, which only client code calls.
- **Save data:** entities save through `ValueOutput`/`ValueInput`. Nested compounds and lists go through
  `MoCTools.writeTag/readCompound/readList/has`, and inventory stacks through `MoCTools.saveStack/loadStack` (item codec,
  same "Slot" layout). The pet owner is synced as an entity reference.
- **Pet map:** `MoCPetMapData.TYPE` is a 26.3 `SavedDataType`, id `mocreatures:pets`, so it's a new file in a new world.
- **Loot:** 26.3 ties a mob's loot table to its entity type, so the base classes override `dropFromLootTable` and
  drop from the creature's own `getDefaultLootTable()` (per variant; null drops nothing). `dropCustomDeathLoot` gets
  the looting level from `MoCTools.lootingLevel`.
- **Undead and arthropods:** `MobType` is gone. Always-undead and arthropod creatures are in the `minecraft:undead` and
  `minecraft:arthropod` entity type tags (`data/minecraft/tags/entity_type`). The variants that are only sometimes
  undead (some horses and horse mobs, the undead ostrich, the ghost wyvern) override `isInvertedHealAndHarm`, so
  healing and harming work as before. Smite only sees the tag.
- **Riding:** creatures keep `getPassengersRidingOffset()` / `getMyRidingOffset()`; the base classes turn them into
  26.3 passenger and vehicle attachment points. Seat heights may differ slightly for players; check in game.
- **Leads:** `canBeLeashed()` no longer gets the player, so tamed pets refuse a lead from anyone but the owner or an op
  in `interact` instead.
- **On-hit effects:** 26.3 has no `doEnchantDamageEffects`, so it is a Mo' Creatures method on the base classes, called
  from their `doHurtTarget`; creatures still override it for poison, fire and the like.
- **Eye height and size:** `getStandingEyeHeight` became `getDefaultDimensions(...).withEyeHeight(...)`; the werewolf and
  filch lizard size changes moved there too (`getDimensions` is final).
- **Eggs:** the egg's creature type is the `mocreatures:egg_type` item component (`MoCDataComponents`), not stack NBT.
- **Approximations to check in game:** werewolf damage from swords and tools (no tool tiers in 26.3; it uses the
  weapon's attack damage), `ultraWarm` dimensions (now "water evaporates"), glowing outline (not drawn).
- **Server safety:** entity code no longer uses `I18n` or `Minecraft`; verbose names use `Language`, and client-only
  lookups go through the proxy.

### Client rendering batch

- **Drawing happens later in the frame.** The 26.3 renderer only records what to draw; models are shared by every
  entity of a type. Anything a renderer used to set on a model while rendering (crocodile bite, goat bleat, kitty
  pose, kitty bed contents, big cat sitting, werewolf hunch) moved into `prepareModel(entity, model)`, which
  `MoCLivingRenderer` calls right before each draw.
- **Pet name and health bar:** one helper, `MoCPetLabel`, submits the text and bar quads. `MoCRenderMoC` uses it for
  most creatures; the dolphin, shark, goat and legacy big cat keep their own offsets and distance limits.
- **Translucency:** ghosts, vanishing horses and wraiths go through `getBodyAlpha`/`getBodyColor`. GL blend calls
  (`RenderSystem.enableBlend` etc.) can't be used any more. The insects with see-through wings already split their
  parts (`IPartialTransparencyModel`), so the bridge draws those parts translucent. The horse, wyvern, big cat,
  firefly and maggot models use a translucent render type for the whole model. Their old `clearColor` calls never
  tinted anything, so only texture alpha matters.
- **Layers** are `RenderLayer<MoCRenderState, M>` with `submit(...)`. Glow and effect layers (firefly, golem) redraw the
  model through `submitModelPass`; the filch lizard's stolen item goes through an item render state; the werewolf's
  human form, the kitty bed sheet and the legacy big cat mane draw their second model as custom geometry.
- **Humanoid models** (wraith, werehuman) extend `MoCHumanoidModel`, which keeps vanilla's humanoid mesh and the 1.20
  walk, ride, swing and crouch animation.
- **Thrown rocks** are drawn like falling blocks (`submitMovingBlock`), spinning as before.
- **Particles** use codecs for their options and `SingleQuadParticle`; providers register through
  `RegisterParticleProvidersEvent.registerSpriteSet`.
- **Pet naming screen, dive key and ride controls** use the 26.3 screen, key mapping (own "Mo' Creatures" category) and
  client tick APIs.
- **Server safety:** the proxy is picked from the mod constructor's `Dist`, and the client proxy is loaded by name, so a
  dedicated server never loads it. The horse guide opens its book through the proxy.
- **Check in game:** pet name and health bar placement, see-through wings and ghosts, the firefly glow (now emissive
  translucent), the filch lizard's held item and the kitty mood icons.

### Registration batch

- **Entity types** are built with a `ResourceKey`. **Spawn placements** moved from the attribute event to
  `RegisterSpawnPlacementsEvent` (all 88, same placement types, heightmaps and rules, `REPLACE`).
- **Tool and armour materials** are 26.3 records (`MoCItemTier`, `MoCArmorMaterial`). Repair items come from item tags
  (`data/mocreatures/tags/item/*_tool_materials.json`, `*_armor_materials.json`). Silver and sting tools had no repair
  item and still don't (empty tags). Swords, axes and armour use `Item.Properties.sword/axe/humanoidArmor`; axes no
  longer strip logs, since 26.3 has no `AxeItem` class to inherit that from. Each armour material names an equipment
  asset `mocreatures:<croc|fur|hide|scorpd|scorpf|scorpn|scorpc|scorpu|silver>`, which still needs its
  `assets/mocreatures/equipment/*.json` (resources).
- **Fuel** is the `cookingFuel` item property, with burn times in
  `data/mocreatures/context_int_provider/cooking/time_<item>.json`: same values as before (wyvwood sapling 100,
  firestone chunk 2400, heart of fire 3200, shark sword and axe 200).
- **Villager trades** are data: `data/mocreatures/villager_trade/<profession>/<level>/*.json`, added to the vanilla
  `minecraft:<profession>/level_<n>` villager trade tags. 26.3 trades take a fixed count, so the old random counts are
  now their middle values (duck raw 16, ostrich raw 11, cooked duck 7, cooked ostrich 6, silver scrap 5, mystic pear 2,
  fish net price 3, cooked crab 7, fur 10, hide 7, silver ingot price 5, ingots for an emerald 3).
- **Worldgen features** (firestone cluster, wyvern nest, wyvern island) are codec records registered as feature types
  (`Registries.FEATURE_TYPE`); their worldgen JSON still needs moving to the 26.3 layout (resources).
- **Removed:** `MoCTagProvider` and `MoCDataGeneration` (datagen had never been run; the tags are hand-written JSON),
  `MoCRecipes` (fuel) and `MoCVillagerTrades` (trades), both now data. `MoCPOI` registers through the mod's deferred
  registries only.

### Items batch

- **Signatures:** `use` returns `InteractionResult`; `hurtEnemy` returns nothing; tooltips take a consumer; durability
  loss names the hand or slot; use animations are `ItemUseAnimation`.
- **Amulets and fish nets** keep the stored creature in the `minecraft:custom_data` component
  (`MoCTools.stackTag/setStackTag`), with the same keys as before; owner UUIDs are stored with the UUID codec (the same
  int-array form 1.20 used).
- **Food:** nutrition and saturation in the food component; eating time and potion effects in the consumable component.
  Wolves eat the Mo' Creatures meats through the `minecraft:meat` item tag.
- **Music disc:** a jukebox song, `data/mocreatures/jukebox_song/shuffling.json` (sound `mocreatures:shuffling`,
  comparator 15, 120 seconds); check the length against the sound file in game.
- **Scorpion stings** (`MoCItemWeapon`): attack damage, enchantability and repair are components.
- **Armour** set bonuses run from `inventoryTick` when the boots are worn.
- **Eggs** name themselves per creature through `getName`.
- **Horse guide:** the book is a written book component; the creative tab copy has a custom name and lore.
- **Portal staff:** teleports through 26.3 teleport transitions; `MoCDirectTeleporter` builds the portal and moves the
  player itself. The staff no longer blocks enchanting at the table (that is tag-driven now); it still refuses to work
  with mending or unbreaking.
- **Removed:** `MoCItemCrabClaw`, which was never registered.

### Worldgen batch

- **Spawns:** the 88 biome spawn entries add through `addSpawn(type, category, weight, UniformInt)`; the village kitty
  spawn override takes the weight beside the entry.
- **Wyvern Lair data** is in the 26.3 formats:
  - features moved from `worldgen/configured_feature` to `worldgen/feature`, with their config inlined and block
    providers in the short `{"id"}` form;
  - the two `random_patch` features (tall wyvgrass, mushrooms) are now plain features; their placed features carry the
    old tries and spread as count and offset placements;
  - trees replace `dirt_provider`/`force_dirt` with a `below_trunk_provider` that places wyvdirt (always for the large
    trees, which forced it; otherwise only where vanilla would replace the block below a trunk);
  - biome colours, music and mood sound moved into `effects` and environment `attributes`; the empty spawn lists went
    (Mo' Creatures spawns come from the biome modifier);
  - biome modifiers moved to `data/mocreatures/neoforge/biome_modifier` with `neoforge:` types. The nest modifier pointed at
    a tag that doesn't exist (`#mocreatures:worldgen/biome/is_wyvern_lair`); it now uses `#mocreatures:is_wyvern_lair`;
  - island structure templates moved from `structures/` to `structure/`, where 26.3 looks for them.
- **Terrain:** the old noise settings used 1.20 density-function syntax that 26.3 no longer reads. The Wyvern Lair now
  uses vanilla 26.3's floating islands terrain, with wyvstone, no fluid and the Wyvern Lair's own surface rule
  (now `material_rule`). The dimension is 0 to 256 high to match (it was -64 to 320). Check the island shapes and heights
  in game.
- **Dimension type:** bed, raid, respawn anchor and piglin rules are attributes; day and night come from the overworld
  clock and timelines. The unregistered custom sky (`effects`) was dropped, so the sky is vanilla's.
- **Portal and teleporter:** see the items batch.
- None of this data has been loaded by a game yet: the first run will show any datapack errors in the log.

### Blocks batch

- Gleaming glass is a `TransparentBlock`; its model marks the texture `force_translucent`. 26.3 picks cutout for leaves,
  sapling and tall grass from their textures' transparency, so `ItemBlockRenderTypes` isn't needed.
- The wyvwood sapling uses a 26.3 `TreeGrower` (`WyvwoodTreeGrower.WYVWOOD`): dark oak and spruce shapes at weight 9 each,
  the two large shapes at weight 1 each, the same one-in-ten chance of a large tree as before.
- Silver sand is a `ColoredFallingBlock` with its old dust colour; silver sand and wyvgrass say which plants they grow
  through NeoForge's `canSustainPlant(..., BlockState plant)` (cacti and dead bushes; mushrooms).
- Wyvgrass bone meal uses the biome's bone meal features; the ores drop experience unless mined with silk touch through
  NeoForge's `getExpDrop`; leaves have no ambient sound; blocks that used `PushReaction.DESTROY` now pop (`POPPED`).

### Last compile errors (MoCTools, commands, events, compat)

- **Commands** check permissions through `source.permissions()` (level 2 is `Permissions.COMMANDS_GAMEMASTER`).
  `/moctp` teleports pets across dimensions with a teleport transition and now matches the owner by UUID; 1.20 compared
  a saved owner name that Mo' Creatures never wrote, so it never found a pet.
- **Spawn events** use `FinalizeSpawnEvent`; its old `ALLOW`/`DEFAULT` results only meant "carry on" and are gone.
  Note: `MoCEventHooks.onLivingSpawnEvent` looks entities up in `entityMap` by class while the map is keyed by entity type,
  so it never matches (as in 1.20.1); left as is.
- **MoCTools:** mob griefing reads the 26.3 game rule from the server level; explosions use `ServerExplosion.getSeenPercent`;
  break checks use `BreakBlockEvent`; op checks use `NameAndId`; entity copies go through `TagValueOutput`/`TagValueInput`.
- **Removed:** `compat/datafixes` (1.16 id remappers that nothing registered). Their mappings matter for the 3.5 world
  converter: an old `mocreatures:scorpion` becomes cave (TypeInt 2), fire (3), frost (4) or dirt (other) scorpion;
  an old `mocreatures:manticore` becomes dark (2), frost (3), toxic (4) or fire (other) manticore; both get TypeInt 1.
- **Client packet handling** lives in `client/MoCClientPacketHandlers`. The client-bound message classes, which the server
  loads to send them, only call it from the lambda that runs on the client. A constant-pool scan of the jar finds no
  other client class referenced from code a dedicated server loads.

### Resources batch

- **Data pack layout:** `loot_tables` to `loot_table`, `recipes` to `recipe`, `tags/items` and `tags/blocks` to
  `tags/item` and `tags/block`. The `data/forge` tags moved to `data/c` (ores) or to `data/mocreatures` (`raw_fishes`,
  `cooked_fishes`, `chitinscorpion`, which `MoCTags` now reads from the `mocreatures` namespace). Unused
  `minecraft` biome and item tags, the old structure and processor folders and `worldgen/biome_effects` are gone.
- **Recipes:** `forge:` ingredient tags point at the `c:` or vanilla tags.
- **Loot tables:** 26.3 condition and function names. `set_nbt {EggType:N}` became `set_components` with
  `mocreatures:egg_type`; looting chances use `random_chance_with_enchanted_bonus` and `enchanted_count_increase`;
  shears and silk touch checks use the `minecraft:tool/can_shear` and `can_silk_touch` item tags.
- **Item definitions:** `assets/mocreatures/items/<id>.json` for all 266 registered items and block items. Eight
  1.20.1 item models with no registered item (`builderhammer`, `staffteleport`, `wyvern_*`) are deleted.
- **Spawn eggs:** 26.3 has no egg template, so all 88 eggs share `models/item/spawn_egg.json` with two new
  greyscale textures (`textures/item/spawn_egg.png` and `spawn_egg_overlay.png`), tinted in each item definition with
  the old egg colours from `MoCSpawnEggs`.
- **Armour:** `assets/mocreatures/equipment/<material>.json` for the nine materials. The `layer_1`/`layer_2`
  textures moved to `textures/entity/equipment/humanoid/` and `humanoid_leggings/`.
- **Lang:** the 1.12 `.lang` files (German, French, Russian) are now `de_de.json`, `fr_fr.json` and `ru_ru.json`, with
  the old keys mapped to the current ones; every old key found a match. `en_us.json` gained the horse guide name.
- **Other:** the `forge_marker` in `blockstates/tall_wyvgrass.json` and the 1.16 `pack.mcmeta` are gone (NeoForge
  provides the pack metadata for mods). Villager trades, fuel burn times and the zebra disc were already in 26.3 form
  from the registration batch. All JSON under `src/main/resources` parses and every model parent, texture and
  blockstate model it names exists.
- **Not checked yet:** nothing has loaded these files in a game. The data loader may still reject individual
  loot tables, recipes or worldgen files; the game log will name them.

## Almura changes to re-apply

- Turkey breeding items: soybean and corn, using the Almura 4.0 item ids. The 1.20.1 base already has its own turkey
  breeding; check in game that babies come out both male and female, and that only adult males use the male texture.
- Fish always drop an item. The 1.20.1 base drops only on a random roll.

## Server safety

The dev server runs the mod, so nothing the server runs may touch client-only classes (`Minecraft`, renderers,
screens). Before each build goes to the server, check the server-loaded classes for client references.
