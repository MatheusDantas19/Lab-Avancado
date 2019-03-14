package concurrency.connector;

public interface TupleSpace {

  // deposits data in tuple space
  public void out (String tag, Object data);

  // extracts object with tag from tuple space, blocks if not available
  public Object in (String tag) throws InterruptedException;

  // reads object with tag from tuple space, blocks if not available
  public Object rd (String tag) throws InterruptedException;


  // extracts object if available, return null if not available
  public Object inp (String tag);

  //reads object if available, return null if not available
  public Object rdp (String tag);

}