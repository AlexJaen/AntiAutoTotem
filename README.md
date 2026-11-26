<h1 align="center">🛡️ AntiAutoTotem</h1>
<p align="center"><b>Neutralises "auto-totem" macros without ever flagging a legitimate player.</b></p>

<p align="center">
  <img alt="Version" src="https://img.shields.io/badge/version-1.0.1-blue">
  <img alt="Paper" src="https://img.shields.io/badge/Paper-1.21.x%20%7C%2026.x-success">
  <img alt="Java" src="https://img.shields.io/badge/Java-21%2B-orange">
  <img alt="License" src="https://img.shields.io/badge/license-MIT-lightgrey">
</p>

| | |
|---|---|
| **Version** | 1.0.0 |
| **Platform** | Paper 1.21.x and 26.x (built against 1.21.11, Java 21) |
| **Dependencies** | None |
| **Command** | `/antiautototem reload` |
| **Config** | 2 options, tiny footprint |

## What is it?

"Auto-totem" hacks re-equip a Totem of Undying in the same tick the previous one pops. Anticheats like GrimAC or Vulcan have no dedicated check for it, because an instant re-equip is indistinguishable from a fast human… at the server level.

**AntiAutoTotem doesn't try to detect it — it mitigates it.** Right after a player consumes a totem, their off-hand slot is briefly locked (default **0.3 s / 6 ticks**). A human re-equips later than that, so nobody notices; a macro that refills the off-hand instantly loses its edge.

## How it works

When a player's totem pops (`EntityResurrectEvent`), the plugin starts a short lock. During the lock these actions on the off-hand are cancelled:

- clicking the off-hand slot in the inventory,
- dragging items onto the off-hand slot,
- swapping hands with the **F** key.

Once the lock expires everything works as usual. Players with a lock active get a subtle message (optional).

## Features

- ✅ **Zero false positives** — it never punishes or flags anyone.
- ⚡ **Extremely light** — one map entry per player, no scheduler tasks.
- 🔧 **Two settings** — lock duration and message.
- 🔁 `/antiautototem reload` — no restart needed.
- 🤝 Works alongside any anticheat.

## Configuration

`plugins/AntiAutoTotem/config.yml`:

```yaml
cooldown-ticks: 6     # lock duration in ticks (20 ticks = 1 s)
notify-player: true   # tell the player when they try to equip during the lock
notify-message: "&7Wait a moment before re-equipping your off-hand..."
```

## Commands & permissions

| Command | Description | Permission | Default |
|---|---|---|---|
| `/antiautototem reload` | Reload the configuration | `antiautototem.admin` | op |

## Installation

1. Download `antiautototem-<version>.jar` from [Releases](../../releases).
2. Put it in `plugins/` and restart.

## Build from source

```bash
git clone https://github.com/AlexJaen/AntiAutoTotem.git
cd AntiAutoTotem
mvn clean package
```

Requires JDK 21+ and Maven. The jar is written to `target/antiautototem-<version>.jar`.

## License

MIT — see [LICENSE](LICENSE).

---

### 🇪🇸 Resumen

Mitiga (no detecta) los macros de *auto-totem*: tras consumir un tótem, el slot de la mano secundaria se bloquea ~0,3 s. Un jugador humano no lo nota; un macro que rellena la mano al instante pierde su ventaja. Sin falsos positivos, sin dependencias. Configurable en `config.yml` (`cooldown-ticks`, `notify-player`, `notify-message`).
