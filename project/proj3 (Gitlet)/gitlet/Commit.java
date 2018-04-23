package gitlet;

import java.io.Serializable;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/** The Commit class where an instance of Commit represents a specific commit
 *  made in the current directory.
 *  @author Woo Sik (Lewis) Kim and Alex Walczak (Partner: cs61b-abo)
 */
public class Commit implements Serializable {

    /** A new Commit whose message is set to MESSAGE, date set to DATE,
     *  and has a pointer to its parent commit through PREVIOUSSHA1. */
    public Commit(String message, Date date, String previousSha1) {
        _commitMessage = message;
        makeDateFormat(date);
        _prevSha1 = previousSha1;
    }

    /** Return the message associated with this Branch. */
    String getMessage() {
        return _commitMessage;
    }

    /** Create the correct date format of DATE for this gitlet version-control
     *  system (Month-Day-Year Time). */
    void makeDateFormat(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        _commitDate = dateFormat.format(date);
    }

    /** Return the date when this Branch was created, in the format
     *  Month-Day-Year Time. */
    String getDate() {
        return _commitDate;
    }

    /** Return the SHA-1 hashcode of the preceding commit. */
    String getPrevSha1() {
        return _prevSha1;
    }

    /** Track the file with the SHA-1 hashcode FILEID and its Blob BLOB. */
    void addFile(String fileID, Blob blob) {
        _trackedFiles.put(fileID, blob);
    }

    /** Return true iff the file associated with FILESHAID is tracked
     *  by this commit. */
    boolean containsFile(String fileShaID) {
        return _trackedFiles.containsKey(fileShaID);
    }

    /** Return the files tracked by this commit. */
    HashMap<String, Blob> getTrackedFiles() {
        return _trackedFiles;
    }

    /** Return true iff if this Commit is the master Branch (initial commit). */
    boolean isMasterBranch() {
        return _prevSha1.equals("No Previous Commits");
    }

    /** Message associated with this commit. */
    private String _commitMessage;
    /** The date on when this branch was created. */
    private String _commitDate;
    /** The SHA-1 hashcode of the previous commit. The initial commit has its
     *  _prevSha1 set to "No Previous Commits". */
    private String _prevSha1;
    /** Maps the SHA-1 IDs of all the files tracked by this commit to
     *  the files' Blobs. */
    private HashMap<String, Blob> _trackedFiles =
        new HashMap<String, Blob>();

}
