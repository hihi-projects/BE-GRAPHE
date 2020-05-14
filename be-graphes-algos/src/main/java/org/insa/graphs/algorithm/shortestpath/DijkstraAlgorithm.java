package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.utils.*;
import org.insa.graphs.model.*;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import java.util.Collections;
import java.util.ArrayList;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
	// on fait une variable qui va stock notre cout final :FC
	private float FC; 
	
	// notre label doit être créer
	
	protected Label newLabel(Node node, ShortestPathData data) {
		return new Label(node); 
	} 
	
	// getteur de FC
	// retourner le cout final
	
	public float getCoutFinal() {  
	    	return this.FC;
	}

	//notre constructeur 
	
    public DijkstraAlgorithm(ShortestPathData data) { 
        super(data);
        this.FC = 0;
    } 
    

    protected ShortestPathSolution doRun() {
    	
    	// une variable data qui va contenir le graph avec le plus courts chemins
    	
        final ShortestPathData data = getInputData(); 
        
        // solution retourner a la fin de djik
        
        ShortestPathSolution sol = null; 
        
        // j'utilise un boolean pour m'arreter a la fin 
        
        boolean resultatFinal = false; 
        
        //déclaration du tas de labels
        
        BinaryHeap<Label> tasLabels = new BinaryHeap<Label>(); 
        
        // tab labels
        
        Label tabLabels[] = new Label[data.getGraph().size()]; 
        
        //initialisation
        
        tabLabels[data.getOrigin().getId()] = newLabel(data.getOrigin(), data);
        
        tasLabels.insert(tabLabels[data.getOrigin().getId()]);
        
        tabLabels[data.getOrigin().getId()].setIntas();
        
        tabLabels[data.getOrigin().getId()].setCout(0);
        
     // on annonce a l'observateur apres le premier évenemnt 
     // premiere event => origin traité
        
        notifyOriginProcessed(data.getOrigin());
        
       // on déclare un tab pour les prédec
        
        Arc[] predecessorArcs = new Arc[data.getGraph().size()]; 
       
       // on boucle tant que le taslab est non vide et rf false
        
        while((tasLabels.isEmpty() == false) && (resultatFinal == false)) {
        	Label current = tasLabels.deleteMin(); 
        	notifyNodeMarked(current.getNode());
        	current.setMarque();
        	
        	
        	if (current.getNode() == data.getDestination()) {// noeud current = noeud des
        		FC = current.getCout();
        		resultatFinal = true; 
        	}
        	
        	for(Arc a : current.getNode().getSuccessors()) {
        		
        		if (!data.isAllowed(a)) continue;
        		
        		if (tabLabels[a.getDestination().getId()] == null) {
        			notifyNodeReached(a.getDestination());
        			tabLabels[a.getDestination().getId()] = newLabel(a.getDestination(), data); 
        		}

        		if (tabLabels[a.getDestination().getId()].getMarque() == false) {
        			
        			if (tabLabels[a.getDestination().getId()].getCout() > current.getCout() + data.getCost(a)) {
        				tabLabels[a.getDestination().getId()].setCout(current.getCout() + (float) data.getCost(a));
        				
        				tabLabels[a.getDestination().getId()].setFather(current.getNode());
        				
        				if (tabLabels[a.getDestination().getId()].getIntas()) {
        					tasLabels.remove(tabLabels[a.getDestination().getId()]);	
        				}
        				else tabLabels[a.getDestination().getId()].setIntas();
        					
        				tasLabels.insert(tabLabels[a.getDestination().getId()]);
        				
        				predecessorArcs[a.getDestination().getId()] = a; 
        			}
        			
        		}
        		
        	}
        	
        }
        
        // imaginons on a pas de préd pour la destination 
        // la sol sera impo 
        
        if (predecessorArcs[data.getDestination().getId()] == null) {
			sol = new ShortestPathSolution(data, Status.INFEASIBLE);
		} else {
			
			// sinon on l'a et on notify l'observ
			
			notifyDestinationReached(data.getDestination());

			ArrayList<Arc> arcs = new ArrayList<Arc>();	// a partir du array pred on creer le chemin
			Arc arc = predecessorArcs[data.getDestination().getId()]; // chemin du tab de préd creer

			while (arc != null) {
				arcs.add(arc);
				arc = predecessorArcs[arc.getOrigin().getId()];
			}
			
			 // reverse les chemins
			
			Collections.reverse(arcs);
			
			//la solution finale sera : 
			
			sol = new ShortestPathSolution(data, Status.OPTIMAL, new Path(data.getGraph(), arcs));
			
		}
        
        return sol;
    }

}

