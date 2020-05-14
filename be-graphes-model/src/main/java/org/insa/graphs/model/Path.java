package org.insa.graphs.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * <p>
 * Class representing a path between nodes in a graph.
 * </p>
 * 
 * <p>
 * A path is represented as a list of {@link Arc} with an origin and not a list
 * of {@link Node} due to the multi-graph nature (multiple arcs between two
 * nodes) of the considered graphs.
 * </p>
 *
 */
public class Path {

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the fastest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     */
	public static Path createFastestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {

        List<Arc> arcs = new ArrayList<Arc>();

        //liste des nodes vide
        if (nodes.isEmpty()) {
            return new Path(graph, arcs);
        }
        // un seul noeud 
        // on utilise nodes.get(0) 
        // le id 0 du coup car un seul noeud 
        else if(nodes.size()==1) {
            return new Path(graph,nodes.get(0)); 
        }

        //variables
        Node node_current, node_next;
        List<Arc> successors;

        for (int i=0;i<(nodes.size()-1);i++) {
            node_current = nodes.get(i);
            node_next = nodes.get(i+1);
            // determiner le successors
            successors = node_current.getSuccessors();
            Arc a_current;
            Arc a_min = null;
            double Min = Double.MAX_VALUE;
            for (int j=0;j<successors.size();j++) {
                a_current = successors.get(j);
                if (node_next.equals(a_current.getDestination())){
                    if(a_current.getMinimumTravelTime()<Min) {
                        Min = a_current.getMinimumTravelTime();
                        a_min = a_current;
                    }
                }
            }
            // faut considerer le cas ou les deux nodes consecutif n'ont aucun lien
            if (a_min == null) {
                throw (new IllegalArgumentException("pas de lien !\n"));
            }
            arcs.add(a_min);
        }


        return new Path(graph, arcs);
    }
			

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the shortest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     */
	public static Path createShortestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {

        List<Arc> arcs = new ArrayList<Arc>();

        //pas de noeud dans la liste
        if (nodes.isEmpty()) {
            return new Path(graph, arcs);
        }
        else if(nodes.size()==1) {
            return new Path(graph,nodes.get(0));
        }

        //variable
        Node node_current, node_next;;
        List<Arc> successors;

        for (int i=0;i<(nodes.size()-1);i++) {
            node_current = nodes.get(i);
            node_next = nodes.get(i+1);
            successors = node_current.getSuccessors();
            Arc a_current;
            Arc a_min = null;
            float Min = Float.MAX_VALUE;
            for (int j=0;j<successors.size();j++) {
                a_current = successors.get(j);
                if (node_next.equals(a_current.getDestination())){
                    if(a_current.getLength()<Min) {
                        Min = a_current.getLength();
                        a_min = a_current;
                    }
                }
            }
            //pas de lien entre les noeuds consec
            if (a_min == null) {
                throw (new IllegalArgumentException("pas de lien!\n"));
            }
            arcs.add(a_min);
        }

        return new Path(graph, arcs);
    }
    /**
     * Concatenate the given paths.
     * 
     * @param paths Array of paths to concatenate.
     * 
     * @return Concatenated path.
     * 
     * @throws IllegalArgumentException if the paths cannot be concatenated (IDs of
     *         map do not match, or the end of a path is not the beginning of the
     *         next).
     */
    public static Path concatenate(Path... paths) throws IllegalArgumentException {
        if (paths.length == 0) {
            throw new IllegalArgumentException("Cannot concatenate an empty list of paths.");
        }
        final String mapId = paths[0].getGraph().getMapId();
        for (int i = 1; i < paths.length; ++i) {
            if (!paths[i].getGraph().getMapId().equals(mapId)) {
                throw new IllegalArgumentException(
                        "Cannot concatenate paths from different graphs.");
            }
        }
        ArrayList<Arc> arcs = new ArrayList<>();
        for (Path path: paths) {
            arcs.addAll(path.getArcs());
        }
        Path path = new Path(paths[0].getGraph(), arcs);
        if (!path.isValid()) {
            throw new IllegalArgumentException(
                    "Cannot concatenate paths that do not form a single path.");
        }
        return path;
    }

    // Graph containing this path.
    private final Graph graph;

    // Origin of the path
    private final Node origin;

    // List of arcs in this path.
    private final List<Arc> arcs;

    /**
     * Create an empty path corresponding to the given graph.
     * 
     * @param graph Graph containing the path.
     */
    public Path(Graph graph) {
        this.graph = graph;
        this.origin = null;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path containing a single node.
     * 
     * @param graph Graph containing the path.
     * @param node Single node of the path.
     */
    public Path(Graph graph, Node node) {
        this.graph = graph;
        this.origin = node;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path with the given list of arcs.
     * 
     * @param graph Graph containing the path.
     * @param arcs Arcs to construct the path.
     */
    public Path(Graph graph, List<Arc> arcs) {
        this.graph = graph;
        this.arcs = arcs;
        this.origin = arcs.size() > 0 ? arcs.get(0).getOrigin() : null;
    }

    /**
     * @return Graph containing the path.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @return First node of the path.
     */
    public Node getOrigin() {
        return origin;
    }

    /**
     * @return Last node of the path.
     */
    public Node getDestination() {
        return arcs.get(arcs.size() - 1).getDestination();
    }

    /**
     * @return List of arcs in the path.
     */
    public List<Arc> getArcs() {
        return Collections.unmodifiableList(arcs);
    }

    /**
     * Check if this path is empty (it does not contain any node).
     * 
     * @return true if this path is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.origin == null;
    }

    /**
     * Get the number of <b>nodes</b> in this path.
     * 
     * @return Number of nodes in this path.
     */
    public int size() {
        return isEmpty() ? 0 : 1 + this.arcs.size();
    }

    /**
     * Check if this path is valid.
     * 
     * A path is valid if any of the following is true:
     * <ul>
     * <li>it is empty;</li>
     * <li>it contains a single node (without arcs);</li>
     * <li>the first arc has for origin the origin of the path and, for two
     * consecutive arcs, the destination of the first one is the origin of the
     * second one.</li>
     * </ul>
     * 
     * @return true if the path is valid, false otherwise.
     * 
     */
    public boolean isValid() {
        // 1er cas de figure a considerer : Path Empty 
    	if (this.isEmpty()) {
    		return true ;
    	}
    	// un seul noeud : 
    	else if (this.size()==1) {
    		return true ; 
    	}
    	// au moins 2 noeuds dans le chemin
    	else {
    		Node origine = this.getOrigin();
    		for (Arc arc : this.arcs) {
    			if (!origine.equals(arc.getOrigin())) {
    				return false;
    			}
    			origine = arc.getDestination();
    		} 			
    	}
        return true;
    }

    /**
     * Compute the length of this path (in meters).
     * 
     * @return Total length of the path (in meters).
     * 
     */
    public float getLength() {
        // on declarer une variable float qui va etre incrementer et qui va retourner a la fin la longueur total
    	float lenghtot = 0.0f;
    	// on boucle pour incrémenter par rapport a notre arc 
    	for (Arc arc : this.arcs) {
    		lenghtot = lenghtot + arc.getLength();
    	}
        return lenghtot;
    }

    /**
     * Compute the time required to travel this path if moving at the given speed.
     * 
     * @param speed Speed to compute the travel time.
     * 
     * @return Time (in seconds) required to travel this path at the given speed (in
     *         kilometers-per-hour).
     */
    public double getTravelTime(double speed) {
        // déclaration d'une variable time qui va être retourner a la fin
    	double time = 0.0;
    	// on récuperer la longueur du path grâce a notre fonction getLenght()
    	float distance = getLength();
    	// on a besoin de convertir le parametre speed fourni a notre fonction ( sa me rappel mes anciens cours en prépa ) 
    	double vitesse_en_ms = speed * (1000.0/3600.0) ;
    	// et enfin on applique la regle d= t*v => t =d/v
    	time = distance / vitesse_en_ms ;
        return time;
    }

    /**
     * Compute the time to travel this path if moving at the maximum allowed speed
     * on every arc.
     * 
     * @return Minimum travel time to travel this path (in seconds).
     * 
     */
    public double getMinimumTravelTime() {
    	// pour les déclaration et l'incrémentation même raisonnement que getTravelTime
    	double time_min= 0 ;
    	for (Arc arc : this.arcs) {
    		time_min = time_min + arc.getMinimumTravelTime();
    	}
        return time_min;
    }

}
