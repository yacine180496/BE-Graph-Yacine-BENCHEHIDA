package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.utils.*;

import org.insa.graphs.algorithm.AbstractSolution.Status;

import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path ; 


import java.util.*;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    @Override
    
    protected ShortestPathSolution doRun() {
        
    	final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        
        // TODO:
        
/*------------------Initialisation----------------------------------*/
        
        Graph graph = data.getGraph(); // On récupère le graphe 
        List<Node>	nodes = graph.getNodes(); // 
        final int nbNodes = graph.size();
        
        BinaryHeap<Label> labelHeap = new BinaryHeap<Label>();
        Label[] tabLabel = new Label[nbNodes]; 
        
        // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[nbNodes];
        
        int initId =  data.getOrigin().getId(); 
        tabLabel[initId]= this.newLabel(data.getOrigin(), data);
        
        tabLabel[initId].setCost(0);                  // Cost(s) <- O ; 
        labelHeap.insert(tabLabel[initId]);           // Insert(s,tas) ; 
        
     // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());
        
      
        
/*-----Corps-du-programme-------------------------------------------------------------*/
      
        Label auxNode ;
        boolean found = false ;
        
        while(!labelHeap.isEmpty()&&!found) {
        	auxNode = labelHeap.deleteMin() ; 
        	auxNode.setMark(); 
        	
        	if(auxNode.getCurrent_NodeId()==data.getDestination().getId()) {
        		found = true ; 
        	}
        	
        	for(Arc a : nodes.get(auxNode.getCurrent_NodeId()).getSuccessors()) {
        		
        		if(!data.isAllowed(a)) {
        			continue;
        		}
        		
        		int nodeId = a.getDestination().getId(); 
        		
        		if(tabLabel[nodeId]==null) {
        			tabLabel[nodeId] = this.newLabel(a.getDestination(), data);
        		}
        		
        		if (!tabLabel[nodeId].getMark()) {
        			
        			double w = data.getCost(a);
                    double oldDistance = tabLabel[nodeId].getTotalCost();
                    double newDistance = tabLabel[auxNode.getCurrent_NodeId()].getTotalCost() + w;
                    if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                        notifyNodeReached(a.getDestination());
                    }
                    
                    if (newDistance <oldDistance) {
                    	if(!tabLabel[nodeId].isAux()) {
                    		tabLabel[nodeId].setAux();
            			}else {
            				labelHeap.remove(tabLabel[nodeId]);
            			}
                    	tabLabel[nodeId].setCost(tabLabel[nodeId].getCost()+w); 
                    	tabLabel[nodeId].setFather(auxNode.getCurrent_NodeId());
                        labelHeap.insert(tabLabel[nodeId]);
                        predecessorArcs[a.getDestination().getId()] = a;
                    }else if(newDistance==oldDistance) {
                    	
                    }
        		}
        	}
        	
        }
        
     // Destination has no predecessor, the solution is infeasible...
        if (predecessorArcs[data.getDestination().getId()] == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
            System.out.println("Le nombre d'arcs du PCC: 0");
        }
        else {

            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = predecessorArcs[data.getDestination().getId()];
            while (arc != null) {
                arcs.add(arc);
                arc = predecessorArcs[arc.getOrigin().getId()];
            }
            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }
  
        
        return solution ;
   
    }
    
    
    

	public Label newLabel(Node node, ShortestPathData data ) {
		return new Label(node);
	}

}
