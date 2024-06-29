package andrew.cmu.edu.abhineec;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/******************************************************************************
 * MerkleTree builds a Merkle hash tree
 * as defined @ https://en.wikipedia.org/wiki/Merkle_tree
 ******************************************************************************/
public class MerkleTree {

    List<SinglyLinkedList> merkletree;
    int countNodes;

    /**
     * Initialize an object of the MerkleTree
     * @postcondition
     *   An object of MerkleTree is initialized with an empty List of SinglyLinkedList
     *   and countNodes=0
     **/
    public MerkleTree(){
        this.merkletree = new ArrayList<SinglyLinkedList>();
        this.countNodes = 0;
    }

    /**
     * Method to create the bottom two levels of the merkle tree (Base)
     * The bottom layer contains the data block (lines from the file) and the layer above that holds
     * the individual hashes of each data block
     * @param fileObject
     * Reference to the file that contains the data block
     * @postcondition
     *   First two levels of merkle tree have been initialized
     **/
    /*
        Asymptotic Notations:
            Big-O : O(n)
            Big-Omega: Ω(n)
            Big-Theta: Θ(n)
     */
    public void buildMerkleTreeBase(File fileObject) throws NoSuchAlgorithmException, FileNotFoundException {
        SinglyLinkedList base = new SinglyLinkedList();
        SinglyLinkedList baseHash = new SinglyLinkedList();
        String lastLine = "";

        Scanner myReader = new Scanner(fileObject);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            base.addAtEndNode(data);
            baseHash.addAtEndNode(h(data));
            countNodes++;
            lastLine = data;
        }
        myReader.close();

        if(countNodes%2!=0){
            base.addAtEndNode(lastLine);
            baseHash.addAtEndNode(h(lastLine));
            countNodes++;
        }
        this.merkletree.add(base);
        this.merkletree.add(baseHash);
    }

    /**
     * Method to create the bottom two levels of the merkle tree (Base)
     * The bottom layer contains the data block (lines from the file) and the layer above that holds
     * the individual hashes of each data block
     * @precondition
     * Merkle tree base (First 2 levels) should be initialized
     * countNodes holds the total number of nodes (or data blocks) in list
     * @postcondition
     *   Merkle tree is fully populated, with the last List of size 1
     *   holding the Merkle root of the data blocks
     * @return
     *  Merkle root of the datablocks
     **/
    /*
        Asymptotic Notations:
            Big-O : O(n^2)
            Big-Omega: Ω(n^2)
            Big-Theta: Θ(n^2)
     */
    public String buildMerkleTree() throws Exception{
        if(merkletree.isEmpty())
            throw new Exception("Merkle tree base has not been initialized");
        //Continue building the tree until root is found
        while(countNodes>1) {
            //head  holds the top-most list (level) in the Merkle tree
            SinglyLinkedList head = merkletree.get(merkletree.size()-1);
            SinglyLinkedList currentList = new SinglyLinkedList();
            String hash = "";
            /*For each alternate element in head
                get the string at this position and the position next to it
                calculate the hash
                add to list and decrement the countNodes by 1 (since we combined 2 nodes into 1 now)
             */
            for (int i = 0; i < head.countNodes; i += 2) {
                String firstString = (String) head.getObjectAt(i);
                String lastString = (String) head.getObjectAt(i + 1);
                hash = h(firstString + lastString);
                currentList.addAtEndNode(hash);
                countNodes --;
            }
            //Balance the list if the number of elements in current level are odd
            //Ignore countNodes=1 since it implies we have our Merkle root
            if(countNodes>1 && countNodes%2!=0){
                currentList.addAtEndNode(hash);
                countNodes++;
            }
            merkletree.add(currentList);
        }
        //return the data held in the last SinglyLinkedList's first position
        //which contains the Hash root for the data blocks
        return (String) merkletree.get(merkletree.size()-1).getObjectAt(0);
    }

    /**
     * Method to create the bottom two levels of the merkle tree (Base)
     * The bottom layer contains the data block (lines from the file) and the layer above that holds
     * the individual hashes of each data block
     * @postcondition
     *   MerkleTree object has been reinitialized to its base state
     **/
    /*
        Asymptotic Notations:
            Big-O : O(1)
            Big-Omega: Ω(1)
            Big-Theta: Θ(1)
     */
    public void reset(){
        this.merkletree = new ArrayList<SinglyLinkedList>();
        this.countNodes = 0;
    }

    /**
     * Method to calculate the hash of a given string,
     * uses SHA-256 that  outputs 256 bits long output
     * @reference
     * Copied as is from Project 1 Assignment description
     * @postcondition
     *   hash of the passed string is returned
     * @return
     *  SHA-256 hash of a string
     **/
    /*
        Asymptotic Notations:
            Big-O : O(n)
            Big-Omega: Ω(n)
            Big-Theta: Θ(n)
     */
    public static String h(String text) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash =
                digest.digest(text.getBytes(StandardCharsets.UTF_8));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i <= 31; i++) {
            byte b = hash[i];
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    /**
     * Helper method to print the current state of Merkle tree list
     * in console. Can be used to verify the tree being constructed.
     **/
    /*
        Asymptotic Notations:
            Big-O : O(n)
            Big-Omega: Ω(n)
            Big-Theta: Θ(n)
     */
    public void printMerkleTree(){
        for(SinglyLinkedList curr : merkletree){
            System.out.println(curr.toString());
        }
    }

    /**
     * Helper method to dump the current state of Merkle tree list
     * in a file called log.txt in user's  current directory.
     * Can be used to verify the tree being constructed in cases where the
     * tree is too huge to fit in console.
     **/
    /*
        Asymptotic Notations:
            Big-O : O(n)
            Big-Omega: Ω(n)
            Big-Theta: Θ(n)
     */
    public void printMerkleTreeToFile() throws IOException {
        FileWriter myWriter = new FileWriter("log.txt");
        for(SinglyLinkedList curr : merkletree){
            myWriter.write(curr.toString());
            myWriter.write("\n\n");
        }
        myWriter.close();
    }

    /**
     * Main function demonstrating program execution
     **/
    public static void main(String[] args) throws Exception {
        System.out.println("Hello and welcome!\n");
        System.out.println("This assignment was submitted by:\nName: Abhineet Chaudhary\nAndrewId: abhineec\nCourse: 95-771 A Fall 2023\n\n");

        String userDirectory = System.getProperty("user.dir");
        System.out.println("Please ensure your project directory is correct. It should be the absolute path till ...Project1/MerkleTree-Project \nProject Directory: " + userDirectory + "\n");

        MerkleTree myMerkleTreeObj = new MerkleTree();
        String fileName2 = "CrimeLatLonXY1990_Size2.csv";
        File fileObj = new File(userDirectory+ "/resources/" + fileName2);
        System.out.println("Calculating merkle root for "+ fileName2 + "...");
        myMerkleTreeObj.buildMerkleTreeBase(fileObj);
        String hashRoot2 = myMerkleTreeObj.buildMerkleTree();
        System.out.println("Merkle Root for "+ fileName2 + " = " + hashRoot2+"\n");
        myMerkleTreeObj.reset();

        String fileName3 = "CrimeLatLonXY1990_Size3.csv";
        fileObj = new File(userDirectory+ "/resources/" + fileName3);
        System.out.println("Calculating merkle root for "+ fileName3 + "...");
        myMerkleTreeObj.buildMerkleTreeBase(fileObj);
        String hashRoot3 = myMerkleTreeObj.buildMerkleTree();
        System.out.println("Merkle Root for "+ fileName3 + " = " + hashRoot3+"\n");
        myMerkleTreeObj.reset();

        String fileName1 = "CrimeLatLonXY.csv";
        fileObj = new File(userDirectory+ "/resources/" + fileName1);
        System.out.println("Calculating merkle root for "+ fileName1 + "...");
        myMerkleTreeObj.buildMerkleTreeBase(fileObj);
        String hashRoot1 = myMerkleTreeObj.buildMerkleTree();
        System.out.println("Merkle Root for "+ fileName1 + " = " + hashRoot1+"\n");
        myMerkleTreeObj.reset();

        String TARGET_ROOT = "A5A74A770E0C3922362202DAD62A97655F8652064CCCBE7D3EA2B588C7E07B58";

        if(hashRoot1.equals(TARGET_ROOT))
            System.out.println("It can be interpreted that " + fileName1 + " has the target merkle root of " + hashRoot1);
        else if(hashRoot2.equals(TARGET_ROOT))
            System.out.println("It can be interpreted that " + fileName2 + " has the target merkle root of " + hashRoot2);
        else if(hashRoot3.equals(TARGET_ROOT))
            System.out.println("It can be interpreted that " + fileName3 + " has the target merkle root of " + hashRoot3);
    }

}
