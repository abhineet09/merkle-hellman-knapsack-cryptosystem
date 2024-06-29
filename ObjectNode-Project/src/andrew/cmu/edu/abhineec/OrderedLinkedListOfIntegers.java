package andrew.cmu.edu.abhineec;

/*
 * Submission Details:
 *      Name - Abhineet Chaudhary
 *      andrewId = abhineec
 *      Course - 95-771 Data Structure and Algorithms for Information Processing
 *      Section - A
 * */

import edu.colorado.nodes.ObjectNode;

/******************************************************************************
 * OrderedLinkedListOfIntegers extends the SinglyLinkedList class to maintain
 * an ordered list of integers
 ******************************************************************************/
public class OrderedLinkedListOfIntegers extends SinglyLinkedList {

    /**
     * Adds an integer such that the ordering of elements (ascending) is preserved
     * @param num
     *   the integer to be inserted in list
     * @postcondition
     *   The element has been added to its appropriate position in list
     **/
    /*
        Asymptotic Notations:
            Big-O : O(n)
            Big-Omega: Ω(n)
            Big-Theta: Θ(n)
     */
    public void sortedAdd(int num){
        ObjectNode objectToAdd = new ObjectNode(num, null);
        if(countNodes==0 || (countNodes==1 && num<=Integer.parseInt(String.valueOf(head.getData())))){
            addAtFrontNode(objectToAdd);
        }
        else if(countNodes==1 && num>Integer.parseInt(String.valueOf(head.getData()))){
            addAtEndNode(objectToAdd);
        }
        else{
            ObjectNode localIterator = super.head;
            ObjectNode localIteratorTail = super.head;
            while(localIterator!=null &&  num>Integer.parseInt(String.valueOf(localIterator.getData()))){
                localIteratorTail =  localIterator;
                localIterator = localIterator.getLink();
            }
            if(localIterator==null)
                addAtEndNode(objectToAdd);
            else if(localIterator == localIteratorTail)
                addAtFrontNode(objectToAdd);
            else{
                objectToAdd.setLink(localIterator);
                localIteratorTail.setLink(objectToAdd);
                countNodes++;
            }

        }
    }

    /**
     * Adds an integer such that the ordering of elements (ascending) is preserved
     * @param list1
     *   the first list to be merged
     * @param list2
     *   the first list to be merged
     * @return
     *    a new OrderedLinkedListOfIntegers containing elements from both list in ascending order
     * @postcondition
     *   list1 and list2 are preserved; a new ordered merged list's head reference is returned
     **/
    /*
        Asymptotic Notations:
            Big-O : O(n)   [Considering n represents the size of list that is larger]
            Big-Omega: Ω(n)
            Big-Theta: Θ(n)
     */
    public static OrderedLinkedListOfIntegers merge(OrderedLinkedListOfIntegers list1, OrderedLinkedListOfIntegers list2){
        ObjectNode p1=list1.head, p2=list2.head;
        OrderedLinkedListOfIntegers mergedList = new OrderedLinkedListOfIntegers();
        //Loop unless each element in both list are traversed
        while(p1!=null || p2!=null){
            //If p1 is empty (list1 has fully been merged) keep adding elements in list 2 to mergedList
            if(p1==null){
                mergedList.addAtEndNode(p2.getData());
                p2 = p2.getLink();
            }
            //If p2 is empty (list2 has fully been merged) keep adding elements in list 1 to mergedList
            else if (p2==null){
                mergedList.addAtEndNode(p1.getData());
                p1 = p1.getLink();
            }
            //Add the lesser valued element between current position of p1 and p2
            //move the pointer to its next for the lesser valued one
            else if(Integer.parseInt(String.valueOf(p1.getData()))<=Integer.parseInt(String.valueOf(p2.getData()))){
                mergedList.addAtEndNode(p1.getData());
                p1 = p1.getLink();
            }
            else{
                mergedList.addAtEndNode(p2.getData());
                p2 = p2.getLink();
            }
        }
        return mergedList;
    }

    /**
     * Main function demonstrating program execution
     **/
    public static void main(String[] a){
        System.out.println("Hello and welcome!\n");
        System.out.println("This assignment was submitted by:\nName: Abhineet Chaudhary\nAndrewId: abhineec\nCourse: 95-771 A Fall 2023\n\n");

        OrderedLinkedListOfIntegers orderedLinkedListOfIntegers1 = new OrderedLinkedListOfIntegers();
        System.out.print("First List Numbers added:");
        for(int i=0; i<20; i++){
            int random = (int) (Math.random() * 100) + 1;
            System.out.print(random+", ");
            orderedLinkedListOfIntegers1.sortedAdd(random);
        }
        System.out.println();
        System.out.println(orderedLinkedListOfIntegers1.toString());
        System.out.println(orderedLinkedListOfIntegers1.countNodes());

        OrderedLinkedListOfIntegers orderedLinkedListOfIntegers2 = new OrderedLinkedListOfIntegers();
        System.out.println();
        System.out.print("Second List Numbers added:");
        for(int i=0; i<20; i++){
            int random = (int) (Math.random() * 100) + 1;
            System.out.print(random+", ");
            orderedLinkedListOfIntegers2.sortedAdd(random);
        }
        System.out.println();
        System.out.println(orderedLinkedListOfIntegers2.toString());
        System.out.println(orderedLinkedListOfIntegers2.countNodes());

        OrderedLinkedListOfIntegers mergedList = merge(orderedLinkedListOfIntegers1, orderedLinkedListOfIntegers2);
        System.out.println();
        System.out.println(mergedList.toString());
        System.out.println(mergedList.countNodes());
    }

}
