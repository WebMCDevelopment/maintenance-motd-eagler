package xyz.webmc.maintenancemotdeagler.base;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import eu.kennytv.maintenance.api.Maintenance;
import eu.kennytv.maintenance.api.MaintenanceProvider;
import eu.kennytv.maintenance.core.MaintenancePlugin;
import eu.kennytv.maintenance.core.Settings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.lax1dude.eaglercraft.backend.server.api.event.IEaglercraftMOTDEvent;
import net.lax1dude.eaglercraft.backend.server.api.query.IMOTDConnection;

@SuppressWarnings({ "rawtypes" })
public final class MaintenanceMOTDEagler {
  private static final Maintenance api;
  private static final Settings settings;
  private static final MaintenancePlugin mt;

  public static final void handleMOTD(final IMaintenanceMOTDEaglerPlugin plugin, final IEaglercraftMOTDEvent event) {
    if (api.isMaintenance()) {
      final IMOTDConnection conn = event.getMOTDConnection();
      boolean changed = false;
      if (settings.isEnablePingMessages()) {
        final List<String> messages = Mirror.access(settings, "pingMessages");
        final List<String> msgLines = new ArrayList<>();
        if (!messages.isEmpty()) {
          for (final String msg : messages) {
            final Component comp = MiniMessage.miniMessage().deserialize(msg);
            final String message = LegacyComponentSerializer.legacySection().serialize(comp);
            msgLines.add(message);
          }
          conn.setServerMOTD(msgLines);
          changed = true;
        }
      }
      if (settings.hasCustomIcon()) {
        conn.setServerIcon(convertFavicon(plugin.getFavicon(mt)));
        changed = true;
      }
      if (settings.hasCustomPlayerCountMessage()) {
        conn.setPlayerUnlimited();
        changed = true;
      }
      if (settings.hasCustomPlayerCountHoverMessage()) {
        final List<String> messages = Mirror.access(settings, "legacyParsedPlayerCountHoverLines");
        final List<String> msgLines = new ArrayList<>();
        if (!messages.isEmpty()) {
          for (final String msg : messages) {
            msgLines.add(msg);
          }
          conn.setPlayerList(msgLines);
          changed = true;
        }
      }
      if (changed) {
        conn.sendToUser();
        conn.disconnect();
      }
    }
  }

  public static final byte[] getDataURIBytes(final String uri) {
    final String data = uri.substring(uri.indexOf(",") + 1);
    return Base64.getDecoder().decode(data);
  }

  private static final byte[] convertFavicon(final byte[] favicon) {
    try {
      BufferedImage img = ImageIO.read(new ByteArrayInputStream(favicon));
      if (img.getWidth() != 64 || img.getHeight() != 64) {
        final Image scaled = img.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        final BufferedImage tmp = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = tmp.createGraphics();
        g.drawImage(scaled, 0, 0, null);
        g.dispose();
        img = tmp;
      } else if (img.getType() != BufferedImage.TYPE_INT_ARGB) {
        final BufferedImage tmp = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = tmp.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        img = tmp;
      }

      final int[] px = img.getRGB(0, 0, 64, 64, null, 0, 64);
      final byte[] ret = new byte[64 * 64 * 4];

      int o = 0;
      for (int i = 0; i < px.length; i++) {
        final int p = px[i];
        ret[o++] = (byte) ((p >> 16) & 0xFF);
        ret[o++] = (byte) ((p >> 8) & 0xFF);
        ret[o++] = (byte) (p & 0xFF);
        ret[o++] = (byte) ((p >> 24) & 0xFF);
      }

      return ret;
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  static {
    api = MaintenanceProvider.get();
    settings = (Settings) api.getSettings();
    mt = Mirror.access(settings, "plugin");
  }
}
