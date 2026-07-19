# CrossBorderCore 🌐

A highly optimized and lightweight Minecraft plugin designed to handle seamless player and entity transitions across local-world borders or multiple servers (Cross-Server) for sharded Minecraft worlds. Engineered from the ground up to be fully compatible with **Folia** and **Paper** server software.

> [!NOTE]
> This plugin has been fully developed and maintained by **hamza**.

---

## 🚀 Key Features

* **Modern Server Software Support:** Fully compatible with **Folia** (region-threaded scheduling) and **Paper**, ensuring high-performance scheduling without blocking region threads.
* **Entity & Vehicle Teleportation:**
  * Teleports players along with their vehicles (boats, minecarts, horses) including their passengers (e.g., villagers in boats).
  * Teleports leashed mobs.
  * Teleports nearby villagers and allays (adjustable search radius).
  * Highly configurable entity and vehicle filtering systems (Whitelist/Blacklist).
  * Built-in limit for maximum teleported entities per player to prevent performance abuse and duplication exploits.
* **Velocity & BungeeCord Integration via Redis:**
  * Fast and secure player redirection across a multi-server setup.
  * Dedicated asynchronous Redis worker threads (never executes network/IO operations on region threads).
  * Heartbeat status reporting: continuously updates the target server status to prevent sending players to offline destinations.
* **Visual Barrier & Particle Effects:**
  * Displays a visual boundary/barrier using particles when players approach configured borders.
  * Customizable particle types (e.g., `FLAME`, `REDSTONE`, `PORTAL`, `END_ROD`) and viewing distance.
* **External Teleport Protection:**
  * Prevents external teleport commands or random teleport (RTP) plugins from dropping players directly onto active borders or seams to prevent infinite teleport loops.

---

## 🛠️ Requirements

* **Java Version:** Java 21 or newer.
* **Minecraft Version:** 1.18+ (1.21+ is highly recommended).
* **Server Software:** Paper or Folia.
* **Redis Database:** (Optional; only required for cross-server setups).

---

## 📦 Installation

1. Download the `CrossBorderCore` jar file.
2. Place the jar file inside your server's `plugins` folder.
3. Restart the server to generate default configurations.
4. Customize `config.yml` (see guide below).
5. Reload the plugin configuration using `/cbc reload`.

---

## ⚙️ Configuration Guide

The `config.yml` file contains the primary settings for the plugin. Here is an overview of key sections:

### 1. Entity Teleportation Settings (`entity-teleportation`)
Control which entities teleport alongside the player:

```yaml
entity-teleportation:
  enabled: true                    # Enable/disable entity teleportation with players
  teleport-vehicles: true          # Teleport vehicles (minecarts, boats, horses)
  teleport-leashed-mobs: true      # Teleport mobs attached to leads
  teleport-nearby-villagers: true  # Teleport nearby villagers and allays
  nearby-entity-distance: 3        # Search radius around player (default: 3 blocks)
  max-entities: 10                 # Maximum entities to teleport per player
  mobs_must_be_leashed: false      # If true, only leashed mobs will teleport
  mobs:
    exclusion: true                # true = blacklist (exclude list), false = whitelist (only list)
    list:                          # List of mob entity types
      - WITHER
      - ENDER_DRAGON
```

### 2. Redis Configuration (`redis`)
Used for multi-server setups (BungeeCord/Velocity):

```yaml
redis:
  enabled: false                   # Enable when configuring cross-server setups
  host: localhost
  port: 6379
  password: ""
  database: 0                      # Database index (0-15)
  key-prefix: "crossservertp:"     # Prevents database key collisions
  async-threads: 2                 # Dedicated Redis worker threads
  status-cache-ttl-ms: 3000        # Server status cache validity period
  transfer-timeout-ms: 3000        # Release player back to source shard if Redis is slow
```

### 3. Visual Particle Settings
Visual boundary particle settings:

```yaml
particle-view-distance: 50         # Distance (in blocks) at which particles are visible
particle-type: FLAME               # Particle style (e.g., FLAME, REDSTONE, PORTAL, END_ROD)
```

### 4. Border Definitions (`borders`)
Define where the active boundaries are located:

```yaml
borders:
  # Example local-world transfer from world to world2 at the NORTH border
  world-north-to-world2:
    world: world                   # Source world name
    side: NORTH                    # Border side (NORTH, SOUTH, EAST, WEST)
    coordinate: 0                  # Border boundary coordinate (e.g. Z = 0)
    target-world: world2           # Destination world name
    target-side: SOUTH             # Destination side player spawns at
    target-coordinate: 0           # Target boundary coordinate
    target-offset: 5               # Blocks inside target border to prevent teleport loop
    show-particles: true           # Enable boundary particles for this border
```

---

## 💻 Commands & Permissions

### Commands
The plugin's main command is `/crossbordercore` (Aliases: `/cbc`, `/cborder`, `/cbcore`).

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/cbc help` | Displays the help and commands menu | `crossbordercore.use` |
| `/cbc info` | Shows plugin details and runtime status | `crossbordercore.info` |
| `/cbc list` | Lists all configured active borders | `crossbordercore.list` |
| `/cbc reload` | Reloads the configuration file | `crossbordercore.reload` |

### Permissions
* `crossbordercore.*` - Grants all permissions (OPs).
* `crossbordercore.use` - Allows using basic commands (default: true for all players).

---

## 🗺️ Layout & Architecture Example

To maintain continuous coordinates in a sharded world setup (e.g., 4 worlds):

```
                world2 (NORTH)
                 Z = -30000..0
                       |
world3 (WEST)   <--  world  -->  world4 (EAST)
X = -30000..0      X = 0..30000    X = 30000..60000
                       |
                   (SOUTH)
```

When a player reaches the edge of `world` towards the `EAST` boundary at coordinate `30,000`, they are seamlessly transitioned to `world4` at coordinate `30,005` (using a `5` block safety offset to prevent rubber-banding/teleport loops).

---

## 👨‍💻 Developer & Credits

Designed and maintained for high-performance Minecraft servers and networks.
* **Lead Developer:** **hamza**
* **Project Team:** alphaStudio (t7o)
