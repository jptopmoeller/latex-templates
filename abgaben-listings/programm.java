package pst.gcsim.GarbageCollectors;
import java.util.*;

/**
 * Klasse zur Realisierung eines Mark-Sweep-Kollektors.
 */
public class MarkSweep<T extends Allocator> extends GarbageCollector<T> {
  public static final int MS_WHITE = 0;
  LinkedList<HeapObject> toScan;
  
  public MarkSweep(CollectionController<T> controller) {
    this.toScan = new LinkedList<>();
    this.controller = controller;
  }
  
  /**
   * Ausführung der Bereinigungsphase (ohne Animation).
   */
  void sweep() {
    controller.getObjects().sort(new AddressComparator());
    // Verhinderung von ConcurrentModificationExceptions
    ArrayList<HeapObject> toRemove = new ArrayList<>();
    for (HeapObject obj : controller.getObjects())
      if (obj.getMark() == MS_WHITE) toRemove.add(obj);
      else obj.setMark(MS_WHITE);
    toRemove.forEach(obj -> {
      controller.getAllocator().free(obj);
      controller.getObjects().remove(obj);
    });
  }
}
