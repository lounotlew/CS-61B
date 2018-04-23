package gitlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import static java.nio.file.StandardCopyOption.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/** Assorted serialization utilities.
 *  @author Woo Sik (Lewis) Kim and Alex Walczak (Partner: cs61b-abo)
 */
public class SerUtils {

    /** Serialize the current COMMITTREE (a HashMap) for this gitlet
     *  version-control system into a "commitTreeFile.ser" file in the
     *  .gitlet directory. */
    static void saveCommitTree(HashMap<String, Commit> commitTree) {
        if (commitTree == null) {
            return;
        }
        try {
            File commitTreeFile = new File(".gitlet/commitTreeFile.ser");
            FileOutputStream fileOut = new FileOutputStream(commitTreeFile);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(commitTree);

        } catch (IOException e) {
            System.out.println("IOException while saving the Commit Tree.");
            e.printStackTrace();
        }
    }

    /** Return the current commit tree (a HashMap) for this gitlet
     *  version-control system from loading it from the
     *  "commitTreeFile.ser" file in the .gitlet directory. */
    @SuppressWarnings("unchecked")
    static HashMap<String, Commit> loadCommitTree() {
        HashMap<String, Commit> myCommitTree = null;
        File commitTreeFile = new File(".gitlet/commitTreeFile.ser");

        if (commitTreeFile.exists()) {
            try {
                FileInputStream fileIn = new FileInputStream(commitTreeFile);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                myCommitTree = (HashMap<String, Commit>) objectIn.readObject();

            } catch (IOException e) {
                System.out.println("IOException while loading "
                    + "the commitTreeFile.ser file.");

            } catch (ClassNotFoundException e) {
                System.out.println("ClassNotFoundException while loading "
                    + "the commitTreeFile.ser file.");
            }
        }
        return myCommitTree;
    }

    /** Save the current commit CURRCOMMIT into a .ser file. */
    static void saveCurrentCommit(Commit currCommit) {
        if (currCommit == null) {
            return;
        }
        try {
            File currentCommit = new File(".gitlet/currentCommit.ser");
            FileOutputStream fileOut = new FileOutputStream(currentCommit);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(currCommit);

        } catch (IOException e) {
            System.out.println("IOException while saving current commit.");
        }
    }

    /** Return the current commit from the "currentCommit.ser" file. */
    @SuppressWarnings("unchecked")
    static Commit loadCurrentCommit() {
        Commit currCommit = null;
        File currentCommit = new File(".gitlet/currentCommit.ser");

        if (currentCommit.exists()) {
            try {
                FileInputStream fileIn = new FileInputStream(currentCommit);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                currCommit = (Commit) objectIn.readObject();

            } catch (IOException e) {
                System.out.println("IOException while loading "
                    + "the currentSha1.ser file.");

            } catch (ClassNotFoundException e) {
                System.out.println("ClassNotFoundException while loading "
                    + "the currentSha1.ser file.");
            }
        }
        return currCommit;
    }

    /** Serialize the SHA-1 hashcode HASHCODE of the current Commit. */
    static void saveCurrentSha1(String hashcode) {
        if (hashcode == null) {
            return;
        }
        try {
            File currentSha1 = new File(".gitlet/currentSha1.ser");
            FileOutputStream fileOut = new FileOutputStream(currentSha1);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(hashcode);

        } catch (IOException e) {
            System.out.println("Error: IOException while saving the current"
                + " SHA-1 hashcode.");
        }
    }

    /** Return the SHA-1 hashcode of the current commit after loading
     *  it from the "currentSha1.ser" file from the .gitlet directory. */
    @SuppressWarnings("unchecked")
    static String loadCurrentSha1() {
        String currHash = null;
        File currentSha1 = new File(".gitlet/currentSha1.ser");

        if (currentSha1.exists()) {
            try {
                FileInputStream fileIn = new FileInputStream(currentSha1);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                currHash = (String) objectIn.readObject();

            } catch (IOException e) {
                System.out.println("IOException while loading "
                    + "the currentSha1.ser file.");

            } catch (ClassNotFoundException e) {
                System.out.println("ClassNotFoundException while loading "
                    + "the currentSha1.ser file.");
            }
        }
        return currHash;
    }

    /** Serialize the list of the names of files that have been added to the
     *  staging area, STAGEDFILES, in order to track them. */
    static void saveStagedFiles(ArrayList<String> stagedFiles) {
        if (stagedFiles == null) {
            return;
        }
        try {
            File trackedFiles = new File(".gitlet/trackedFiles.ser");
            FileOutputStream fileOut = new FileOutputStream(trackedFiles);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(stagedFiles);

        } catch (IOException e) {
            System.out.println("IOException while saving tracked files.");
        }
    }

    /** Return the list of the names of files that are being tracked. */
    @SuppressWarnings("unchecked")
    static ArrayList<String> loadStagedFiles() {
        ArrayList<String> stagedFiles = null;
        File trackedFiles = new File(".gitlet/trackedFiles.ser");

        if (trackedFiles.exists()) {
            try {
                FileInputStream fileIn = new FileInputStream(trackedFiles);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                stagedFiles = (ArrayList<String>) objectIn.readObject();

            } catch (IOException e) {
                System.out.println("IOException while loading "
                    + "the trackedFiles.ser file.");

            } catch (ClassNotFoundException e) {
                System.out.println("ClassNotFoundException while loading "
                    + "the trackedFiles.ser file.");
            }
        }
        return stagedFiles;
    }

    /** Serialize the list of the names of files that are to be removed from
     *  the next commit, TOREMOVE. */
    static void saveRemovedFiles(ArrayList<String> toRemove) {
        if (toRemove == null) {
            return;
        }
        try {
            File removedFiles = new File(".gitlet/removedFiles.ser");
            FileOutputStream fileOut = new FileOutputStream(removedFiles);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(toRemove);

        } catch (IOException e) {
            System.out.println("IOException while saving removed files.");
        }
    }

    /** Return the list of the names of files that are to be removed. */
    @SuppressWarnings("unchecked")
    static ArrayList<String> loadRemovedFiles() {
        ArrayList<String> toRemove = null;
        File removedFiles = new File(".gitlet/removedFiles.ser");
        
        if (removedFiles.exists()) {
            try {
                FileInputStream fileIn = new FileInputStream(removedFiles);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                toRemove = (ArrayList<String>) objectIn.readObject();

            } catch (IOException e) {
                System.out.println("IOException while loading "
                    + "the removedFiles.ser file.");

            } catch (ClassNotFoundException e) {
                System.out.println("ClassNotFoundException while loading "
                    + "the removedFiles.ser file.");
            }
        }
        return toRemove;
    }

}
