# AGENTS.md

## Module Scope

- `survive` provides survival systems (hydration, diet/nutrition, body heat, effects, related UI/network glue).
- Depends on `:base` and `:thermal`; integrates with `:hud` via `compileOnly` compatibility hooks.

## Build and Dependency Facts

- Standalone Gradle module and git submodule; also included by root multi-project build.
- Uses Java toolchain 21 and Kotlin JVM target 21.
- Uses `net.neoforged.moddev` and Kotlin for Forge.
- Declares:
    - `implementation(project(":base"))`
    - `implementation(project(":thermal"))`
    - `compileOnly(project(":hud"))`

## Main Entrypoint and Core Areas

- Entrypoint: `src/main/kotlin/dev/deepslate/fallacy/survive/TheMod.kt`.
- Attachments / attributes / capabilities:
    - `src/main/kotlin/dev/deepslate/fallacy/survive/ModAttachments.kt`
    - `src/main/kotlin/dev/deepslate/fallacy/survive/ModAttributes.kt`
    - `src/main/kotlin/dev/deepslate/fallacy/survive/ModCapabilities.kt`
- Systems:
    - Hydration: `src/main/kotlin/dev/deepslate/fallacy/survive/hydration/*`
    - Diet: `src/main/kotlin/dev/deepslate/fallacy/survive/diet/*`
    - Thermal body logic: `src/main/kotlin/dev/deepslate/fallacy/survive/thermal/*`
- Commands and networking:
    - `src/main/kotlin/dev/deepslate/fallacy/survive/command/RegisterHandler.kt`
    - `src/main/kotlin/dev/deepslate/fallacy/survive/network/handler/RegisterHandler.kt`
- HUD compatibility hook:
    - `src/main/kotlin/dev/deepslate/fallacy/survive/compat/hud/Hook.kt`

## Java Injection and Mixin Points

- Interface extensions:
    - `src/main/java/dev/deepslate/fallacy/survive/inject/itemfoodproperties/ItemFoodPropertiesExtension.java`
    - `src/main/java/dev/deepslate/fallacy/survive/inject/recordmovement/MovementRecord.java`
- Mixins:
    - `src/main/java/dev/deepslate/fallacy/survive/mixin/itemfoodproperties/ItemMixin.java`
    - `src/main/java/dev/deepslate/fallacy/survive/mixin/fooddata/PlayerMixin.java`
    - `src/main/java/dev/deepslate/fallacy/survive/mixin/recordmovement/ServerPlayerMixin.java`
- Injection/AT configs:
    - `src/main/resources/META-INF/interfaceinjection.cfg`
    - `src/main/resources/META-INF/accesstransformer.cfg`

## Registration and Event Flow

- Registration is largely event-subscriber based (`@EventBusSubscriber`).
- Deferred register objects (attachments, attributes, effects, nutrition types, data components) are registered on
  construct events via `MOD_BUS`.
- Container menus/screens should prefer Tau menu flow (`TauMenuHolder` + `TauMenuHelper.registerMenuScreen`) over
  ad-hoc `AbstractContainerMenu`/`AbstractContainerScreen` wiring.
- **Do not use `bus = EventBusSubscriber.Bus.MOD`**. The `bus` parameter on `@EventBusSubscriber` is deprecated in newer
  NeoForge/KFF versions. For MOD bus events (e.g., `FMLConstructModEvent`, `RegisterMenuScreensEvent`), register
  listeners manually in `TheMod.init` via `MOD_BUS.addListener(...)` or `MOD_BUS.register(...)`.
- Gameplay runtime logic is mostly tick/event driven:
    - hydration tick and drink interaction handlers,
    - diet cause/eat handlers,
    - body heat tick sync and updates,
    - packet sync handlers for client state.

## Cross-Module Integration Rules

- Keep direct `hud` references isolated in `compat/hud/*` (because `hud` is `compileOnly`).
- When touching thermal-related survival behavior, verify assumptions against `thermal` engine contracts before changing
  thresholds or tick cadence.
- Shared helper utilities should live in `base` when generally reusable.

## Resources and Datagen

- Mod metadata template: `src/main/templates/META-INF/neoforge.mods.toml`.
- Mixin config: `src/main/resources/fallacy_survive.mixins.json`.
- Datagen outputs under `src/generated/resources` are active resources (blockstates/models/lang/loot tables).

## Verification

- Fast check: `./gradlew :survive:compileKotlin`
- Full module build: `./gradlew :survive:build`
