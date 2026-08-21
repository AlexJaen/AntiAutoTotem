# AntiAutoTotem

Plugin para Paper 1.21.x que **mitiga** (no detecta) los macros de "autototem". Los anticheats convencionales (GrimAC, Vulcan) no tienen ningún check dedicado para esto, ya que el re-equipado rápido de totems es indistinguible de un jugador humano con buenos reflejos.

En vez de intentar detectarlo, el plugin fuerza un breve bloqueo del slot de la mano secundaria justo después de consumir un totem, para que un macro que rellena el offhand al instante no tenga ningún efecto extra sobre lo que ya podría hacer un jugador legítimo.

## Comandos

| Comando | Descripción | Permiso |
|---|---|---|
| `/antiautototem reload` | Recarga la configuración | `antiautototem.admin` |

## Instalación

Descarga el `.jar` desde [Releases](../../releases) (o compílalo tú mismo, ver abajo) y colócalo en la carpeta `plugins/` de tu servidor Paper 1.21.x.

## Compilar desde el código fuente

Requiere JDK 21 y Maven.

```
mvn package
```

El `.jar` resultante queda en `target/antiautototem-<version>.jar`.

## Autor

Lorealex
