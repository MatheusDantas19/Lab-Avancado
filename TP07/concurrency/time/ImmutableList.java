package concurrency.time;
import java.util.*;

public class ImmutableList<T> implements Iterable<T> {
  ImmutableList<T> next;
  T item;

  private ImmutableList(ImmutableList<T> next, T item) {
    this.next = next; this.item=item;
  }

  public static<T> ImmutableList<T> add(ImmutableList<T> list, T item) {
    return new ImmutableList<T>(list, item);
  }

  public static<T> ImmutableList<T> remove(ImmutableList<T> list, T target) {
    if (list == null) return null;
    return list.remove(target);
  }

  private ImmutableList<T> remove(T target) {
    if (item == target) {
      return next;
    } else {
      ImmutableList<T> new_next = remove(next,target);
      if (new_next == next ) return this;
      return new ImmutableList<T>(new_next,item);
    }
  }

  public Iterator<T> iterator() {
        return new ImmutableListIterator<T>(this);
  }
}

final class ImmutableListIterator<T> implements Iterator<T> {

    private ImmutableList<T> current;

    ImmutableListIterator(ImmutableList<T> l){current=l;};

    public boolean hasNext() {return current != null;}

    public T next() {
      if (current!=null) {
        T o = current.item;
        current = current.next;
        return o;
      }
      throw new NoSuchElementException();
    }
	
	public void remove() {
			throw new UnsupportedOperationException();
    }

}