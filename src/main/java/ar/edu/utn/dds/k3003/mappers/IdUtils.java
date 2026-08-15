package ar.edu.utn.dds.k3003.mappers;

public final class IdUtils {
  private IdUtils() {}

  public static Long parse(String id) {
    return id == null ? null : Long.valueOf(id);
  }

  public static String stringify(Long id) {
    return id == null ? null : String.valueOf(id);
  }
}
