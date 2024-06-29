package andrew.cmu.edu.abhineec;

import edu.colorado.nodes.ObjectNode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 * MerkleHellmanKnapsackCryptosystem implements the Merkle–Hellman knapsack cryptosystem
 * as defined @ https://en.wikipedia.org/wiki/Merkle%E2%80%93Hellman_knapsack_cryptosystem
 * It can be used to encrypt and decrypt text containing upto 80 characters and
 * is bounded by  the limitations of the algorithm itself
 ******************************************************************************/
public class MerkleHellmanKnapsackCryptosystem {
    private SinglyLinkedList w;
    private BigInteger sumW;
    private BigInteger q;
    private BigInteger r;
    private SinglyLinkedList b;
    private int lengthOfBinaryMessage;

    /**
     * Initialize an object of the MerkleHellmanKnapsackCryptosystem
     * @postcondition
     *   An object of MerkleHellmanKnapsackCryptosystem is initialized with w and b
     *   as empty SinglyLinkedList objects and sumW as 0
     **/
    public MerkleHellmanKnapsackCryptosystem(){
        w = new SinglyLinkedList();
        b = new SinglyLinkedList();
        sumW  = BigInteger.valueOf(0);
    }

    /**
     * Wrapper method to add a BigInteger object to the w list
     * @postcondition
     *   a BigInteger object is added to the w list
     **/
    /*
        Asymptotic Notations:
            Big-O : O(1)
            Big-Omega: Ω(1)
            Big-Theta: Θ(1)
     */
    public void addBigIntegerToW(BigInteger x){
        this.w.addAtEndNode(new ObjectNode(x, null));
        sumW = sumW.add(x);
    }

    /**
     * Wrapper method to get value at a position from b i.e.
     * the list of public keys
     * @precondition
     * index should be between 0 and the size of list b
     * @postcondition
     *   object remains unmodified, the BigInteger value at index position in list b is returned
     * @return
     *   a BigInteger value from b (public keys list) at position index
     **/
    /*
        Asymptotic Notations:
            Big-O : O(1)
            Big-Omega: Ω(1)
            Big-Theta: Θ(1)
     */
    public BigInteger getValueAtFromB(int index){
        return (BigInteger) b.getObjectAt(index);
    }

    /**
     * Method used to populate the list b i.e.
     * the list of public keys
     * @precondition
     * w list should contain a super  increasing sequence of BigIntegers
     * @postcondition
     *   list b is populated
     **/
    /*
        Asymptotic Notations:
            Big-O : O(n)
            Big-Omega: Ω(n)
            Big-Theta: Θ(n)
     */
    public void generatePublicKeys() throws Exception {
        if(this.w==null)
            throw new Exception("W is null");

        //choose random q > sumW
        int randomBtw2and9 = (int) (Math.random() * 7) +2;
        this.q = sumW.multiply(BigInteger.valueOf(randomBtw2and9));
        //find r as a coprime of q
        this.r = coprime(q);
        //Construct public key blocks as r * wi mod q
        w.reset();
        while(w.hasNext()){
            BigInteger wi = (BigInteger) ((ObjectNode)w.next()).getData();
            BigInteger bi = wi.multiply(r).mod(q);
            //add public key to end of list b
            b.addAtEndNode(new ObjectNode(bi, null));
        }
    }

    /**
     * Method used to encrypt a message using
     * Merkle–Hellman knapsack cryptosystem
     * @precondition
     * list w and b are  populated with appropriate values
     * @postcondition
     *   object remains as is; cipher text is returned
     * @return
     *  a string object containing the encrypted text
     **/
    /*
        Asymptotic Notations:
            Big-O : O(n)
            Big-Omega: Ω(n)
            Big-Theta: Θ(n)
     */
    public String encrypt(String message){
        StringBuilder encodedString = new StringBuilder();
        //convert the plain text to its binary value
        StringBuilder messageInBinary = new StringBuilder();
        for(int i=0; i<message.length(); i++){
            int asciiValue = message.charAt(i);
            //get the  binaryValue of  current char
            String binaryValue = Integer.toBinaryString(asciiValue);
            //Add trailing 0s if binary length is less than 8
            for(int x=binaryValue.length(); x<8; x++)
                binaryValue = "0" + binaryValue;
            messageInBinary.append(binaryValue);
        }
        //update variable lengthOfBinaryMessage to contain the length of messageInBinary
        // is used during decryption
        this.lengthOfBinaryMessage = messageInBinary.length();
        //Select each bi for which messageInBinary[i] is nonzero, compute (mi*bi) and add them together.
        //This represents the cipher text
        int index = 0;
        BigInteger cipherVal = BigInteger.valueOf(0);
        for(char binaryVal: messageInBinary.toString().toCharArray()){
            int tmp = binaryVal=='0'?0:1;
            BigInteger bi = (BigInteger) ((ObjectNode) b.getObjectAt(index++)).getData();
            BigInteger ctVal = bi.multiply(BigInteger.valueOf(tmp));
            cipherVal = cipherVal.add(ctVal);
        }
        //return cipher text as string
        encodedString.append(cipherVal);
        return encodedString.toString().trim();
    }

    /**
     * Method used to decrypt a message using
     * Merkle–Hellman knapsack cryptosystem
     * @precondition
     * cipherText  is actually an encrypted text and not a plain text
     * @postcondition
     *   object remains as is; plain text is returned
     * @return
     *  a string object containing the plain text
     **/
    /*
        Asymptotic Notations:
            Big-O : O(n)
            Big-Omega: Ω(n)
            Big-Theta: Θ(n)
     */
    public String decrypt(String cipherText){
        //calculate modular inverse of r modulo q
        BigInteger rModInvq = r.modInverse(q);
        BigInteger cipherValue = new BigInteger(cipherText);
        //calculate c_dash as c * rModInvq mod q
        BigInteger c_dash = cipherValue.multiply(rModInvq).mod(q);
        //Find the resulting list of indexes of the elements of W which sum to c_dash
        List<Integer> wiList = greedyDecompose(c_dash);
        StringBuilder messageInBinary = new StringBuilder();
        for(int i=0; i<b.countNodes(); i++){
            if(wiList.contains(i))
                messageInBinary.append("1");
            else
                messageInBinary.append("0");
        }

        StringBuilder decodedString = new StringBuilder();
        int i=0, j=8;
        while(i!=this.lengthOfBinaryMessage){
            int ascii = convertBinaryToAscii(messageInBinary.substring(i, j));
            decodedString.append((char) ascii);
            i=j;
            j+=8;
        }
        return decodedString.toString();
    }

    /**
     * Helper function to convert a binary string to its corresponding ascii character
     **/
    /*
        Asymptotic Notations:
            Big-O : O(n)
            Big-Omega: Ω(n)
            Big-Theta: Θ(n)
     */
    public int convertBinaryToAscii(String binary){
        int asciiValue = 0;
        int power =0;
        //for every position in binary with 1, raise two to the power of its index and sum them up
        for(int i=binary.length()-1; i>=0; i--){
            if(binary.charAt(i) == '1')
                asciiValue += Math.pow(2, power);
            power++;
        }
        return asciiValue;
    }

    /**
     * Helper function to get the coprime of a BigInteger
     **/
    /*
        Asymptotic Notations:
            Big-O : O(n)
            Big-Omega: Ω(n)
            Big-Theta: Θ(n)
     */
    private BigInteger coprime(BigInteger q){
        //coprime of a number falls in  range of (2, n/2)
        BigInteger i = q.divide(BigInteger.valueOf(2));
        while(i.compareTo(q)!=0){
            //if the gcd comes out to be 1 -> i and q are coprime
            if(gcd(i, q).compareTo(BigInteger.valueOf(1))==0)
                return i;
            i = i.add(BigInteger.valueOf(1));
        }
        return null;
    }

    /**
     * Helper function to recursively find out the gcd of two numbers
     * @reference
     *  This is a derivative work from my experience with a competitive programming
     *  portal named leetcode (https://leetcode.com/) (orignal Author unknown,
     *  was not copied or referenced during this assignment,
     *  but I learned it from there)
     **/
    /*
        Asymptotic Notations:
            Big-O : O(log n)
            Big-Omega: Ω(log n)
            Big-Theta: Θ(log n)
     */
    private BigInteger gcd(BigInteger a, BigInteger b){
        //Break condition: until b has been fully decomposed to 0
        if(b.compareTo(BigInteger.valueOf(0))==0)
            return a;
        return gcd(b, a.mod(b));
    }

    /**
     * Helper function to use greedy approach to find the positions in w
     * that sum upto c_dash
     **/
    /*
        Asymptotic Notations:
            Big-O : O(n)
            Big-Omega: Ω(n)
            Big-Theta: Θ(n)
     */
    private List<Integer> greedyDecompose(BigInteger c_dash){
        int tailIndex = this.lengthOfBinaryMessage-1;
        List<Integer> result = new ArrayList<Integer>();
        while(tailIndex>=0){
            BigInteger wi = (BigInteger) ((ObjectNode) w.getObjectAt(tailIndex)).getData();
            if(c_dash.compareTo(wi)>=0){
                result.add(tailIndex);
                c_dash = c_dash.subtract(wi);
                if(c_dash.compareTo(BigInteger.valueOf(0))==0)
                    return result;
            }
            tailIndex--;
        }
        return result;
    }

    /**
     * Main function demonstrating program execution
     **/
    public static void main(String[] a) throws Exception{
        System.out.println("Hello and welcome!\n");
        System.out.println("This assignment was submitted by:\nName: Abhineet Chaudhary\nAndrewId: abhineec\nCourse: 95-771 A Fall 2023\n\n");

        //Driver Code
        MerkleHellmanKnapsackCryptosystem  merkleHellmanKnapsackCryptosystem = new MerkleHellmanKnapsackCryptosystem();

        //Creates a super increasing sequence of BigIntegers of power of 7
        BigInteger prevSum = BigInteger.valueOf(1234567623);
        int count =0;
        while(count<641){
            merkleHellmanKnapsackCryptosystem.addBigIntegerToW(prevSum);
            //add a random value (~10^6) in w
            int x = (int) (Math.random() * 1000000) +1;
            BigInteger added = prevSum.add(BigInteger.valueOf(x));
            prevSum = prevSum.add(added);
            count++;
        }
        merkleHellmanKnapsackCryptosystem.generatePublicKeys();

        //User Interface
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter a string and I will encrypt it as single large integer.\n");
        String userInput = reader.readLine();
        //ignoring trailing whitespaces
        userInput = userInput.trim();
        System.out.println();
        System.out.println("Clear Text:"+userInput);
        System.out.println("Number of clear text bytes = "+userInput.length());
        String cipherText = merkleHellmanKnapsackCryptosystem.encrypt(userInput);
        System.out.println();
        System.out.println(userInput+" is encrypted as "+cipherText);
        System.out.println();
        System.out.println("Result of decryption: "+merkleHellmanKnapsackCryptosystem.decrypt(cipherText));
    }

}
