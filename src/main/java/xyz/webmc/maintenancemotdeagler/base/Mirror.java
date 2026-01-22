package xyz.webmc.maintenancemotdeagler.base;

import java.lang.reflect.Field;

@SuppressWarnings({ "unchecked" })
public final class Mirror {
  public static final <T> T access(final Object obj, final String field) {
    try {
      Class<?> c = obj.getClass();
      Field f = null;

      while (c != null) {
        if (hasField(c, field)) {
          f = c.getDeclaredField(field);
          c = null;
        } else if (c.getSuperclass() != null) {
          c = c.getSuperclass();
        } else {
          c = null;
        }
      }

      if (f != null) {
        f.setAccessible(true);
        return (T) f.get(obj);
      } else {
        throw new NoSuchFieldException(field);
      }
    } catch (final ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  private static final boolean hasField(final Class<?> c, final String field) {
    try {
      c.getDeclaredField(field);
      return true;
    } catch (final NoSuchFieldException e) {
      return false;
    }
  }
}
