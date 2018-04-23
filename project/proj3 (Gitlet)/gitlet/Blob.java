package gitlet;

import java.io.Serializable;

/** The Blob class that stores all metadata and content data of a file.
 *  @author Woo Sik (Lewis) Kim and Alex Walczak (Partner: cs61b-abo)
 */
public class Blob implements Serializable {

    /** A new Blob whose SHA-1 ID is set to HASH, file name set to
     *  NAME, file path set to PATH, and file contents set to
     *  CONTENTS. */
    public Blob(String hash, String name, String path, byte[] contents) {
        _sha1hash = hash;
        _fileName = name;
        _filePath = path;
        _contents = contents;
    }

    /** Return the SHA-1 hash of this Blob's file. */
    String getHash() {
        return _sha1hash;
    }

    /** Return the name of this Blob's file. */
    String getFileName() {
        return _fileName;
    }

    /** Return the path of this Blob's file. */
    String getFilePath() {
        return _filePath;
    }

    /** Return the contents of this Blob's file. */
    byte[] getContents() {
        return _contents;
    }

    /** The SHA-1 Hash of the file this Blob represents. */
    private String _sha1hash;
    /** The file name of the file this Blob represents. */
    private String _fileName;
    /** The file path of the file this Blob represents. */
    private String _filePath;
    /** The contents of the file this Blob represents. */
    private byte[] _contents;

}
