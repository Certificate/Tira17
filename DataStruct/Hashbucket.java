/*
  Data Structures 2017
  Valtteri Vuori
  415642

  This class represents the bucket in which each item is stored into. It can point to the next item, which is
  used in the search of the correct item in a given slot. Kinda like a LinkedList.
 */
package DataStruct;
class Hashbucket {

    // MARK: Attributes

    int key;
    int value;
    Hashbucket next;


    // MARK: Constructor

    Hashbucket(int n, int val) {
        key = n;
        value = val;
        next = null;
    }
}