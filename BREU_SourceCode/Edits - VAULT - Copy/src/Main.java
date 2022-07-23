
import workflowGen.randomizeGen;
import workflowGen.task;
import workflowGen.workflow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Justin Gazsi
 */
public class Main {
    static ArrayList<Transaction> genesisBlockTXs = new ArrayList<Transaction>();

    public static ArrayList<Node> Nodes = new ArrayList<Node>();            //Network of Nodes
    public static Block genesisBlock;// = new Block(new Transaction(-1), "0", 1);  //Genesis Block
    public static Quorum quorum;                             //Class to generate Quorum
    public static ArrayList<Boolean> genesisQuorum = new ArrayList<Boolean>();
    private final int NUM_NODES = 1000; //Number of nodes in the network
    private final int NUM_BLOCKS = 5; //Number of blocks/transactions/workflow tasks
    private final int QUORUM_SIZE = 10;
    private final double QUORUM_THRESHOLD = .8;


    //MAIN DRIVER
    public static void main(String[] args) throws InterruptedException, IOException {


        ArrayList<String> dummyProvenanceData = new ArrayList<>();
        for(int i = 0; i< 5; i++){
            dummyProvenanceData.add("0");
        }

        //Need initial genesis block
        genesisBlockTXs.add(new Transaction(-1, dummyProvenanceData));
        genesisBlock = new Block(genesisBlockTXs, "0", 1, genesisQuorum);

        //Run Experiments
        Main main = new Main();
        main.scalability();
    }

    //MINE: Scalability Experiment
    private void scalability(){


        //Create Nodes
        for (int i = 0; i < this.NUM_NODES; i++) {
            Nodes.add(new Node());
        }
        connectNetwork();
        printNetworkConnections();

        //Number of Blocks to create for tests
        //create workflows
        randomizeGen randomizeGen = new randomizeGen();
        ArrayList<workflow> workflows = randomizeGen.getWorkflows();

        for (int i = 0; i<workflows.size(); i++){
            ArrayList<task> workflow = workflows.get(i).getWorkflow();
            for(int j = 0; j < workflow.size(); j++) {
                ArrayList<String> provenanceData = workflow.get(j).toProvenanceData();

                long start = System.currentTimeMillis();
                this.quorum = new Quorum(this.QUORUM_SIZE);          //1.Create Quroum
                long QCreation = System.currentTimeMillis();
                long qDuration = (QCreation - start);  //divide by 1000000 to get milliseconds.
                //Print 1
                System.out.println("Begin---------------------");
                System.out.print("Quorum creation duration: " + qDuration);
                //broadcast #tps for #seconds

                long broadcastStart = System.currentTimeMillis();

                Nodes.get(0).broadcastTransaction(Nodes.get(0).createTransaction(provenanceData));

                long broadcastEnd = System.currentTimeMillis();
                long bDuration = (broadcastEnd - broadcastStart);
                //Print 2
                System.out.println(", broadcast transactions duration: " + bDuration);

                long validationStart = System.currentTimeMillis();

                for (Node node : Nodes) {  //3. Validate Transactions

                    node.validateBlock();
                }
                long validationEnd = System.currentTimeMillis();
                long vDuration = (validationEnd - validationStart);
                //Print 3
                System.out.println(", validation duration: " + vDuration);
                System.out.println("Middle---------------------");

                //Propse block and append all Nodes' ledgers
                long blockStart = System.currentTimeMillis();

                System.out.println(quorum.getNODES().size());

                quorum.getNODES().get(0).proposeBlock(this.QUORUM_THRESHOLD);  //4. Broadcast Block and propogate ledgers
                long blockEnd = System.currentTimeMillis();
                long blockDuration = (blockEnd - blockStart);
                //Print 4
                System.out.print(", Add block duration: " + blockDuration);

                //total time
                long endTime = System.currentTimeMillis();
                long totalTime = (endTime - start);
                //Print 5
                System.out.println(", total time: " + totalTime);
                System.out.println("END---------------------");
                System.out.println();
            }
            //System.out.println();
        }

    }

    static void connectNetwork() {
        Random rand = new Random();

        for (Node node : Nodes) {
            int count = rand.nextInt(2) + 9;  //Random connection to peers
            //int count = 10;  //Random connection to peers
            //System.out.println(count);

            //While # of peers is less than desired size, get a random peer to connect to
            while (node.getPeers().size() < count) {
                Node peer = Nodes.get(rand.nextInt(Nodes.size()));

                //Peer must not be in list already and peer must not equal itself, or must try to get new peer
                while (node.getPeers().contains(peer) || (node.getNodeID() == peer.getNodeID())) {
                    peer = Nodes.get(rand.nextInt(Nodes.size()));
                }
                //Connect to listen to peers
                node.addPeer(peer);
                //peer.addPeer(node);  //for two-way connection
            }
            //Uncomment below to see full history of peer connections as they are created
            //printNetworkConnections();
        }
    }

    //Function to print peer connections
    static void printNetworkConnections() {
        int min = 10, max = 0, sum = 0, avg;
        for (int i = 0; i < Nodes.size(); i++) {
            sum += Nodes.get(i).getPeers().size();
            if (Nodes.get(i).getPeers().size() < min) {
                min = Nodes.get(i).getPeers().size();
            }
            if (Nodes.get(i).getPeers().size() > max) {
                max = Nodes.get(i).getPeers().size();
            }
        }

        avg = sum / Nodes.size();

        System.out.println("");
        System.out.println("Peer Connection List");
        System.out.println("CONNECTIONS: Min: " + min + " Max: " + max + " Avg: " + avg);
        System.out.println("--------------------");

//        for (Node n : Nodes) {
//            System.out.print("NodeID: " + n.getNodeID() + " - Peers: ");
//            for (int i = 0; i < n.getPeers().size(); i++) {
//                System.out.print(n.getPeers().get(i).getNodeID() + " ");
//            }
//            System.out.println();
//        }
        System.out.println();
    }


}
