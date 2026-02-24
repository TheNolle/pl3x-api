# Contributing to Pl3x API

Thanks for taking the time to contribute.  
This document covers everything you need to get started - environment setup, code style, branching, and the PR process.

---

## Table of Contents

- [Requirements](#requirements)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Code Style](#code-style)
- [Branching Strategy](#branching-strategy)
- [Making Changes](#making-changes)
- [Pull Request Guidelines](#pull-request-guidelines)
- [Commit Convention](#commit-convention)
- [Reporting Issues](#reporting-issues)
- [What Not to Do](#what-not-to-do)

---

## Requirements

| Tool       | Version         |
|------------|-----------------|
| JDK        | 21+             |
| Kotlin     | 2.3.20-Beta2+   |
| Maven      | 3.9+            |
| Pl3xMap    | 1.21.11-539     |
| Spigot API | 1.21.11         |

You also need a local Spigot API installed via BuildTools before the project will resolve:

```bash
# Run once to install Spigot API locally
java -jar BuildTools.jar --rev 1.21.11
```

---

## Getting Started

```bash
# Clone the repo
git clone https://github.com/thenolle/pl3x-api.git
cd pl3x-api

# Verify the build
mvn clean package
```

If `mvn clean package` produces a jar in `target/` without errors, you're set up correctly.

---

## Project Structure

```
src/main/kotlin/com/nolly/pl3x/
│
├── Pl3xAPI.kt                  # Public singleton entry point
├── Pl3xBootstrap.kt            # Lifecycle manager (attach/detach)
├── Pl3xContext.kt              # Internal state - registries + readiness flag
│
├── dsl/                        # All DSL extensions and scopes
│   ├── LayerScope.kt           # draw {} block context
│   ├── LayerPreset.kt          # Reusable layer configuration
│   ├── StylePreset.kt          # Reusable marker style configuration
│   ├── MarkerUpdateScope.kt    # layer.update() helper
│   ├── PlayerDrawing.kt        # player.drawOn {} scope
│   ├── RenderDsl.kt            # renderAroundSpawn(), renderAround(), etc.
│   ├── Shapes.kt               # line(), box(), ring() helpers
│   └── WorldSelection.kt       # forEachWorld(), forWorlds()
│
├── event/
│   └── Pl3xEventBridge.kt      # Thread-safe event subscription bridge
│
├── icon/
│   └── IconBuilder.kt          # Fluent icon image registration
│
├── layer/
│   └── MapLayer.kt             # Wraps Pl3xMap SimpleLayer
│
├── marker/                     # All marker types + builder + style DSL
│   ├── MapMarker.kt            # Sealed base class
│   ├── MapCircle.kt
│   ├── MapEllipse.kt
│   ├── MapRectangle.kt
│   ├── MapPolyline.kt
│   ├── MapRegion.kt
│   ├── MapIconMarker.kt
│   ├── MapMultiPolygon.kt
│   ├── MapMultiPolyline.kt
│   ├── MarkerBuilder.kt        # Fluent builder for all marker types
│   ├── ShapeDSL.kt             # PointsBuilder
│   └── StyleDSL.kt             # Options/style builder
│
├── player/
│   └── MapPlayer.kt            # Wraps Pl3xMap Player
│
├── registry/                   # All internal ConcurrentHashMap registries
│   ├── WorldRegistry.kt
│   ├── LayerRegistry.kt
│   ├── MarkerRegistry.kt
│   └── IconRegistry.kt
│
├── render/
│   ├── RenderController.kt     # Per-world render trigger API
│   ├── RenderProgress.kt       # Render progress data
│   └── DoubleCheckerController.kt
│
└── world/
    ├── MapWorldConfig.kt       # World-specific config
    └── MapWorld.kt             # Wraps Pl3xMap World
```

---

## Code Style

This project follows a strict style. Match it exactly - no exceptions.

### General

- **No semicolons**
- **Tabs** for indentation, not spaces
- **DRY** - no repeated logic; extract to functions or extensions
- **OOP** - group related behavior into classes, not top-level function dumps
- **KDoc on everything public** - classes, functions, properties

### Kotlin Specifics

- Prefer `val` over `var` everywhere possible
- Prefer expression bodies (`fun x() = ...`) for single-expression functions
- Use `apply`, `also`, `let`, `run` idiomatically - don't chain them just to look clever
- Extension functions over utility classes
- No `!!` - use `requireNotNull()` with a message, or safe calls with `?:`
- `object` for singletons, never a class with a companion holding state

### Documentation

Every public class, function, and property must have a KDoc comment:

```kotlin
/**
 * Returns the [MapLayer] for this world, creating and registering it if it doesn't exist yet.
 *
 * @param key Stable identifier for the layer. Used as the Pl3xMap layer key.
 * @param label Display name shown in the Pl3xMap UI. Defaults to [key].
 * @return The existing or newly created [MapLayer].
 */
fun layer(key: String, label: String = key): MapLayer
```

Internal (`internal` / `private`) members don't need KDoc but should have a short inline comment if the logic isn't immediately obvious.

---

## Branching Strategy

| Branch | Purpose                                            |
|--------|----------------------------------------------------|
| `main` | Stable, released code only - never commit directly |
| `dev` | Active development - base branch for all PRs       |
| `feature/<name>` | New features, branched from `dev`                  |
| `fix/<name>` | Bug fixes, branched from `dev`                     |
| `chore/<name>` | Maintenance (deps, CI, docs), branched from `dev`  |

**Never push directly to `main`.** It is updated only via the release flow (`mvn release:prepare` + `mvn release:perform`).

---

## Making Changes

```bash
# Start from dev
git checkout dev
git pull origin dev

# Create your branch
git checkout -b feature/my-feature

# Make changes, then build to verify
mvn clean package

# Commit (see commit convention below)
git add .
git commit -m "feat: add ring shape helper to MarkerBuilder"

# Push
git push origin feature/my-feature
```

Then open a PR from your branch → `dev`.

---

## Pull Request Guidelines

- **One concern per PR** - don't bundle a feature and a refactor together
- **Title** follows the commit convention (see below)
- **Description** must include:
    - What changed and why
    - Any breaking changes (clearly marked)
    - Any follow-up work left out intentionally
- PRs that break the build will not be merged
- PRs without KDoc on new public API will not be merged
- Keep diffs small and readable - large PRs will be asked to split

---

## Commit Convention

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>: <short description>
```

| Type | When to use                                             |
|------|---------------------------------------------------------|
| `feat` | New feature or DSL addition                             |
| `fix` | Bug fix                                                 |
| `docs` | Documentation only                                      |
| `refactor` | Code change that neither fixes a bug nor adds a feature |
| `chore` | Dependency bumps, CI, build config                      |
| `style` | Formatting, whitespace - no logic change                |
| `perf` | Performance improvement                                 |

**Examples:**

```
feat: add ring() shape helper to Shapes.kt
fix: remove() in LayerScope now correctly syncs MarkerRegistry
docs: add KDoc to all MapLayer properties
chore: bump Kotlin to 2.3.20-Beta2
refactor: extract compositeKey logic into LayerRegistry
```

Breaking changes get a `!` suffix and a footer:

```
feat!: rename draw() worldName param to name

BREAKING CHANGE: draw(worldName, ...) is now draw(name, ...)
```

---

## Reporting Issues

Use [GitHub Issues](https://github.com/thenolle/pl3x-api/issues) with the appropriate label.

A good bug report includes:

- Pl3x API version
- Pl3xMap version + Minecraft version
- Minimal reproduction (code snippet is enough)
- What you expected vs. what actually happened
- Stack trace if applicable

A good feature request includes:

- The use case driving it - not just the proposed API
- A sketch of what the DSL or API surface would look like
- Whether it's a thin wrapper over existing Pl3xMap functionality or something new

---

## What Not to Do

- **Don't add dependencies** without discussing it first - this library is intentionally lightweight
- **Don't expose raw Pl3xMap types** in new public API - always wrap or map them
- **Don't remove the `requireReady()` guard** from any public-facing method
- **Don't use `Thread.sleep()`**, blocking calls, or synchronous I/O on the main thread
- **Don't commit IDE files** - `.idea/`, `.iml`, etc. are in `.gitignore` for a reason
- **Don't commit `settings.xml`** or any file containing credentials

---

## License

By contributing, you agree that your contributions will be covered under the project's license.  
See [LICENSE](../LICENSE) for full terms.
