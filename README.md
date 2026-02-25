# Pl3x API

A idiomatic Kotlin DSL wrapper around the [Pl3xMap](https://modrinth.com/plugin/pl3xmap) API for Minecraft (Spigot/Paper).  
Designed to be clean, modular, and zero-friction - no boilerplate, just expressive code.

---

## Requirements

| Dependency | Version         |
|------------|-----------------|
| Java       | 21+             |
| Kotlin     | 2.3.20-Beta2+   |
| Pl3xMap    | 1.21.11-539     |
| Spigot API | 1.21.11         |

---

## Installation

### Maven

Add the Nexus releases repository and the dependency to your `pom.xml`:

```xml

<repositories>
    <repository>
        <id>nexus-releases</id>
        <url>https://nexus.thenolle.com/repository/maven-releases/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.nolly</groupId>
        <artifactId>pl3x-api</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

### Gradle (Kotlin DSL)

```kotlin
repositories {
	maven("https://nexus.thenolle.com/repository/maven-releases/")
}

dependencies {
	implementation("com.nolly:pl3x-api:1.0.0")
}
```

---

## Setup

Call `Pl3xBootstrap.attach()` when your plugin enables and `Pl3xBootstrap.detach()` when it disables.  
All API calls are guarded - they throw if called before Pl3xMap is ready.

```kotlin
class MyPlugin : JavaPlugin() {
  override fun onEnable() {
    Pl3xBootstrap.attach()

    Pl3xBootstrap.onEnabled {
      // Pl3xMap is ready - safe to use Pl3xAPI here
      logger.info("Pl3xMap is ready!")
    }

    Pl3xBootstrap.onDisabled {
      // Pl3xMap went down
      logger.info("Pl3xMap disabled.")
    }
  }

  override fun onDisable() {
    Pl3xBootstrap.detach()
  }
}
```

---

## API Entry Point

Everything is accessed through the `Pl3xAPI` singleton.

```kotlin
// Worlds
val world: MapWorld = Pl3xAPI.world("world")
val worlds: List<MapWorld> = Pl3xAPI.worlds()

// Players
val players: List<MapPlayer> = Pl3xAPI.players()
val player: MapPlayer? = Pl3xAPI.player(uuid)

// Layers
val layer: MapLayer = Pl3xAPI.layer("my_layer")
val layers: List<MapLayer> = Pl3xAPI.layers()

// Raw Pl3xMap API access
val raw: Pl3xMap = Pl3xAPI.raw()
```

---

## Drawing Markers

Use the `draw` DSL to add markers to a world layer.  
All shapes are scoped to the layer automatically - no manual wiring needed.

```kotlin
Pl3xAPI.draw("world", "my_layer", "My Layer") {
  circle("spawn_zone") {
    center(0, 0)
    radius(100.0)
    fill("#FF000044")
    stroke("#FF0000")
    tooltip("Spawn Zone")
  }

  rectangle("base_area") {
    corners(-50, -50, 50, 50)
    fill("#00FF0033")
    stroke("#00FF00", weight = 2)
    popup("<b>Base Area</b>")
  }

  region("custom_zone") {
    polygon {
      point(0, 0)
      point(100, 0)
      point(100, 100)
      point(0, 100)
    }
    hole {
      point(30, 30)
      point(70, 30)
      point(70, 70)
      point(30, 70)
    }
    fill("#0000FF22")
    stroke("#0000FF")
  }
}
```

---

## Marker Types

| DSL Function      | Marker Type         | Required Fields                     |
|-------------------|---------------------|-------------------------------------|
| `circle`          | `MapCircle`         | `center()`, `radius()`              |
| `ellipse`         | `MapEllipse`        | `center()`, `radius(x, z)`          |
| `rectangle`       | `MapRectangle`      | `corners()`                         |
| `polyline`        | `MapPolyline`       | `point()` calls                     |
| `region`          | `MapRegion`         | `polygon {}` block                  |
| `iconMarker`      | `MapIconMarker`     | `center()`, `image(key)`            |
| `multiPolygon`    | `MapMultiPolygon`   | `addPolygon {}` blocks              |
| `multiPolyline`   | `MapMultiPolyline`  | `addLine {}` blocks                 |

### Icon Marker

```kotlin
Pl3xAPI.icon("my_icon")
  .image(File("plugins/MyPlugin/icons/marker.png"))
  .register()

Pl3xAPI.draw("world", "icons_layer") {
  iconMarker("player_home") {
    center(100, 200)
    image("my_icon")
    size(32, 32)
    anchor(16.0, 16.0)
    tooltip("Home")
  }
}
```

### Multi-Polygon

```kotlin
Pl3xAPI.draw("world", "territories") {
  multiPolygon("region_a") {
    addPolygon(
      outer = {
        point(0, 0); point(200, 0); point(200, 200); point(0, 200)
      },
      { point(50, 50); point(150, 50); point(150, 150); point(50, 150) } // hole
    )
    fill("#FF990033")
    stroke("#FF9900")
  }
}
```

---

## Shape Helpers

Utility extensions for common shapes on top of `MarkerBuilder`:

```kotlin
// A straight line between two points
markerBuilder.line(x1 = 0, z1 = 0, x2 = 100, z2 = 100)

// A centered square
markerBuilder.box(centerX = 0, centerZ = 0, halfSize = 50)

// A ring (donut shape)
markerBuilder.ring(
  centerX = 0,
  centerZ = 0,
  outerRadius = 100,
  innerRadius = 60,
  segments = 48
)
```

---

## Style DSL

All styling is applied through the `style {}` block or shorthand methods.

```kotlin
circle("styled_zone") {
  center(0, 0)
  radius(50.0)

  style {
    stroke("#FF0000", weight = 2)
    strokeDashPattern("5, 10")
    fill("#FF000033")
    tooltip("Danger Zone", sticky = true)
    popup(
      content = "<b>Danger!</b><br>Stay out.",
      maxWidth = 200
    )
  }
}
```

### Style Presets

Reuse a consistent style across multiple markers:

```kotlin
val dangerStyle = StylePreset {
  stroke("#FF0000", weight = 3)
  fill("#FF000033")
}

circle("zone_a") {
  center(0, 0); radius(50.0)
  applyPreset(dangerStyle)
}

rectangle("zone_b") {
  corners(-100, -100, 100, 100)
  applyPreset(dangerStyle)
}
```

---

## Layer Configuration

```kotlin
val layer = Pl3xAPI.world("world").layer("my_layer", "My Layer")

layer.configure {
  updateInterval = 10        // seconds
  showControls = true
  defaultHidden = false
  priority = 5
  zIndex = 10
  liveUpdate = true
  css = ".leaflet-pl3x-my_layer { opacity: 0.8; }"
}
```

### Layer Presets

```kotlin
val overlayPreset = LayerPreset {
  updateInterval = 30
  defaultHidden = true
  priority = 1
}

layer.applyPreset(overlayPreset)
```

---

## Layer Management

```kotlin
val world = Pl3xAPI.world("world")

// Get or create a layer
val layer = world.layer("my_layer", "My Layer")

// Get an existing layer or null
val maybeLayer = world.layerOrNull("my_layer")

// Remove a layer from the map
world.removeLayer("my_layer")

// Inline marker update (remove + re-add)
layer.update("my_circle", MarkerBuilder.Type.CIRCLE) {
  center(0, 0)
  radius(75.0)
  fill("#00FF0044")
}
```

---

## Player Drawing

Draw markers in the world of a specific player, with position helpers:

```kotlin
val player: MapPlayer = Pl3xAPI.player(uuid) ?: return

player.drawOn("player_markers", "Player Markers") {
  circle("my_position") {
    centerHere()         // uses player's current position
    radius(16.0)
    stroke("#FFFFFF")
    tooltip(player.name)
  }

  icon("my_icon") {
    centerHere()
    image("player_pin")
    size(24)
  }
}
```

---

## World Iteration

```kotlin
// All worlds
forEachWorld {
  draw("icons", "Icons") {
    // ...
  }
}

// Worlds matching a condition
forWorlds(predicate = { it.startsWith("survival") }) {
  layer("pvp_zones")?.clearMarkers()
}

// Named worlds only
forWorlds("world", "world_nether") {
  draw("borders", "World Borders") {
    // ...
  }
}
```

---

## Event Bridge

Subscribe to Pl3xMap and world lifecycle events:

```kotlin
Pl3xAPI.events.onWorldLoad { world ->
  logger.info("World loaded: ${world.name}")
}

Pl3xAPI.events.onWorldUnload { world ->
  logger.info("World unloaded: ${world.name}")
}

Pl3xAPI.events.onMapEnabled {
  logger.info("Pl3xMap enabled")
}

Pl3xAPI.events.onMapDisabled {
  logger.info("Pl3xMap disabled")
}

Pl3xAPI.events.onServerLoaded {
  logger.info("Server fully loaded")
}
```

---

## Render Control

```kotlin
val world = Pl3xAPI.world("world")

// Full re-render
world.render.full()

// Render a specific region
world.render.region(regionX = 0, regionZ = 0)

// Render around a block coordinate
world.render.regionAtBlock(blockX = 100, blockZ = 200)

// Radius-based render
world.render.radiusRender(centerX = 0, centerZ = 0, radiusBlocks = 512)

// Pause / resume
world.render.pause()
world.render.resume()

// Progress info
val progress = world.render.progress()
println("${progress.percent}% - ETA: ${progress.eta}")
```

### DSL Render Helpers

```kotlin
world.renderAroundSpawn(radiusBlocks = 1024)
world.renderAround(blockX = 500, blockZ = -200, radiusBlocks = 256)
world.renderPlayerRegions()  // renders only regions where players currently are
```

---

## MapPlayer Reference

```kotlin
val player: MapPlayer = Pl3xAPI.player(uuid) ?: return

player.uuid          // UUID
player.name          // String
player.position      // Point (x, z)
player.yaw           // Float
player.health        // Int
player.armorPoints   // Int
player.worldName     // String
player.isHidden      // Boolean
player.isNpc         // Boolean
player.isInvisible   // Boolean
player.isSpectator   // Boolean
player.skinUrl       // URL?

player.setHidden(true, persistent = true)
player.raw()         // raw Pl3xMap Player
```

---

## License

This project is licensed under the [Nolly’s Pl3x API Proprietary License v1.4](LICENSE.md).  
All rights reserved © 2026 Nolly. Commercial use requires explicit permission.

> Use, modification, and distribution are allowed only under the terms of the license. Attribution is required. Commercial use requires permission.
