import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.TreeSet;

/** Graph searching utlity.
 *  @author Woo Sik (Lewis) Kim
 */
public class Search {

    /** Return a shortest path from vertex START to vertex END in G.
     *  The edge labels of G are the lengths of the edges (all
     *  non-negative).  The vertex labels are 3-element arrays whose first
     *  2 elements contain the coordinates of the vertices.  The third element
     *  in each vertex label is unused (and may be modified by shortestPath)
     *  The Euclidean distance between vertices joined by an edge is
     *  guaranteed to be less than or equal to the length of the edge.
     *  The path is returned as a list of the vertices along the path,
     *  with the first being START and the last being LAST. */
    public static List<Integer> shortestPath(Graph<double[], Double> G,
                                             int start, int end) {
        List<Integer> path = new ArrayList<Integer>();
        List<Integer> vertices = G.successors(start);
        TreeSet<Integer> fringe = new TreeSet<Integer>();
        fringe.add(start);
        fringe.add(end);

        for (Integer vertex : vertices) {
            fringe.add(vertex);
        }

        while (!fringe.isEmpty()) {
            Integer v = fringe.pollFirst();
            double[] eLabel = G.getLabel(v);
            // Calculate distance.
        }

        // Add distance to path.

        return path;

    }
}

// PriorityQueue<Vertex> fringe;
// For each node v { v.dist() = ∞; v.back() = null; }
// s.dist() = 0;
// fringe = priority queue ordered by smallest .dist();
// add all vertices to fringe;
// while (! fringe.isEmpty()) {
// Vertex v = fringe.removeFirst ();
// For each edge (v,w) {
// if (v.dist() + weight(v,w) < w.dist())
// { w.dist() = v.dist() + weight(v,w); w.back() = v; }
// }
