package concurrency.connector;

import java.util.*;

public class TupleSpaceImpl implements TupleSpace {
  private HashMap<String,ArrayList> tuples = new HashMap<String, ArrayList>(); 

  // deposits data in tuple space
  public synchronized void out (String tag, Object data) {
    ArrayList v = tuples.get(tag);
    if (v==null) {
        v=new ArrayList();
        tuples.put(tag,v);
    }
    v.add(data);
    notifyAll();
  }

  private Object get(String tag, boolean remove) {
    ArrayList v = tuples.get(tag);
    if (v==null) return null;
    if (v.size()==0) return null;
    Object o = v.get(0);
    if (remove) v.remove(0);
    return o;
  }

  // extracts object with tag from tuple space, blocks if not available
  public synchronized Object in (String tag) throws InterruptedException {
    Object o;
    while ((o=get(tag,true))==null) wait();
    return o;
  }

  // reads object with tag from tuple space, blocks if not available
  public synchronized Object rd (String tag) throws InterruptedException {
    Object o;
    while ((o=get(tag,false))==null) wait();
    return o;
  }

  // extracts object if available, return null if not available
  public synchronized Object inp (String tag) {
    return get(tag,true);
  }

  //reads object if available, return null if not available
  public synchronized Object rdp (String tag) {
    return get(tag,false);
  }

}