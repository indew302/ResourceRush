# 🌾 ResourceRush

🎮 A lightweight Minecraft plugin that rewards players for breaking specific blocks.

## ✅ Features

- 🔧 Configurable list of blocks in `blocks.yml`
- 📊 Customizable scoreboard in `config.yml`
- 🚀 Lightweight and efficient
- 📦 Simple, extensible command system
- 🔄 Live configuration reload support
- 🧑 Multiplayer support with point tracking

---

## ⚙️ Commands

| Command           | Permission           | Description                 |
|-------------------|----------------------|-----------------------------|
| `/collect join`   | `resourcerush.user`  | Join the resource race      |
| `/collect leave`  | `resourcerush.user`  | Leave the resource race     |
| `/collect reload` | `resourcerush.admin` | Reload plugin configuration |
| `/collect start`  | `resourcerush.admin` | Start the game manually     |
| `/collect stop`   | `resourcerush.admin` | Force-stop the game         |

All commands support tab-completion.

---

## 🧾 Configuration

### 🔹 `blocks.yml`

Specify the block types that grant points when broken:

```yaml
materials:
  blocks:
    - STONE
    - COAL_ORE
    - DIAMOND_ORE
    - IRON_ORE
