# ResourceRush

🎮 A lightweight Minecraft plugin that rewards players for breaking specific blocks.

## Features

- Configurable list of blocks in `blocks.yml`
- Easy to use
- Lightweight and efficient
- Extensible command system

## Commands

| Command                | Description                  |
|------------------------|------------------------------|
| `/resourcerush join`   | Join the resource race       |
| `/resourcerush leave`  | Leave the resource race      |
| `/resourcerush reload` | Reload plugin configuration  |

## Configuration

Inside `blocks.yml`, define the blocks:

```yaml
materials:
  blocks:
    - STONE
    - COAL_ORE
    - DIAMOND_ORE
