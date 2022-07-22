import java.util.ArrayList;
import java.util.Random;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PID# 6224488
 */

//Mine
public class Transaction {
    private String wID;
    private String  pTID;
    private String pWID;

    private int uID;
    private String tID;
    private String data;
    private long timeStamp;




    //Constructor
    public Transaction(int uID, ArrayList<String> provenanceData) {
        this.tID = createtID();
        this.timeStamp = new Date().getTime();
        this.uID = uID;
        this.data = ("TxDataStub - uID: ").concat(String.valueOf(uID)
                .concat(" / tID: ").concat(tID).concat(" / Time: ").concat(String.valueOf(timeStamp)));


        this.wID = provenanceData.get(0);
        this.pTID = provenanceData.get(1);
        this.pWID = provenanceData.get(2);
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String gettID() {
        return tID;
    }

    public void settID(String tID) {
        this.tID = tID;
    }

    public int getuID() {
        return uID;
    }

    public void setuID(int uID) {
        this.uID = uID;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


    public static String createtID() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 20;
        Random random = new Random();

        //Create random string 20 char long for Tx ID
        String tID = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        //System.out.println("Test tID output: " + tID);
        return tID;
    }
}


//Original

///**
// *
// * @author PID# 6224488
// */
//public class Transaction {
//
//    private String tID;
//    private int uID;
//    private String data;
//    private long timeStamp;
//
//    //Constructor
//    public Transaction(int uID) {
//        this.tID = createtID();
//        this.timeStamp = new Date().getTime();
//        this.uID = uID;
//        this.data = ("TxDataStub - uID: ").concat(String.valueOf(uID)
//                .concat(" / tID: ").concat(tID).concat(" / Time: ").concat(String.valueOf(timeStamp)));
//    }
//
//
//    public String getData() {
//        return data;
//    }
//
//    public void setData(String data) {
//        this.data = data;
//    }
//
//    public String gettID() {
//        return tID;
//    }
//
//    public void settID(String tID) {
//        this.tID = tID;
//    }
//
//    public int getuID() {
//        return uID;
//    }
//
//    public void setuID(int uID) {
//        this.uID = uID;
//    }
//
//    public long getTimeStamp() {
//        return timeStamp;
//    }
//
//    public void setTimeStamp(long timeStamp) {
//        this.timeStamp = timeStamp;
//    }
//
//    public static String createtID() {
//        int leftLimit = 48; // numeral '0'
//        int rightLimit = 122; // letter 'z'
//        int targetStringLength = 20;
//        Random random = new Random();
//
//        //Create random string 20 char long for Tx ID
//        String tID = random.ints(leftLimit, rightLimit + 1)
//                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
//                .limit(targetStringLength)
//                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
//                .toString();
//
//        //System.out.println("Test tID output: " + tID);
//        return tID;
//    }
//}
