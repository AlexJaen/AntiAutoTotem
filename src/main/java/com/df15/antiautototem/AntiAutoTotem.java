package com.df15.antiautototem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiAutoTotem extends JavaPlugin implements Listener
{
    private static final int OFFHAND_SLOT = 40;

    private int cooldownTicks;
    private boolean notifyPlayer;
    private String notifyMessage;

    // UUID del jugador -> tick del servidor (Bukkit.getCurrentTick()-like,
    // usamos System.currentTimeMillis() para no depender de un contador
    // de ticks propio) en el que expira el bloqueo.
    private final Map<UUID, Long> lockedUntilMillis = new HashMap<>();

    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        this.loadSettings();

        getServer().getPluginManager().registerEvents(this, this);

        if(getCommand("antiautototem") != null)
        {
            getCommand("antiautototem").setExecutor((sender, command, label, args) ->
            {
                this.reloadConfig();
                this.loadSettings();
                sender.sendMessage(ChatColor.GREEN + "[AntiAutoTotem] Configuracion recargada.");
                return true;
            });
        }
    }

    private void loadSettings()
    {
        this.cooldownTicks = getConfig().getInt("cooldown-ticks", 6);
        this.notifyPlayer = getConfig().getBoolean("notify-player", true);
        this.notifyMessage = ChatColor.translateAlternateColorCodes('&',
            getConfig().getString("notify-message", "&7Espera un instante antes de re-equipar la mano secundaria..."));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onResurrect(EntityResurrectEvent e)
    {
        if(!(e.getEntity() instanceof Player))
        {
            return;
        }

        Player player = (Player) e.getEntity();

        long lockMillis = (this.cooldownTicks * 50L); // 1 tick = 50ms

        this.lockedUntilMillis.put(player.getUniqueId(), System.currentTimeMillis() + lockMillis);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent e)
    {
        if(!(e.getWhoClicked() instanceof Player))
        {
            return;
        }

        Inventory clicked = e.getClickedInventory();

        // Solo nos importa el slot 40 (offhand) DENTRO del inventario del
        // propio jugador -- ignoramos clicks en cofres/otros contenedores.
        if(clicked == null
        || clicked.getType() != InventoryType.PLAYER
        || e.getSlot() != OFFHAND_SLOT)
        {
            return;
        }

        this.blockIfLocked((Player) e.getWhoClicked(), e::setCancelled);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent e)
    {
        if(!(e.getWhoClicked() instanceof Player))
        {
            return;
        }

        if(!e.getRawSlots().contains(OFFHAND_SLOT))
        {
            return;
        }

        this.blockIfLocked((Player) e.getWhoClicked(), e::setCancelled);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onSwapHands(PlayerSwapHandItemsEvent e)
    {
        // La tecla F (swap main/offhand) es el metodo mas comun de
        // "autototem" -- macro que pulsa F en cuanto detecta que el
        // totem se ha gastado, para recolocar otro al instante.
        this.blockIfLocked(e.getPlayer(), e::setCancelled);
    }

    private void blockIfLocked(Player player, java.util.function.Consumer<Boolean> cancel)
    {
        Long until = this.lockedUntilMillis.get(player.getUniqueId());

        if(until == null)
        {
            return;
        }

        long now = System.currentTimeMillis();

        if(now >= until)
        {
            this.lockedUntilMillis.remove(player.getUniqueId());
            return;
        }

        cancel.accept(true);

        if(this.notifyPlayer && this.notifyMessage != null && !this.notifyMessage.isEmpty())
        {
            player.sendMessage(this.notifyMessage);
        }
    }
}
