package xyz.webmc.maintenancemotdeagler.velocity;

import xyz.webmc.maintenancemotdeagler.base.IMaintenanceMOTDEaglerPlugin;
import xyz.webmc.maintenancemotdeagler.base.MaintenanceMOTDEagler;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.velocity.MaintenanceVelocityPlugin;
import net.lax1dude.eaglercraft.backend.server.api.velocity.event.EaglercraftMOTDEvent;

@SuppressWarnings({ "deprecation" })
public final class MaintenanceMOTDEaglerVelocity implements IMaintenanceMOTDEaglerPlugin {
  @Subscribe(priority = 99, order = PostOrder.LAST)
  public final void onEaglerMOTD(final EaglercraftMOTDEvent event) {
    MaintenanceMOTDEagler.handleMOTD(this, event);
  }

  @Override
  public final byte[] getFavicon(final MaintenancePlugin plugin) {
    final MaintenanceVelocityPlugin vPlugin = (MaintenanceVelocityPlugin) plugin;
    return MaintenanceMOTDEagler.getDataURIBytes(vPlugin.getFavicon().getBase64Url());
  }
}
