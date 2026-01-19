package xyz.webmc.maintenancemotdeagler.bungee;

import xyz.webmc.maintenancemotdeagler.base.IMaintenanceMOTDEaglerPlugin;
import xyz.webmc.maintenancemotdeagler.base.MaintenanceMOTDEagler;

import eu.kennytv.maintenance.bungee.MaintenanceBungeePlugin;
import eu.kennytv.maintenance.core.MaintenancePlugin;
import net.lax1dude.eaglercraft.backend.server.api.bungee.event.EaglercraftMOTDEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public final class MaintenanceMOTDEaglerBungee extends Plugin implements IMaintenanceMOTDEaglerPlugin, Listener {
  @Override
  public final void onEnable() {
    this.getProxy().getPluginManager().registerListener(this, this);
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public final void onEaglerMOTD(final EaglercraftMOTDEvent event) {
    MaintenanceMOTDEagler.handleMOTD(this, event);
  }

  @Override
  public final byte[] getFavicon(final MaintenancePlugin plugin) {
    final MaintenanceBungeePlugin bPlugin = (MaintenanceBungeePlugin) plugin;
    return MaintenanceMOTDEagler.getDataURIBytes(bPlugin.getFavicon().getEncoded());
  }
}
