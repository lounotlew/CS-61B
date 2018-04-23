package gitlet;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.nio.file.Files;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import static java.nio.file.StandardCopyOption.*;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;
import java.util.Date;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Woo Sik (Lewis) Kim and Alex Walczak (Partner: cs61b-abo)
 */
public class Main {

    /** GITLET COMMANDS. */

    /** Create a new gitlet version-control system in the current directory.
     *  This system automatically starts with a commit that contains no files
     *  and "initial commit" as the commit message. It has a single branch,
     *  master, which initially points to this initial commit, and the current
     *  branch will be master. Send a message ff there already is a gitlet
     *  version-control system in the current directory. */
    private static void init() {
        File gitletSystem = new File(".gitlet");

        if (gitletSystem.exists()) {
            System.out.println("A gitlet version-control system already"
                + " exists in the current directory.");
            return;
        } else {
            gitletSystem.mkdir();
            _commitTree = new HashMap<String, Commit>();
            _commitTree.put("No Previous Commits", null);

            Date initDate = new Date();
            Commit initCommit =
                new Commit("initial commit", initDate, "No Previous Commits");
            String toHash = "initial commit" + ", " + initCommit.getDate();
            String initSha1 = Utils.sha1(toHash);

            SerUtils.saveCurrentSha1(initSha1);
            _commitTree.put(initSha1, initCommit);
            SerUtils.saveCommitTree(_commitTree);
            _stagedFiles = new ArrayList<String>();
            SerUtils.saveStagedFiles(_stagedFiles);
            _removedFiles = new ArrayList<String>();
            SerUtils.saveRemovedFiles(_removedFiles);
            _currentCommit = initCommit;
            SerUtils.saveCurrentCommit(_currentCommit);

            File staging = new File(".gitlet/staging");
            staging.mkdir();
            File branches = new File(".gitlet/branches");
            branches.mkdir();
            File currentbranch = new File(".gitlet/currentbranch");
            currentbranch.mkdir();

            (new File(".gitlet/currentbranch/master")).mkdir();
            (new File(".gitlet/currentbranch/master/tracked")).mkdir();
            (new File(".gitlet/currentbranch/master/refs")).mkdir();
            createAndWriteFile(".gitlet/currentbranch/master/headCommit",
                initSha1);
            createAndWriteFile(".gitlet/onBranch", "master");
        }
        return;
    }

    /** Add a copy of the file FILE as it currently exists into the staging
     *  area of this gitlet version-control system. */
    private static void add(File file) {
        String fname = file.getName();
        if (!(new File(file.getParentFile(), ".gitlet")).isDirectory()) {
            System.out.println("Gitlet system not initialized.");
            return;
        }
        if (file.isDirectory() || !file.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        String pathToCommitedFile, idOfFile, headCommit;
        headCommit =
            readFileContents(".gitlet/currentbranch/"
                + getCurrentBranch() + "/headCommit");

        if ((new File(".gitlet/" + headCommit)).isDirectory()) {
            List<String> lastTracked =
                Arrays.asList((new File(".gitlet/"
                    + headCommit + "/refs")).list());

            if (lastTracked.contains(fname)) {
                idOfFile = readFileContents(".gitlet/"
                    + headCommit + "/refs/" + fname);

                pathToCommitedFile = ".gitlet/" + idOfFile + "/" + fname;
                if (fequal(fname, pathToCommitedFile)) {
                    createAndWriteFile(".gitlet/" + headCommit
                        + "/refs/" + fname, idOfFile);
                    createAndWriteFile(".gitlet/currentbranch/"
                        + getCurrentBranch() + "/refs/"
                        + fname, idOfFile);
                    createAndWriteFile(".gitlet/currentbranch/"
                        + getCurrentBranch() + "/tracked/" + fname, "");

                    _removedFiles = SerUtils.loadRemovedFiles();

                    if (_removedFiles.contains(file.getName())) {
                        _removedFiles.remove(file.getName());
                    }

                    SerUtils.saveRemovedFiles(_removedFiles);
                    return;
                }
            }
        }

        File staging = new File(".gitlet/staging");
        moveFile(file.getPath(), staging.getPath()
            + "/" + file.getName());
        _stagedFiles = SerUtils.loadStagedFiles();
        _stagedFiles.add(file.getName());
        SerUtils.saveStagedFiles(_stagedFiles);

        _removedFiles = SerUtils.loadRemovedFiles();
        if (_removedFiles.contains(file.getName())) {
            _removedFiles.remove(file.getName());
        }
        SerUtils.saveRemovedFiles(_removedFiles);
        return;
    }

    /** Save a snapshot of certain files in the current commit and staging
     *  area so that they can be restored at a later time, creating a new
     *  commit. By default, each commit's snapshot of files will be exactly
     *  the same as its parent commit's snapshot of files; it will keep
     *  versions of files exactly as they are, and not update them. A commit
     *  will only update files it is tracking that have been staged at the
     *  time of commit, in which case the commit will now include the version
     *  of the file that was staged instead of the version it got from its
     *  parent. Commit message is set to MESSAGE. */
    private static void commit(String message) {
        File stagingArea = new File(".gitlet/staging");
        _commitTree = SerUtils.loadCommitTree();
        _currentSha1 = SerUtils.loadCurrentSha1();
        _currentCommit = SerUtils.loadCurrentCommit();
        _stagedFiles = SerUtils.loadStagedFiles();
        _removedFiles = SerUtils.loadRemovedFiles();

        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
            return;

        } else if (_stagedFiles.size() == 0 && _removedFiles.size() == 0) {
            System.out.println("No changes added to the commit.");
            return;

        } else {
            Date commitDate = new Date();
            Commit childCommit = new Commit(message, commitDate, _currentSha1);
            HashMap<String, Blob> parentsCommittedFiles =
                _currentCommit.getTrackedFiles();
            Set<String> fileIDs = parentsCommittedFiles.keySet();

            for (String fileID : parentsCommittedFiles.keySet()) {
                Blob fileBlob = parentsCommittedFiles.get(fileID);
                String fileName = fileBlob.getFileName();

                if (!_removedFiles.contains(fileName)
                    && !_stagedFiles.contains(fileName)) {
                    childCommit.addFile(fileID, fileBlob);
                }
            }

            File stagingDir = new File(".gitlet/staging");

            for (String fileName : stagingDir.list()) {
                File toAdd = new File(".gitlet/staging/" + fileName);
                byte[] fileData = Utils.readContents(toAdd);
                String fileID = Utils.sha1(fileData);
                Blob fileBlob =
                    new Blob(fileID, fileName, toAdd.getPath(), fileData);

                if (!childCommit.containsFile(fileID)) {
                    childCommit.addFile(fileID, fileBlob);
                }
            }

            _currentSha1 = Utils.sha1(message + ", " + childCommit.getDate());
            File newDir = new File(".gitlet/" + _currentSha1);
            File refs = new File(".gitlet/" + _currentSha1 + "/refs");
            newDir.mkdir();
            refs.mkdir();
            moveFile(".gitlet/currentbranch/" + getCurrentBranch()
                + "/refs", ".gitlet/" + _currentSha1 + "/refs");

            for (String fileName : stagingDir.list()) {
                File toAdd = new File(".gitlet/staging/" + fileName);

                try {
                    Files.copy(Paths.get(toAdd.getPath()),
                        Paths.get(newDir.getPath() + "/"
                        + toAdd.getName()), REPLACE_EXISTING);
                    createAndWriteFile(".gitlet/" + _currentSha1
                        + "/refs/" + fileName, _currentSha1);
                    createAndWriteFile(".gitlet/currentbranch/"
                        + getCurrentBranch() + "/refs/"
                        + fileName, _currentSha1);

                } catch (IOException e) {
                    System.out.println("IOException while copying files.");
                }
            }

            _commitTree.put(_currentSha1, childCommit);
            _stagedFiles = new ArrayList<String>();
            _removedFiles = new ArrayList<String>();
            SerUtils.saveCurrentSha1(_currentSha1);
            SerUtils.saveCommitTree(_commitTree);
            SerUtils.saveStagedFiles(_stagedFiles);
            SerUtils.saveRemovedFiles(_removedFiles);
            SerUtils.saveCurrentCommit(childCommit);
            String curBranch = getCurrentBranch();

            for (File f : (new File(".gitlet/staging")).listFiles()) {
                createAndWriteFile(".gitlet/currentbranch/"
                    + curBranch + "/tracked/" + f.getName(), "");
            }

            clearDirectory(".gitlet/staging");
            String oldCommitID = readFileContents(".gitlet/currentbranch/"
                + curBranch + "/headCommit");
            createAndWriteFile(".gitlet/" + _currentSha1
                + "/prevCommit", oldCommitID);
            createAndWriteFile(".gitlet/currentbranch/" + curBranch
                + "/headCommit", _currentSha1);
            return;
        }
    }

    /** Untrack the file FNAME so that it is not included in the next commit,
     *  even if it is tracked in the current commit. */
    private static void rm(String fname) {
        File checkStaging = new File(".gitlet/staging/" + fname);
        File checkRefs = new File(".gitlet/currentbranch/"
            + getCurrentBranch() + "/refs/" + fname);
        File checkTracked = new File(".gitlet/currentbranch/"
            + getCurrentBranch() + "/tracked/" + fname);

        if (!checkStaging.isFile() && !checkTracked.isFile()
            && !checkRefs.isFile()) {
            System.out.println("No reason to remove the file.");
            return;
        }

        File toRm = new File(fname);

        if (checkStaging.isFile()) {
            checkStaging.delete();
        }

        if (checkRefs.isFile()) {
            checkRefs.delete();
            toRm.delete();
            _removedFiles = SerUtils.loadRemovedFiles();
            _removedFiles.add(fname);
            SerUtils.saveRemovedFiles(_removedFiles);
        }

        if (checkTracked.isFile()) {
            checkTracked.delete();
            toRm.delete();
            _removedFiles = SerUtils.loadRemovedFiles();
            _removedFiles.add(fname);
            SerUtils.saveRemovedFiles(_removedFiles);
        }
    }

    /** Display information about each commit backwards along the commit tree
     *  until the initial commit. Print the SHA-1 hash, then the commit date,
     *  and finally the commit message. */
    private static void log() {
        String currentCommitID = readFileContents(".gitlet/currentbranch/"
            + getCurrentBranch() + "/headCommit");
        ArrayList<String> parentCommits = commitTrail(currentCommitID);
        _commitTree = SerUtils.loadCommitTree();

        for (String id : parentCommits) {
            _currentCommit = _commitTree.get(id);

            if (_currentCommit == null) {
                continue;
            }

            System.out.println("===");
            System.out.println("Commit " + id);
            System.out.println(_currentCommit.getDate());
            System.out.println(_currentCommit.getMessage());
            System.out.println();
        }
    }

    /** Display information about every commit ever made, from the initial
     *  commit to the Nth commit (current commit). */
    private static void globalLog() {
        _commitTree = SerUtils.loadCommitTree();
        Set<String> sha1ids = _commitTree.keySet();
        Commit commit;

        for (String id : sha1ids) {

            if (id.equals("No Previous Commits")) {
                continue;
            }

            commit = _commitTree.get(id);
            System.out.println("===");
            System.out.println("Commit " + id);
            System.out.println(commit.getDate());
            System.out.println(commit.getMessage());
            System.out.println();
        }
    }

    /** Print out the IDs of all commits that have the given message MESSAGE,
     *  one per line. If there are multiple such commits, it prints the IDs
     *  out on seperate lines. */
    private static void find(String message) {
        _commitTree = SerUtils.loadCommitTree();
        Set<String> sha1ids = _commitTree.keySet();
        Commit commit;
        String commitMsg;
        int count = 0;

        for (String id : sha1ids) {
            commit = _commitTree.get(id);

            if (commit != null) {
                commitMsg = commit.getMessage();

                if (commitMsg.equals(message)) {
                    count += 1;
                    System.out.println(id);
                }
            }
        }

        if (count == 0) {
            System.out.println("Found no commit with that message.");
        }
        return;
    }

    /** Display what branches currently exist, and marks the current branch
     *  with a *. Also displays what files have been staged or marked for
     *  untracking. */
    private static void status() {
        File curDir = new File(".");
        List<String> workDirFiles = Arrays.asList(curDir.list());
        File trackedDir = new File(".gitlet/currentbranch/"
            + getCurrentBranch() + "/refs");
        List<String> trackedFiles = Arrays.asList(trackedDir.list());

        System.out.println("=== Branches ===");
        System.out.print("*");
        System.out.println(readFileContents(".gitlet/onBranch"));
        List<String> allBranches = Utils.dirs(".gitlet/branches");
        if (allBranches != null) {
            for (String b : allBranches) {
                System.out.println(b);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        ArrayList<String> staging =
            new ArrayList<String>(Utils.plainFilenamesIn(".gitlet/staging"));
        if (staging != null) {
            for (String s : staging) {
                System.out.println(s);
            }
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        _removedFiles = SerUtils.loadRemovedFiles();
        if (_removedFiles != null) {
            Collections.sort(_removedFiles);
            for (String f : _removedFiles) {
                System.out.println(f);
            }
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        String resolvedID, commitedPath, stagedPath;
        ArrayList<String> changeMod = new ArrayList<String>();
        for (String w : workDirFiles) {
            if (trackedFiles.contains(w) && !staging.contains(w)) {
                resolvedID = readFileContents(".gitlet/currentbranch/"
                    + getCurrentBranch() + "/refs/" + w);
                commitedPath = ".gitlet/" + resolvedID + "/" + w;
                if (!fequal(commitedPath, w)) {
                    changeMod.add(w + " (modified)");
                }
            }
        }
        for (String w : trackedFiles) {
            if (!_removedFiles.contains(w) && !workDirFiles.contains(w)) {
                if (!changeMod.contains(w)) {
                    changeMod.add(w + " (deleted)");
                }
            }
        }
        for (String w : staging) {
            if (workDirFiles.contains(w)) {
                stagedPath = ".gitlet/staging/" + w;
                if (!fequal(stagedPath, w)) {
                    if (!changeMod.contains(w)) {
                        changeMod.add(w + " (modified)");
                    }
                }
            } else {
                if (!changeMod.contains(w)) {
                    changeMod.add(w + " (deleted)");
                }
            }
        }
        Collections.sort(changeMod);
        for (String f : changeMod) {
            System.out.println(f);
        }
        System.out.println();
        System.out.println("=== Untracked Files ===");
        ArrayList<String> untrackedFiles = new ArrayList<String>();
        for (String w : workDirFiles) {
            if (!staging.contains(w) && !trackedFiles.contains(w)) {
                if ((new File(w)).isFile()) {
                    untrackedFiles.add(w);
                }
            }
        }
        Collections.sort(untrackedFiles);
        for (String name : untrackedFiles) {
            System.out.println(name);
        }
        System.out.println();
    }

    /** Take the version of the file with FNAME as it exists in the head commit,
     *  the front of the current branch, and puts it in the working directory,
     *  overwriting the version of the file that's already there if there is one.
     *  The new version of the file is not staged. */
    private static void checkout1(String fname) {
        String commitID = readFileContents(".gitlet/currentbranch/"
            + getCurrentBranch() + "/headCommit");
        List<String> refs = Arrays.asList(new File(".gitlet/currentbranch/"
            + getCurrentBranch() + "/refs").list());
        if (!refs.contains(fname)) {
            System.out.println("File does not exist in that commit.");
            return;
        } else {
            File file = new File(".gitlet/" + readFileContents(".gitlet/"
                + commitID + "/refs/" + fname),
                fname);
            try {
                Files.copy(Paths.get(file.getPath()),
                    Paths.get(fname), REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("IOException while checking out files.");
            }
        }
    }

    /** Take the version of the file with FNAMEas it exists in the commit with
     *  the given commit ID COMMITID, and puts it in the working directory,
     *  overwriting the version of the file that's already there if there is one.
     *  The new version of the file is not staged. */
    private static void checkout2(String commitID, String fname) {
        if (MAX > commitID.length() && commitID.length() > MIN) {
            commitID = matchShaID(commitID);
        } else if (commitID.length() > MAX || commitID.length() <= MIN) {
            System.out.println("No commit with that id exists.");
            return;
        }
        File commitFile = new File(".gitlet/" + commitID);
        if (!commitFile.isDirectory()) {
            System.out.println("No commit with that id exists.");
            return;
        }
        List<String> refs =
            Arrays.asList(new File(".gitlet/" + commitID + "/refs").list());
        if (!refs.contains(fname)) {
            System.out.println("File does not exist in that commit.");
            return;
        } else {
            File file = new File(".gitlet/" + readFileContents(".gitlet/"
                + commitID + "/refs/" + fname),
                fname);
            try {
                Files.copy(Paths.get(file.getPath()),
                    Paths.get(fname), REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println("IOException while checking out files.");
            }
        }
    }

    /** Take all files in the commit at the head of the given branch with BNAME,
     *  and puts them in the working directory, overwriting the versions of the
     *  files that are already there if they exist. Also, at the end of this
     *  command, the given branch will now be considered the current branch.
     *  Any files that are tracked in the current branch but are not present in
     *  the checked-out branch are deleted. The staging area is cleared, unless
     *  the checked-out branch is the current branch. */
    private static void checkout3(String bname) {
        List<String> allBranches =
            Arrays.asList((new File(".gitlet/branches")).list());
        if (bname.equals(getCurrentBranch())) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        if (!allBranches.contains(bname)) {
            System.out.println("No such branch exists.");
            return;
        }
        String oldbranch = getCurrentBranch();
        String id = readFileContents(".gitlet/branches/"
            + bname + "/headCommit");
        File curDir = new File(".");
        List<String> filesToRestore =
            Arrays.asList((new File(".gitlet/branches/"
                + bname + "/refs")).list());
        File[] workDirFiles = curDir.listFiles();
        List<String> curBranchFiles =
            Arrays.asList((new File(".gitlet/currentbranch/"
                + oldbranch + "/tracked")).list());
        List<String> checkoutBranchFiles =
            Arrays.asList((new File(".gitlet/branches/"
                + bname + "/tracked")).list());

        for (File w : workDirFiles) {
            if (!curBranchFiles.contains(w.getName())
                && checkoutBranchFiles.contains(w.getName())) {
                System.out.println("There is an untracked file in the way;"
                    + " delete it or add it first.");
                return;
            }
        }
        for (String f : filesToRestore) {
            checkout2(id, f);
        }
        for (File w : workDirFiles) {
            if (curBranchFiles.contains(w.getName())
                && !(checkoutBranchFiles.contains(w.getName()))) {
                w.delete();
            }
        }
        purgeDirectoryContents(new File(".gitlet/staging"));
        moveFile(".gitlet/branches/" + bname, ".gitlet/currentbranch/" + bname);
        moveFile(".gitlet/currentbranch/" + oldbranch,
            ".gitlet/branches/" + oldbranch);
        purgeDirectory(new File(".gitlet/branches/" + bname));
        purgeDirectory(new File(".gitlet/currentbranch/" + oldbranch));
        createAndWriteFile(".gitlet/onBranch", bname);
    }

    /** Create a new branch with BNAME, and points it at the current head node. */
    private static void branch(String bname) {
        List<String> allBranches =
            Arrays.asList((new File(".gitlet/branches")).list());
        if (bname.equals(getCurrentBranch()) || allBranches.contains(bname)) {
            System.out.println("A branch with that name already exists.");
            return;
        } else {
            String curBranchID = readFileContents(".gitlet/currentbranch/"
                + getCurrentBranch() + "/headCommit");
            new File(".gitlet/branches/" + bname).mkdir();
            new File(".gitlet/branches/" + bname + "/tracked").mkdir();
            new File(".gitlet/branches/" + bname + "/refs").mkdir();
            moveFile(".gitlet/currentbranch/" + getCurrentBranch() + "/refs",
                ".gitlet/branches/" + bname + "/refs");
            createAndWriteFile(".gitlet/branches/" + bname + "/headCommit",
                curBranchID);
            moveFile(".gitlet/currentbranch/" + getCurrentBranch()
                + "/tracked", ".gitlet/branches/" + bname + "/tracked");
        }
    }

    /** Remove the branch with BNAME. This only means to delete the pointer
     *  associated with the branch; it does not mean to delete all commits that
     *  were created under the branch. */
    private static void rmbranch(String bname) {
        if (bname.equals(getCurrentBranch())) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        File toDelete = new File(".gitlet/branches/" + bname);
        if (toDelete.isDirectory()) {
            deleteRecursive(toDelete);
        } else {
            System.out.println("A branch with that name does not exist.");
        }
    }

    /** Check out all the files tracked by the given commit ID. Removes tracked
     *  files that are not present in the given file. Also moves the current
     *  branch's head to that commit node. */
    private static void reset(String id) {
        if (MAX > id.length() && id.length() > MIN) {
            id = matchShaID(id);
            if (id.equals("")) {
                System.out.println("No commit with that id exists.");
                return;
            }
        } else if (id.length() > MAX || id.length() <= MIN) {
            System.out.println("No commit with that id exists.");
            return;
        }
        File commitToRestore = new File(".gitlet/" + id);
        if (!commitToRestore.isDirectory()) {
            System.out.println("No commit with that id exists.");
            return;
        }

        List<String> trackedByCommit =
            Arrays.asList((new File(".gitlet/" + id + "/refs")).list());
        File curDir = new File(".");
        File[] workDirFiles = curDir.listFiles();
        List<String> curTracked =
            Arrays.asList((new File(".gitlet/currentbranch/"
                + getCurrentBranch() + "/tracked")).list());
        List<String> idTracked = Arrays.asList((new File(".gitlet/"
            + id + "/refs")).list());

        for (File w : workDirFiles) {
            if (idTracked.contains(w.getName())
                && !curTracked.contains(w.getName())) {
                System.out.println("There is an untracked file in the way; "
                    + "delete it or add it first.");
                return;
            }
        }

        for (String f : trackedByCommit) {
            checkout2(id, f);
        }

        for (File w : workDirFiles) {
            if (!idTracked.contains(w.getName())
                && curTracked.contains(w.getName())) {
                w.delete();
            }
        }

        createAndWriteFile(".gitlet/currentbranch/"
            + getCurrentBranch() + "/headCommit", id);
        purgeDirectoryContents(new File(".gitlet/currentbranch/"
            + getCurrentBranch() + "/refs"));
        purgeDirectoryContents(new File(".gitlet/currentbranch/"
            + getCurrentBranch() + "/tracked"));
        moveFile(".gitlet/" + id + "/refs", ".gitlet/currentbranch/"
            + getCurrentBranch() + "/refs");
        moveFile(".gitlet/" + id + "/refs", ".gitlet/currentbranch/"
            + getCurrentBranch() + "/tracked");
        clearDirectory(".gitlet/staging");
    }

    /** Merge files from the given branch BNAME into the current branch. */
    private static void merge(String bname) {
        if (Utils.plainFilenamesIn(".gitlet/staging").size() > 0) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        if (bname.equals(getCurrentBranch())) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        File checkBnameExists  = new File(".gitlet/branches/" + bname);
        if (!checkBnameExists.isDirectory()) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        String splitPoint, curHeadCommit, bnameHeadCommit;
        splitPoint = splitPoint(bname);
        curHeadCommit = readFileContents(".gitlet/currentbranch/"
            + getCurrentBranch() + "/headCommit");
        bnameHeadCommit = readFileContents(".gitlet/branches/"
            + bname + "/headCommit");
        if (splitPoint.equals(bnameHeadCommit)) {
            System.out.println("Given branch is an "
                + "ancestor of the current branch.");
            return;
        }
        File curDir = new File(".");
        File[] workDirFiles = curDir.listFiles();
        List<String> curTracked =
            Arrays.asList((new File(".gitlet/currentbranch/"
                + getCurrentBranch() + "/tracked")).list());
        List<String> bnameTracked = Arrays.asList((new File(".gitlet/"
            + bnameHeadCommit + "/refs")).list());
        for (File w : workDirFiles) {
            if (bnameTracked.contains(w.getName())
                && !curTracked.contains(w.getName())) {
                System.out.println("There is an untracked file in the way; "
                    + "delete it or add it first.");
                return;
            }
        }
        if (splitPoint.equals(curHeadCommit)) {
            reset(bnameHeadCommit);
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        ArrayList<String> lookedOver = new ArrayList<String>();
        ArrayList<String> modifiedBname =
            filesModifiedSinceID(splitPoint, bname);
        ArrayList<String> modifiedCurrent =
            filesModifiedSinceID(splitPoint, getCurrentBranch());

        for (String f : modifiedBname) {
            if (!modifiedCurrent.contains(f)) {
                checkout2(bnameHeadCommit, f);
                add(new File(".gitlet/currentbranch/"
                    + getCurrentBranch() + "/" + f));
            }
        }
        List<String> splitPointFiles = Arrays.asList((new File(".gitlet/"
            + splitPoint + "/refs")).list());
        List<String> bnameFiles =
            Arrays.asList((new File(".gitlet/branches/"
                + bname + "/refs")).list());
        List<String> curBranchFiles =
            Arrays.asList((new File(".gitlet/currentbranch/"
                + getCurrentBranch() + "/refs")).list());
        for (String f : bnameFiles) {
            if (!curBranchFiles.contains(f) && !splitPointFiles.contains(f)) {
                checkout2(bnameHeadCommit, f);
                add(new File(f));
            }
        }
        for (String f : splitPointFiles) {
            if (!modifiedCurrent.contains(f) && !bnameFiles.contains(f)) {
                rm(f);
            }
        }
        ArrayList<String> conflictingBoth = new ArrayList<String>();
        ArrayList<String> confOnlyInCur = new ArrayList<String>();
        ArrayList<String> confOnlyInBname = new ArrayList<String>();
        String curBranchID, bnameID;
        for (String f : modifiedCurrent) {
            if (modifiedBname.contains(f)) {
                curBranchID = readFileContents(".gitlet/currentbranch/"
                    + getCurrentBranch() + "/refs/" + f);
                bnameID = readFileContents(".gitlet/branches/"
                    + bname + "/refs/" + f);
                if (!fequal(".gitlet/" + curBranchID + "/" + f,
                    ".gitlet/" + bnameID + "/" + f)) {
                    conflictingBoth.add(f);
                }
            } else {

                confOnlyInCur.add(f);
            }
        }
        for (String f : modifiedBname) {
            if (!modifiedCurrent.contains(f)) {
                confOnlyInBname.add(f);
            }
        }
        for (String f : curBranchFiles) {
            if (!splitPointFiles.contains(f) && bnameFiles.contains(f)) {
                if (!conflictingBoth.contains(f)) {
                    curBranchID = readFileContents(".gitlet/currentbranch/"
                        + getCurrentBranch() + "/refs/" + f);
                    bnameID = readFileContents(".gitlet/branches/"
                        + bname + "/refs/" + f);
                    if (!fequal(".gitlet/" + curBranchID + "/" + f,
                        ".gitlet/" + bnameID + "/" + f)) {
                        conflictingBoth.add(f);
                    }
                }
            }
        }
        for (String f : bnameFiles) {
            if (!splitPointFiles.contains(f) && curBranchFiles.contains(f)) {
                if (!conflictingBoth.contains(f)) {
                    curBranchID = readFileContents(".gitlet/currentbranch/"
                        + getCurrentBranch() + "/refs/" + f);
                    bnameID = readFileContents(".gitlet/branches/"
                        + bname + "/refs/" + f);
                    if (!fequal(".gitlet/" + curBranchID + "/" + f,
                        ".gitlet/" + bnameID + "/" + f)) {
                        conflictingBoth.add(f);
                    }
                }
            }
        }
        if ((conflictingBoth.size() + confOnlyInBname.size()
            + confOnlyInCur.size()) == 0) {
            commit("Merged " + getCurrentBranch()
                + " with " + bname + ".");
            return;
        }
        File dummy = new File(".dummy");
        createAndWriteFile(".dummy", "");
        for (String f : confOnlyInCur) {
            curBranchID = readFileContents(".gitlet/currentbranch/"
                + getCurrentBranch() + "/refs/" + f);
            dealWithConflict(".gitlet/" + curBranchID + "/" + f, ".dummy", f);
        }
        for (String f : confOnlyInBname) {
            bnameID = readFileContents(".gitlet/branches/"
                + bname + "/refs/" + f);
            dealWithConflict(".dummy", ".gitlet/" + bnameID + "/" + f, f);
        }
        for (String f : conflictingBoth) {
            curBranchID = readFileContents(".gitlet/currentbranch/"
                + getCurrentBranch() + "/refs/" + f);
            bnameID = readFileContents(".gitlet/branches/"
                + bname + "/refs/" + f);
            dealWithConflict(".gitlet/" + curBranchID + "/" + f,
                ".gitlet/" + bnameID + "/" + f, f);
        }
        dummy.delete();
        System.out.println("Encountered a merge conflict.");
    }

    /** UTILITY METHODS FOR GITLET COMMANDS. */

    /** Return the SHA-1 ID that matches the first n characters given by KEY. If
     *  no such SHA-1 ID exists, then return a blank string. */
    private static String matchShaID(String key) {
        int keyLength = key.length();
        _commitTree = SerUtils.loadCommitTree();
        Set<String> allShaIDs = _commitTree.keySet();

        for (String id : allShaIDs) {
            String firstNchar = id.substring(0, keyLength);

            if (firstNchar.equals(key)) {
                return id;
            }
        }

        String blank = "";
        return blank;
    }

    /** Return the SHA-1 ID of the current branch. */
    private static String getCurrentBranch() {
        return readFileContents(".gitlet/onBranch");
    }

    /** Unpack contents of the directory SOURCE. */
    private static void unpackDirContents(String source) {
        File sourceDir = new File(source);
        File[] sourceDirContents = sourceDir.listFiles();

        for (File f : sourceDirContents) {
            moveFile(f.getPath(), f.getName());
        }
    }

    /** Clear (delete) all the files in the directory DIR. */
    private static void clearDirectory(String dir) {
        File dirToClear = new File(dir);
        String[] filesToDelete = dirToClear.list();

        for (String fileName : filesToDelete) {
            File toDelete = new File(dir + "/" + fileName);
            toDelete.delete();
        }
        return;
    }

    /** Purge everything in the directory DIR.
     *  See: http://stackoverflow.com/questions/
     *  13195797/delete-all-files-in-directory
     *  -but-not-directory-one-liner-solution. */
    private static void purgeDirectory(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                purgeDirectory(file);
            }
            file.delete();
        }
        dir.delete();
    }

    /** Purge all the contents in the directory DIR.
     *  See: http://stackoverflow.com/questions/
     *  13195797/delete-all-files-in-directory
     *  -but-not-directory-one-liner-solution. */
    private static void purgeDirectoryContents(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                purgeDirectoryContents(file);
            }
            file.delete();
        }
    }

    /** Recursively delete from PATH.
     *  Adapted from Stack overflow. http://stackoverflow
     *  .com/questions/779519/delete-directories-
     *  recursively-in-java and PATH. */
    private static void deleteRecursive(File path) {
        File[] c = path.listFiles();

        for (File file : c) {
            if (file.isDirectory()) {
                deleteRecursive(file);
                file.delete();

            } else {
                file.delete();
            }
        }
        path.delete();
    }

    /** Return each subsequent parent commit ID of the commit given
     *  by COMMITID. */
    private static ArrayList<String> commitTrail(String commitID) {
        ArrayList<String> trail = new ArrayList<String>();
        trail.add(commitID);
        File checkingCommitDir = new File(".gitlet/" + commitID);

        while (checkingCommitDir.isDirectory()) {
            commitID = readFileContents(checkingCommitDir + "/prevCommit");
            trail.add(commitID);
            checkingCommitDir = new File(".gitlet/" + commitID);
        }
        return trail;
    }

    /** Restore a commit with the SHA-1 hash ID. */
    private static void restoreCommit(String id) {
        unpackDirContents(".gitlet/" + id);
    }

    /** Return the common ancestor commit of the current head commit
     *  and the head commit of branch BNAME. */
    private static String splitPoint(String bname) {
        String curHeadCommit, bnameHeadCommit;
        ArrayList<String> curTrail = new ArrayList<String>();
        ArrayList<String> bnameTrail = new ArrayList<String>();
        int searchIndex = 0;
        curHeadCommit = readFileContents(".gitlet/currentbranch/"
            + getCurrentBranch() + "/headCommit");

        if (bname.equals(getCurrentBranch())) {
            return curHeadCommit;

        } else {
            bnameHeadCommit = readFileContents(".gitlet/branches/"
            + bname + "/headCommit");
            curTrail = commitTrail(curHeadCommit);
            bnameTrail = commitTrail(bnameHeadCommit);

            for (int i = 0; i < bnameTrail.size(); i += 1) {
                if (curTrail.contains(bnameTrail.get(i))) {
                    return bnameTrail.get(i);
                }
            }
            return null;
        }
    }

    /** Properly format a conflicting file at HEADPATH, BNAMEPATH,
     *  and FNAME. */
    private static void dealWithConflict(String headpath,
        String bnamepath, String fname) {
        String line1, line2;
        File file1 = new File(headpath);
        File file2 = new File(bnamepath);
        File conflicting = new File(".conflicting");
        createAndWriteFile(".conflicting", "");

        addToEnd(".conflicting", "<<<<<<< HEAD");
        try (BufferedReader r1 = new BufferedReader(new FileReader(file1))) {
            while ((line1 = r1.readLine()) != null) {
                addToEnd(".conflicting", line1);
            }
        } catch (IOException e) {
            System.out.println("ERROR: 1");
        }

        addToEnd(".conflicting", "=======");
        try (BufferedReader r2 = new BufferedReader(new FileReader(file2))) {
            while ((line2 = r2.readLine()) != null) {
                addToEnd(".conflicting", line2);
            }
        } catch (IOException e) {
            System.out.println("ERROR: 2");
        }
        addToEnd(".conflicting", ">>>>>>>");
        conflicting.renameTo(new File(fname));
    }

    /** Return the contents of the file given by PATH. */
    public static String readFileContents(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            System.out.println("Problem in readFileContents.");
        }
        return null;
    }

    /** Create and write a file with contents TEXT at PATH. */
    public static void createAndWriteFile(String path, String text) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                      new FileOutputStream(path), "utf-8"))) {
            writer.write(text);
        } catch (IOException e) {
            System.out.println("Problem in createAndWriteFile.");
        }
    }

    /** Add TEXT to the end of a file given by PATH. */
    private static void addToEnd(String path, String text) {
        try (PrintWriter out = new PrintWriter(
                new BufferedWriter(new FileWriter(path, true)))) {
            out.println(text);
        } catch (IOException e) {
            System.out.println("Problem in addToEnd.");
        }
    }

    /** Move a file from SOURCE to DEST. */
    private static void moveFile(String source, String dest) {
        File sourceDir = new File(source);
        File destDir = new File(dest);
        if (sourceDir.isDirectory()) {
            if (!destDir.exists()) {
                destDir.mkdir();
            }
            String[] sourceDirContents = sourceDir.list();
            for (String f : sourceDirContents) {
                moveFile(source + "/" + f, dest + "/" + f);
            }
        } else {
            try {
                Files.copy(Paths.get(source),
                    Paths.get(dest), REPLACE_EXISTING);
            } catch (IOException excp) {
                System.out.println("IOException in moveFile.");
            }
        }
    }

    /** Return the files modified since the commit with ID and branch BNAME. */
    private static ArrayList<String>
        filesModifiedSinceID(String id, String bname) {
        String resolveID, resolveBnameHead;
        File curRef, idRef;
        ArrayList<String> modified = new ArrayList<String>();
        if (bname.equals(getCurrentBranch())) {
            curRef = new File(".gitlet/currentbranch/"
                + bname + "/refs");
        } else {
            curRef = new File(".gitlet/branches/"
                + bname + "/refs");
        }
        idRef = new File(".gitlet/" + id + "/refs");
        String[] curRefs = curRef.list();
        List<String> idRefs = Arrays.asList(idRef.list());
        for (String f : curRefs) {
            if (idRefs.contains(f)) {
                resolveID = readFileContents(idRef + "/" + f);
                resolveBnameHead = readFileContents(curRef + "/" + f);

                if (!fequal(".gitlet/" + resolveID + "/" + f,
                    ".gitlet/" + resolveBnameHead + "/" + f)) {
                    modified.add(f);
                }
            }
        }
        return modified;
    }

    /** Return true iff PATH1 equals PATH2. */
    private static boolean fequal(String path1, String path2) {
        String line1, line2;
        File file1 = new File(path1);
        File file2 = new File(path2);
        try (BufferedReader r1 = new BufferedReader(new FileReader(file1));
            BufferedReader r2 = new BufferedReader(new FileReader(file2))) {
            line1 = r1.readLine();
            line2 = r2.readLine();
            if (line1 == null && line2 == null) {
                return true;
            }
            if ((line1 == null && line2 != null)
                || (line2 == null && line1 != null)) {
                return false;
            }
        } catch (IOException e) {
            System.out.println("ERROR: OUTER");
        }
        try (BufferedReader r1 = new BufferedReader(new FileReader(file1));
            BufferedReader r2 = new BufferedReader(new FileReader(file2))) {
            while ((line1 = r1.readLine()) != null) {
                line2 = r2.readLine();
                if (line2 != null) {
                    if (!line1.equals(line2)) {
                        return false;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR: OUTER");
        }
        return true;
    }

    /** Return true iff PATH1 equals PATH2. */
    private static boolean fequal2(String path1, String path2) {
        return readFileContents(path1).equals(readFileContents(path2));
    }

    /** Return the argument(s) of ARGS, after a command. */
    private static String getArg(String... args) {
        String arg = "";
        for (int i = 1; i < args.length; i += 1) {
            arg = arg + args[i];
            if (i != args.length - 1) {
                arg = arg + " ";
            }
        }
        return arg;
    }

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        File isInit = new File(".gitlet");
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        if (args[0].equals("init")) {
            if (args.length != 1) {
                System.out.println("Incorrect operands.");
                return;
            }
            init();
        } else if (!isInit.isDirectory()) {
            System.out.println("Not in an initialized gitlet directory.");
            return;
        } else if (args[0].equals("add")) {
            if (args.length != 2) {
                System.out.println("Incorrect operands.");
                return;
            }
            _workingDirectory = Paths.get(".").toAbsolutePath();
            File fileToAdd = new File(_workingDirectory.toString() + "/"
                + getArg(args));
            add(fileToAdd);

        } else if (args[0].equals("commit")) {
            if (args.length != 2) {
                System.out.println("Incorrect operands.");
                return;
            }
            commit(getArg(args));
        } else if (args[0].equals("rm")) {
            if (args.length != 2) {
                System.out.println("Incorrect operands.");
                return;
            }
            rm(args[1]);
        } else if (args[0].equals("log")) {
            if (args.length != 1) {
                System.out.println("Incorrect operands.");
                return;
            }
            log();
        } else if (args[0].equals("global-log")) {
            if (args.length != 1) {
                System.out.println("Incorrect operands.");
                return;
            }
            globalLog();
        } else if (args[0].equals("find")) {
            if (args.length != 2) {
                System.out.println("Incorrect operands.");
                return;
            }
            find(getArg(args));
        } else if (args[0].equals("status")) {
            if (args.length != 1) {
                System.out.println("Incorrect operands.");
                return;
            }
            status();
        } else if (args[0].equals("checkout")) {
            if (args.length < 1 || args.length > 4) {
                System.out.println("Incorrect operands.");
                return;
            }
            if (args.length == 3) {
                checkout1(args[2]);
            }
            if (args.length == 4) {
                checkout2(args[1], args[3]);
            }
            if (args.length == 2) {
                checkout3(args[1]);
            }
        } else if (args[0].equals("branch")) {
            if (args.length != 2) {
                System.out.println("Incorrect operands.");
                return;
            }
            branch(args[1]);
        } else if (args[0].equals("rm-branch")) {
            if (args.length != 2) {
                System.out.println("Incorrect operands.");
                return;
            }
            rmbranch(args[1]);
        } else if (args[0].equals("reset")) {
            if (args.length != 2) {
                System.out.println("Incorrect operands.");
                return;
            }
            reset(args[1]);
        } else if (args[0].equals("merge")) {
            if (args.length != 2) {
                System.out.println("Incorrect operands.");
                return;
            }
            merge(args[1]);
        } else {
            System.out.println("No command with that name exists.");
            return;
        }
    }

    /** The current commit. */
    private static Commit _currentCommit;
    /** The SHA-1 hashcode of the current commit. */
    private static String _currentSha1;
    /** Map the SHA-1 hashcode of a commit to its respective Commit, in order to
     * keep track of all commits made in this gitlet version-control system. */
    private static HashMap<String, Commit> _commitTree;
    /** Staged files. */
    private static ArrayList<String> _stagedFiles;
    /** Files to be removed from the next commit. */
    private static ArrayList<String> _removedFiles;
    /** The working directory that implements this gitlet
     *  version-control system. */
    private static Path _workingDirectory;
    /** Max length of SHA-1. */
    private static final int MAX = 40;
    /** Min length of SHA-1. */
    private static final int MIN = 5;

}
