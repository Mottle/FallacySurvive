# Fallacy Survive

> A NeoForge survival mechanics module for the Fallacy mod ecosystem.

Fallacy Survive introduces immersive survival systems — hydration, nutrition, body temperature, and environmental adaptation — designed to integrate seamlessly with the Fallacy base and thermal engines.

---

## Features

### Hydration
- Water consumption and thirst mechanics
- Custom boiling pot block (`BoilPot`) with dynamic fluid rendering
  - Supports arbitrary fluid types (water, lava, mod fluids)
  - Dynamic liquid level based on contained amount
  - Per-fluid tint colors and textures via `BlockEntityRenderer`

### Diet & Nutrition
- Nutrient tracking system with multiple nutrient types
- Food history and consumption pattern effects
- Extended food properties support
- Custom diet UI accessible from the inventory screen

### Thermal / Body Heat
- Player body temperature simulation
- Integration with the Fallacy Thermal engine for environmental heat sources
- Heat-sensitive gameplay effects

### Effects & Status
- Custom status effects tied to survival states
- Attribute modifiers for dehydration, malnutrition, and temperature extremes

---

## Tech Stack

| Component        | Version           |
|------------------|-------------------|
| Minecraft        | 1.21.1            |
| NeoForge         | 21.1.186          |
| Kotlin           | JVM 21            |
| Kotlin for Forge | Latest stable     |
| Registrate       | (via base module) |

---

## Module Dependencies

```
survive
├── implementation(:base)       — shared foundation (Registrate, utilities)
├── implementation(:thermal)     — thermodynamics engine
└── compileOnly(:hud)            — HUD compatibility hooks
```

---

## Project Structure

```
src/main/kotlin/dev/deepslate/fallacy/survive/
├── TheMod.kt                    — Mod entrypoint & Registrate
├── ModAttachments.kt            — Data attachments
├── ModAttributes.kt             — Custom attributes
├── ModCapabilities.kt           — Capability registration
├── ModDataComponents.kt         — Data components
├── ModEffects.kt                — Status effects
├── ModCreativeTabs.kt           — Creative tab registration
│
├── block/                       — Blocks & BlockEntities
│   ├── BoilPotBlock.kt
│   ├── ModBlocks.kt
│   ├── ModBlockEntities.kt
│   └── entity/BoilPotEntity.kt
│
├── hydration/                   — Hydration system
│   ├── block/BoilPotTank.kt     — Fluid capability handler
│   ├── entity/Handler.kt        — Hydration tick logic
│   └── logic/                   — Drink interactions
│
├── diet/                        — Nutrition system
│   ├── ModNutritionTypes.kt
│   ├── NutrientType.kt
│   ├── NutrientContainer.kt
│   ├── entity/FoodHistory.kt
│   ├── item/ExtendedFoodProperties.kt
│   └── logic/                   — Eat handlers & cause logic
│
├── thermal/                     — Body heat system
│   ├── HeatSensitive.kt
│   ├── entity/
│   └── logic/PlayerBodyHeatLogic.kt
│
├── client/                      — Client-side code
│   ├── renderer/                — BlockEntity renderers
│   ├── screen/                  — GUIs (DietUI, BoilPotUI)
│   └── tooltip/
│
├── network/                     — Packet syncing
│   ├── packet/
│   └── handler/
│
├── command/                     — Debug commands
├── compat/hud/                  — HUD module integration
└── foodrework/                  — Vanilla food behavior tweaks
```

[//]: # (---)

[//]: # ()
[//]: # (## Key Blocks)

[//]: # ()
[//]: # (### Copper Boil Pot &#40;`copper_boil_pot`&#41;)

[//]: # ()
[//]: # (A container block for heating and boiling liquids.)

[//]: # ()
[//]: # (- **Capacity**: 1000 mB)

[//]: # (- **Fluid interaction**: Right-click with any fluid container &#40;bucket, tank, etc.&#41;)

[//]: # (- **Rendering**: Dynamic fluid surface rendered via `BoilPotBlockEntityRenderer`)

[//]: # (  - Height scales continuously with fluid amount)

[//]: # (  - Tint and texture adapt to the specific fluid type)

[//]: # (- **Heating**: Integrated with the Fallacy Thermal engine — contents heat up based on ambient temperature)

---

## Build & Run

From the **root** Fallacy multi-project directory:

```bash
# Fast compile check
./gradlew :survive:compileKotlin

# Full module build
./gradlew :survive:build

# Run client (for testing)
./gradlew :survive:runClient

# Run datagen
./gradlew :survive:runData
```

> On Windows/WSL environments, prefer: `powershell.exe ./gradlew :survive:build`

---

## Datagen

Generated resources live in `src/generated/resources/` and are included in the main source set. Block states, models, loot tables, and language entries are produced via Registrate datagen.

To regenerate:
```bash
./gradlew :survive:runData
```

---

## Registration Pattern

This module uses **Registrate** (via the `:base` module) for deferred registration. Most registry objects are declared as lazy singletons in dedicated objects (e.g., `ModBlocks`, `ModEffects`) and initialized in `TheMod.init`.

Event wiring is primarily `@EventBusSubscriber`-based. Check existing subscriber patterns before adding manual event registration.

---

## License

MIT
