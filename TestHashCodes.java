import java.util.Arrays;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Scanner;

public class TestHashCodes {
  public static void main(String[] args) {
    CustomHashTable<Integer,Integer> map = new CustomHashTable<>(3);

    Scanner sc = new Scanner(System.in);
    System.out.println("Enter Number of entries");
    int entries = 0;

    try {
      entries = sc.nextInt();
    }
    catch (InputMismatchException e) {
      System.out.println(e.getMessage());
      System.exit(0);
    }


    sc.nextLine();

    while (entries > 0) {

      System.out.print("Enter Key Val:");

      String[] values = sc.nextLine().split(" ");


      if (values.length != 2) {
        System.out.println("Provide two values, try again");
        continue;
      }
      int key, val;
      try {
        key = Integer.parseInt(values[0]);
        val = Integer.parseInt(values[1]);
      }
      catch (NumberFormatException e) {
        System.out.println("Number format exception try again");
        continue;
      }
      map.put(key, val);

      entries--;
    }


      System.out.print("Enter a key to test the output:");
      int out = map.get(sc.nextInt());
      System.out.println("Values is " + out);













  }
}

class Person1 extends Persona{

  int id;
  String name;
  public Person1(int id) {
    this.id = id;
    name = "khan";
  }

  public int hashCode() {
    return Objects.hashCode(id);
  }
}

class Persona {

  String type;

  public Persona() {
    type = "pp";
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Persona)) return false;
    Persona persona = (Persona) o;
    return type.equals(persona.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type);
  }
}

class CustomHashTable<K,V> implements Map<K,V> {
  private CustomList<CustomListNode<K,V>>[] array;
  private int mapSize;
  public CustomHashTable(int size) {

    this.array = new CustomList[size];
    mapSize = 0;

  }

  public void printDict() {
    for (int i = 0; i < array.length; i++) {
      if (array[i] == null) {
        System.out.println(i+" null");
      }
      else {
        System.out.print(i);
        for (CustomListNode<?,?> n : array[i] ) {
          System.out.print(" {"+n.key+" "+n.value+"},");
        }
        System.out.println();
      }

    }
  }

  @Override
  public int size() {
    return mapSize;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean containsKey(Object key) {
    return false;
  }

  @Override
  public boolean containsValue(Object value) {
    return false;
  }

  @Override
  public V get(Object key) {
    int k = getHashCode((K) key) % array.length;
    if (array[k] == null ) {
      return  null;
    }



    return (V) array[k].search((Object) key).value;
  }

  @Override
  public V put(K key, V value) {




    int k = getHashCode(key) % array.length;
    if (array[k] == null) {
      mapSize ++;
      array[k] = new CustomList<CustomListNode<K,V>>();


    }
    if (array[k].search((Object) key) != null) {
     array[k].search((Object) key).value = value;
      return value;
    }

    array[k].add(new CustomListNode<K,V>(key, value));

    extend();


    return value;


  }

  @Override
  public V remove(Object key) {
    int k = getHashCode((K) key) % array.length;
    if (array[k] == null) {
      return null;
    }

    CustomListNode<?,?> n = array[k].search(key);
    array[k].remove(key);

    if(array[k].size() == 0) {
      array[k] = null;
      mapSize --;
    }

    return (V) n.value;
  }

  public void extend() {


    if (((float)mapSize / array.length) >= 0.75) {
      mapSize = 0;
      System.out.println("-----------------------");
      printDict();
      CustomList<CustomListNode<K,V>>[] arrayTmp = Arrays.copyOf(array,array.length);



      array= new CustomList[array.length * 2];

      for ( int i = 0; i < arrayTmp.length ; i++) {
        if (arrayTmp[i] != null) {
          for (CustomListNode<?,?> k : arrayTmp[i]) {
            this.put((K)k.key, (V) k.value);
          }
        }
      }



    }
  }


  @Override
  public void putAll(Map m) {

  }

  @Override
  public void clear() {

  }

  @Override
  public Set keySet() {
    return null;
  }

  @Override
  public Collection values() {
    return null;
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return null;
  }

  public int getHashCode(K k) {
    return Objects.hashCode(k);
  }


}

class CustomList<K> implements List<K>, Iterable<K>{

  CustomListNode<?,?> first;
  CustomListNode<?,?> last;
  int size;
  public CustomList() {


    first = new  CustomListNode<>(null,null);


    last = first.next;
    size = 0;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean contains(Object o) {
    return false;
  }

  @Override
  public Iterator<K> iterator() {
    return new ListItr() ;
  }

  @Override
  public Object[] toArray() {
    return new Object[0];
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return null;
  }

  @Override
  public boolean add(K k) {

    CustomListNode tmp = (CustomListNode<?,?>) k;

    if (last == null) {
      last = tmp;
      last.previous = first;
      first.next = last;
      size++;
      return true;
    }

    last.next = tmp;
    tmp.previous = last;
    last = tmp;
   size++;
    return true;
  }

  @Override
  public boolean remove(Object o) {
    CustomListNode head = first.next;

    while(head != null) {

      if(head.key.equals(o)) {
        if(head.equals(last)) {
          if (size == 1) {
            last = null;
            size--;
            return true;
          }
          CustomListNode tmp = last;
          last.previous.next = null;
          last = tmp.previous;
          size--;
          return true;
        }

        CustomListNode tmp = head.previous;
        tmp.next = head.next;
        head.next.previous = tmp;
        size--;
        return true;
      }

      head = head.next;

    }
    return false;
  }

  public CustomListNode search(Object o) {
    CustomListNode head = first.next;
    while(head != null) {

      if(head.key.equals(o)) {

        return head;
      }

      head = head.next;

    }
    return null;
  }

  @Override
  public boolean containsAll(Collection<?> c) {

    return false;
  }

  @Override
  public boolean addAll(Collection<? extends K> c) {
    return false;
  }

  @Override
  public boolean addAll(int index, Collection<? extends K> c) {
    return false;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return false;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return false;
  }

  @Override
  public void clear() {

  }

  @Override
  public K get(int index) {
    return null;
  }

  @Override
  public K set(int index, K element) {
    return null;
  }

  @Override
  public void add(int index, K element) {

  }

  @Override
  public K remove(int index) {
    return null;
  }

  @Override
  public int indexOf(Object o) {
    return 0;
  }

  @Override
  public int lastIndexOf(Object o) {
    return 0;
  }

  @Override
  public ListIterator<K> listIterator() {
    return new ListItr() ;
  }



  @Override
  public ListIterator<K> listIterator(int index) {
    return new ListItr();
  }

  @Override
  public List<K> subList(int fromIndex, int toIndex) {
    return null;
  }

  private class ListItr<K> implements ListIterator<K> {
    int index ;
    CustomListNode<?,?> n;

    public ListItr() {
      index = 0;
      n = first;
    }

    @Override
    public boolean hasNext() {

      return n.next != null;
    }

    @Override
    public K next() {
      n = n.next;

      return (K)n;
    }

    @Override
    public boolean hasPrevious() {
      return false;
    }

    @Override
    public K previous() {
      return null;
    }

    @Override
    public int nextIndex() {
      return 0;
    }

    @Override
    public int previousIndex() {
      return 0;
    }

    @Override
    public void remove() {

    }

    @Override
    public void set(K k) {

    }

    @Override
    public void add(K k) {

    }
  }
}

class CustomListNode<A,V> {
  CustomListNode next;
  CustomListNode previous;
  public A key;
  public V value;

  public CustomListNode(A key, V value) {
    this.key = key;
    this.value = value;
  }




}

