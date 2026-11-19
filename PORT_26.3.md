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

| Area | Errors | Main work |
|---|---|---|
| Entities (`entity/**`, 122 files) | ~770 | overrides that changed signature; networking calls (`PacketDistributor.NEAR`/`TargetPoint`); `MobType` to entity type tags; riding offsets to passenger attachments; NBT lists and item components in inventories; loot tables as `ResourceKey<LootTable>`; enchantment effects |
| Client rendering (`client/**`, 58 files) | ~480 | custom quads (`vertex(...)` chains, `MultiBufferSource`, `RenderSystem` blend state) to `SubmitNodeCollector`; the six `RenderLayer`s to `submit`; renderers with their own `render(...)` override; name and health bar rendering; particles (`fx/impl`); the werewolf, wraith and legacy big cat renderers |
| Registration (`init/**`) | ~195 | `EntityType.Builder.build(ResourceKey)`; spawn placements; creative tabs; villager trades; worldgen registration |
| Items (`item/**`) | ~160 | tools and armour to `ToolMaterial` / `Item.Properties.sword()/humanoidArmor()` with an equipment asset; `use` returning `InteractionResult`; NBT to data components; `RecordItem` to a jukebox song |
| World / worldgen / dimension | ~135 | 26.3 features are codec records (`Feature.place(level, generator, random, origin)`); `MobSpawnSettings.SpawnerData` changed; biome modifier codecs |
| Network / commands | ~75 | `SimpleChannel` to `CustomPacketPayload` + `RegisterPayloadHandlersEvent`; permission checks |
| Other (events, util, compat, blocks) | ~160 | removed events (`TickEvent`, `BlockEvent.BreakEvent` moves, `MissingMappingsEvent`), `IPlantable`, `GlassBlock`, data fixers |

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
