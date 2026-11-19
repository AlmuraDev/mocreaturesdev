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

## Remaining (compile errors as of the last pass: about 2,000 unique)

After the entities batch: about 1,130 unique errors, none in `entity/**` or the network messages.

| Area | Errors | Main work |
|---|---|---|
| Entities (`entity/**`) | 0 | done, see below; needs in-game testing |
| Client rendering (`client/**`, 58 files) | ~480 | custom quads (`vertex(...)` chains, `MultiBufferSource`, `RenderSystem` blend state) to `SubmitNodeCollector`; the six `RenderLayer`s to `submit`; renderers with their own `render(...)` override; name and health bar rendering; particles (`fx/impl`); the werewolf, wraith and legacy big cat renderers |
| Registration (`init/**`) | ~195 | `EntityType.Builder.build(ResourceKey)`; spawn placements; creative tabs; villager trades; worldgen registration |
| Items (`item/**`) | ~160 | tools and armour to `ToolMaterial` / `Item.Properties.sword()/humanoidArmor()` with an equipment asset; `use` returning `InteractionResult`; NBT to data components; `RecordItem` to a jukebox song |
| World / worldgen / dimension | ~135 | 26.3 features are codec records (`Feature.place(level, generator, random, origin)`); `MobSpawnSettings.SpawnerData` changed; biome modifier codecs |
| Commands (`network/command/**`) | ~30 | permission checks; teleporting between dimensions; pet listing NBT |
| Other (events, util, compat, blocks) | ~160 | removed events (`TickEvent`, `BlockEvent.BreakEvent` moves, `MissingMappingsEvent`), `IPlantable`, `GlassBlock`, data fixers |

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

Resources are untouched so far:
- data pack folder renames (`loot_tables` to `loot_table`, `recipes` to `recipe`, `tags/items` to `tags/item`, and so on), plus the recipe and loot JSON formats
- `data/forge` to `data/c` tags
- the NeoForge biome modifier folder
- client item definitions (`assets/mocreatures/items/*.json`)
- equipment assets for the armour
- spawn egg textures (the old egg colours are still in `MoCSpawnEggs`)
- `render_type` in the block models for leaves, sapling, tall grass and glass, which `ItemBlockRenderTypes` used to set
- Wyvern Lair dimension and worldgen JSON for the 26.3 formats

## Almura changes to re-apply

- Turkey breeding items: soybean and corn, using the Almura 4.0 item ids. The 1.20.1 base already has its own turkey
  breeding; check in game that babies come out both male and female, and that only adult males use the male texture.
- Fish always drop an item. The 1.20.1 base drops only on a random roll.

## Server safety

The dev server runs the mod, so nothing the server runs may touch client-only classes (`Minecraft`, renderers,
screens). Before each build goes to the server, check the server-loaded classes for client references.
