package xyz.webmc.maintenancemotdeagler.bukkit;

import xyz.webmc.maintenancemotdeagler.base.IMaintenanceMOTDEaglerPlugin;
import xyz.webmc.maintenancemotdeagler.base.MaintenanceMOTDEagler;

import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.spigot.MaintenanceSpigotPlugin;
import net.lax1dude.eaglercraft.backend.server.api.bukkit.event.EaglercraftMOTDEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class MaintenanceMOTDEaglerBukkit extends JavaPlugin implements IMaintenanceMOTDEaglerPlugin, Listener {
  @Override
  public final void onEnable() {
    this.getServer().getPluginManager().registerEvents(this, this);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public final void onEaglerMOTD(final EaglercraftMOTDEvent event) {
    MaintenanceMOTDEagler.handleMOTD(this, event);
  }

  @Override
  public final byte[] getFavicon(final MaintenancePlugin plugin) {
    final MaintenanceSpigotPlugin sPlugin = (MaintenanceSpigotPlugin) plugin;
    System.out.println(sPlugin.getFavicon().getClass().getName());
    return null;
  }
}
