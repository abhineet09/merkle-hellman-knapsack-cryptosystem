/*
 * Submission Details:
 *      Name - Abhineet Chaudhary
 *      andrewId = abhineec
 *      Course - 95-771 Data Structure and Algorithms for Information Processing
 *      Section - A
 * */

package andrew.cmu.edu.abhineec;

import edu.colorado.nodes.ObjectNode;

/******************************************************************************
 * SinglyLinkedList extends the ObjectNode class to provide additional features like
 * a head pointer reference
 * a tail pointer reference
 * an iterator pointer which is used to traverse through the list and
 * a countNode variable that keeps track of total number of nodes in the list at any given point
 ******************************************************************************/
public class SinglyLinkedList extends Object{
    protected ObjectNode head;
    protected ObjectNode tail;
    protected ObjectNode iterator;
    protected int countNodes;

    /**
     * Initialize an empty SinglyLinkedList
     * @postcondition
     *   An empty SinglyLinkedList object is initialized
     **/
    public SinglyLinkedList(){
        this.head  = null;
        this.tail = null;
        this.countNodes = 0;
    }

    /**
     * Reset the iterator pointer to refer head of the list
     * @postcondition
     *   Iterator now points to the head of the list
     *   For an empty list it would be null
     **/
    /*
        Asymptotic Notations:
            Big-O : O(1)
            Big-Omega: Ω(1)
            Big-Theta: Θ(1)
     */
    public void reset(){
        iterator =  head;
    }
    /**
     * Returns the data object for the ObjectNode being referred by the iterator at any given point
     * @postcondition
     *   Object data for current iterator reference has been sent
     *   For an empty list it returns a null object
     **/
    /*
        Asymptotic Notations:
            Big-O : O(1)
            Big-Omega: Ω(1)
            Big-Theta: Θ(1)
     */
    public Object next(){
        if(iterator==null)
            return null;
        Object obj =  iterator.getData();
        iterator = iterator.getLink();
        return obj;
    }
    /**
     * Returns true if the ObjectNode being referred by the iterator has a link that points to another ObjectNode
     * @postcondition
     *   Has returned true if there are elements remaining to be traversed
     *   For an empty list it returns a null object
     **/
    /*
        Asymptotic Notations:
            Big-O : O(1)
            Big-Omega: Ω(1)
            Big-Theta: Θ(1)
     */
    public boolean hasNext(){
        return iterator != null;
    }
    public void addAtFrontNode(Object c){
        ObjectNode obj = new ObjectNode(c, head);
        head = obj;
        countNodes++;
        if(tail==null)
            tail=head;
    }
    /**
     * Inserts a new ObjectNode at the end of the list
     * @postcondition
     *   A new ObjectNode has been added to the list
     **/
    /*
        Asymptotic Notations:
            Big-O : O(1)
            Big-Omega: Ω(1)
            Big-Theta: Θ(1)
     */
    public void addAtEndNode(Object c){
        if(tail==null){
            addAtFrontNode(c);
        }
        else{
            ObjectNode obj = new ObjectNode(c, null);
            tail.setLink(obj);
            tail = obj;
            countNodes++;
        }
    }
    /**
     * Returns value of the countNodes representing the number of ObjectNodes in list at any  point in time
     **/
    /*
        Asymptotic Notations:
            Big-O : O(1)
            Big-Omega: Ω(1)
            Big-Theta: Θ(1)
     */
    public int countNodes(){
        return countNodes;
    }

    /**
     * Returns value of ObjectNode at any given position int lis
     * @param i
     *      represents the index of data to be fetched in the list
     * @precondition
     *     i should not be less than zero
     *     i should be less than the value of countNodes
     **/
    /*
        Asymptotic Notations:
            Big-O : O(n)
            Big-Omega: Ω(n)
            Big-Theta: Θ(n)
     */
    public Object getObjectAt(int i){
        //Not utilizing the class variable iterator, since this function may be called within another loop!
        ObjectNode localIterator =  head;
        while(localIterator!=null && i>0){
            localIterator = localIterator.getLink();
            i--;
        }
        if(localIterator==null)
            return null;
        else
            return localIterator.getData();
    }

    /**
     * Returns value of ObjectNode at the last position in list
     * @precondition
     *   tail should not be null and must point to the last element in the list
     **/
    /*
        Asymptotic Notations:
            Big-O : O(1)
            Big-Omega: Ω(1)
            Big-Theta: Θ(1)
     */
    public Object getLast(){
        return tail.getData();
    }

    /**
     * Returns a String containing all elements in a list seperated by a comma
     * @precondition
     *   head should not be null
     **/
    /*
        Asymptotic Notations:
            Big-O : O(n)
            Big-Omega: Ω(n)
            Big-Theta: Θ(n)
     */
    @Override
    public  String toString(){
        //Not utilizing the class variable iterator, since this function may be called within another loop!
        ObjectNode localIterator =  head;
        StringBuilder result = new StringBuilder();
        while(localIterator!=null){
            result.append(localIterator.getData());
            result.append(", ");
            localIterator = localIterator.getLink();
        }
        return result.toString();
    }

    /**
     * Main function demonstrating program execution
     **/
    public static void main(String[] a){
        System.out.println("Hello and welcome!\n");
        System.out.println("This assignment was submitted by:\nName: Abhineet Chaudhary\nAndrewId: abhineec\nCourse: 95-771 A Fall 2023\n\n");

        //Creating a list of alphabetic characters from a-z
        SinglyLinkedList s = new SinglyLinkedList();
        ObjectNode h = new ObjectNode((char) 97, null);
        s.addAtFrontNode(h);
        for(int i=98; i<=122; i++){
            s.addAtEndNode(new ObjectNode((char) i, null));
        }

        System.out.println(s.toString());
        s.reset();
        while(s.hasNext()){
            System.out.print(s.next());
        }
        System.out.println();
    }
}
