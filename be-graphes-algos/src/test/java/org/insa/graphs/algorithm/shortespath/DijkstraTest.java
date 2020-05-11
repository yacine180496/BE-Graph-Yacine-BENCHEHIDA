package org.insa.graphs.algorithm.shortespath;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.insa.graphs.model.Node;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Arc; 
import org.insa.graphs.model.RoadInformation; 

import org.insa.graphs.algorithm.shortestpath.*;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.insa.graphs.algorithm.ArcInspector; 
import org.insa.graphs.algorithm.ArcInspectorFactory; 

import org.junit.BeforeClass;
import org.junit.Test;

import org.insa.graphs.model.io.*; 

public class DijkstraTest {
	
	// Small graph use for tests
	private static Graph graph;

	// List of nodes
	private static Node[] nodes;
	
	private String mapsLocation = "D:\\BE-Graph\\BE-Graph-Yacine-BENCHEHIDA\\Maps";
	
	// List of arcs in the graph, a2b is the arc from node A (0) to B (1).
	@SuppressWarnings("unused")
	private static Arc a2b, a2c, b2d, b2e, b2f, c2a, c2b, c2f, e2c, e2d, e2f, f2e;
	
	 @BeforeClass
	    public static void initAll() throws IOException {

	    	RoadInformation roadInformation = new RoadInformation(RoadType.UNCLASSIFIED, null, true, 1, null);
	    	
	    	// Create nodes
			nodes = new Node[6];
			for (int i = 0; i < nodes.length; ++i) {
				nodes[i] = new Node(i, null);
			}
			
			// Add arcs...
			a2b = Node.linkNodes(nodes[0], nodes[1], 7, roadInformation, null);
			a2c = Node.linkNodes(nodes[0], nodes[2], 8, roadInformation, null);
			b2d = Node.linkNodes(nodes[1], nodes[3], 4, roadInformation, null);
			b2e = Node.linkNodes(nodes[1], nodes[4], 1, roadInformation, null);
			b2f = Node.linkNodes(nodes[1], nodes[5], 5, roadInformation, null);
			c2a = Node.linkNodes(nodes[2], nodes[0], 7, roadInformation, null);
			c2b = Node.linkNodes(nodes[2], nodes[1], 2, roadInformation, null);
			c2f = Node.linkNodes(nodes[2], nodes[5], 2, roadInformation, null);
			e2c = Node.linkNodes(nodes[4], nodes[2], 2, roadInformation, null);
			e2d = Node.linkNodes(nodes[4], nodes[3], 2, roadInformation, null);
			e2f = Node.linkNodes(nodes[4], nodes[5], 3, roadInformation, null);
			f2e = Node.linkNodes(nodes[5], nodes[4], 3, roadInformation, null);

			// Initialize the graph
			graph = new Graph("ID", "", Arrays.asList(nodes), null);
	 }
	 
	//@Test
		public void testDoRun() {
			System.out.println("#####-----Test de validité avec oracle sur un exemple simple-----#####");
			/* Tableau contenant les arcs*/
			//Arc[] arcs = new Arc[] { a2b, a2c, b2d, b2e, b2f, c2a, c2b, c2f, e2c, e2d, e2f, f2e };

			for (int i=0;  i < nodes.length; ++i) {

				/* Affichage du point de départ */
				System.out.print("x"+(nodes[i].getId()+1) + ":");

				for (int j=0;  j < nodes.length; ++j) {

					if(nodes[i]==nodes[j]) {
						System.out.print("     -    ");
					}
					else{

						ArcInspector arcInspectorDijkstra = new ArcInspectorFactory().getAllFilters().get(0);
						ShortestPathData data = new ShortestPathData(graph, nodes[i],nodes[j], arcInspectorDijkstra);

						BellmanFordAlgorithm B = new BellmanFordAlgorithm(data);
						DijkstraAlgorithm D = new DijkstraAlgorithm(data);

						/* Récupération des solutions de Bellman et Dijkstra pour comparer */
						ShortestPathSolution solution = D.run();
						ShortestPathSolution expected = B.run();

						/* Pas de chemin trouvé */
						if (solution.getPath() == null) {
							assertEquals(expected.getPath(), solution.getPath());
							System.out.print("(infini)  ");
						}
						/* Un plus court chemin trouvé */
						else {

							/* Calcul du coût de la solution */
							float costSolution = solution.getPath().getLength();
							float costExpected = expected.getPath().getLength();
							assertEquals(costExpected, costSolution, 0);

							/* On récupère l'avant dernier sommet du chemin de la solution (=sommet père de la destination) */
							List<Arc> arcs = solution.getPath().getArcs();
							Node originOfLastArc = arcs.get(arcs.size()-1).getOrigin();

							/* Affiche le couple (coût, sommet père du Dest) */
							System.out.print("("+costSolution+ ", x" + (originOfLastArc.getId()+1) + ") ");
						}
						
					}

				}

				/* Retour à la ligne */ 
				System.out.println();

			}
			System.out.println();
		}
		
		 //procedure generale permettant de tester differents scenarios sans comparer
	   	public void testScenario1(String mapName, int typeEvaluation, int origine, int destination) throws Exception{
	   		//typeEvaluation : 0 = temps, 1 = distance
	   		
	           // Create a graph reader.
	           GraphReader reader = new BinaryGraphReader(
	                   new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
	           // Read the graph.
	           Graph graph = reader.read();
	           
	           if ((typeEvaluation != 0 && typeEvaluation != 1) || origine < 0 || origine >= graph.size() || destination < 0 || destination >= graph.size()) {
	           	System.out.println("Arguments non valides");
	           	throw new Exception();
	           }else {
	           	System.out.println("Origine : " + origine);
	           	System.out.println("Destination : " + destination);
	           	//si l'origine et la destination sont les mêmes sommets 
	   			if (origine == destination) {
	   				System.out.print("Origine = Destination, cout nul");
	   			}else {
	   	        	ArcInspector arcInspector;
	   	        	//0 = temps, 1 = distance
	   	        	if (typeEvaluation == 0) {
	   	        		arcInspector = new ArcInspectorFactory().getAllFilters().get(2);
	   	        	}else {
	   	        		arcInspector = new ArcInspectorFactory().getAllFilters().get(0);
	   	        	}
	   				ShortestPathData data = new ShortestPathData(graph,graph.get(origine),graph.get(destination),arcInspector);
	   				
	   		
	   				DijkstraAlgorithm dijkstraAlgo = new DijkstraAlgorithm(data);
	   				
	   				
	   				ShortestPathSolution solutionDijkstra = dijkstraAlgo.run();
	   				
	   				//si le path de la solution n'existe pas alors il n'y pas de solution
	   				if (solutionDijkstra.getPath() == null) {
	   					System.out.println("Pas de solution");
	   				}else {
	   					//on vérifie que les couts sont egaux
	   					double costDijkstra;
	   					if (typeEvaluation == 0) { //temps
	   						costDijkstra = solutionDijkstra.getPath().getMinimumTravelTime();
	   					}else { //distance
	   						costDijkstra = solutionDijkstra.getPath().getLength();
	   					}
	   					assertTrue(solutionDijkstra.getPath().isValid());
	   					System.out.println("Cout de la solution : " + costDijkstra);	
	   				}
	   			}
	           }
	           System.out.println();
	   	}
	   	
	   	
	  //procedure generale permettant de tester differents scenarios en comparant avec bellman
		public void testScenario2(String mapName, int typeEvaluation, int origine, int destination) throws Exception{
			//typeEvaluation : 0 = temps, 1 = distance
			
	        // Create a graph reader.
	        GraphReader reader = new BinaryGraphReader(
	                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
	        // Read the graph.
	        Graph graph = reader.read();
	        
	        if ((typeEvaluation != 0 && typeEvaluation != 1) || origine < 0 || origine >= graph.size() || destination < 0 || destination >= graph.size()) {
	        	System.out.println("Arguments non valides");
	        	throw new Exception();
	        }else {
	        	System.out.println("Origine : " + origine);
	        	System.out.println("Destination : " + destination);
	        	//si l'origine et la destination sont les mêmes sommets 
				if (origine == destination) {
					System.out.print("Origine = Destination, cout nul");
				}else {
		        	ArcInspector arcInspector;
		        	//0 = temps, 1 = distance
		        	if (typeEvaluation == 0) {
		        		arcInspector = new ArcInspectorFactory().getAllFilters().get(2);
		        	}else {
		        		arcInspector = new ArcInspectorFactory().getAllFilters().get(0);
		        	}
					ShortestPathData data = new ShortestPathData(graph,graph.get(origine),graph.get(destination),arcInspector);
					
					BellmanFordAlgorithm bellmanAlgo = new BellmanFordAlgorithm(data);
					DijkstraAlgorithm dijkstraAlgo = new DijkstraAlgorithm(data);
					
					ShortestPathSolution solutionBellman = bellmanAlgo.run();
					ShortestPathSolution solutionDijkstra = dijkstraAlgo.run();
					
					//si le path de la solution n'existe pas alors il n'y pas de solution
					if (solutionDijkstra.getPath() == null) {
						assertEquals(solutionBellman.getPath(),solutionDijkstra.getPath());
						System.out.println("Pas de solution");
					}else {
						//on vérifie que les couts sont egaux
						double costBellman;
						double costDijkstra;
						if (typeEvaluation == 0) { //temps
							costBellman = solutionBellman.getPath().getMinimumTravelTime();
							costDijkstra = solutionDijkstra.getPath().getMinimumTravelTime();
						}else { //distance
							costBellman = solutionBellman.getPath().getLength();
							costDijkstra = solutionDijkstra.getPath().getLength();
						}
						assertEquals(costBellman,costDijkstra,0.001);
						System.out.println("Cout de la solution : " + costDijkstra);	
					}
				}
	        }
	        System.out.println();
		}
		
		//Tests d’optimalité avec oracle
		//@Test
		public void testTempsCarreDense() throws Exception{
			System.out.println("--Test--");
			System.out.println("--Map : Carre Dense--");
			System.out.println("--Type Evaluation : Temps--");
			
			String mapName = mapsLocation + "/carre-dense.mapgr";
			int origine = 20;
			int destination = 1550;
			
			testScenario2(mapName,0,origine,destination);
		}
		
		@Test
		public void testTempsCarreDenseCheminNul() throws Exception{
			System.out.println("--Test--");
			System.out.println("--Map : Carre Dense--");
			System.out.println("--Type Evaluation : Temps--");
			
			String mapName = mapsLocation + "/carre-dense.mapgr";
			int origine = 0;
			int destination = 0;
			
			testScenario2(mapName,0,origine,destination);
		}

		@Test
		public void testTempsGuadeloupeNonExistant() throws Exception{
			System.out.println("--Test--");
			System.out.println("--Map : La Guadeloupe --");
			System.out.println("--Type Evaluation : Temps--");
			
			String mapName = mapsLocation + "/guadeloupe.mapgr";
			int origine = 32934;
			int destination = 16045;
			
			testScenario2(mapName,0,origine,destination);
		}
		
		@Test
		public void testDistanceGuadeloupe() throws Exception{
			System.out.println("--Test--");
			System.out.println("--Map : La Guadeloupe --");
			System.out.println("--Type Evaluation : Distance--");
			
			String mapName = mapsLocation + "/guadeloupe.mapgr";
			int origine = 29344;
			int destination = 13911;
			
			testScenario2(mapName,1,origine,destination);
		}

		public void testAleatoireTempsToulouse() throws Exception{
			System.out.println("--Test--");
			System.out.println("--Map : Toulouse--");
			System.out.println("--Type Evaluation : Temps--");
			
			String mapName = mapsLocation + "/toulouse.mapgr";
			double max = 35000;
			double min = 0;
			
			int origine = (int)(Math.random() * (max - min));
			int destination = (int)(Math.random() * (max - min));
			
			testScenario2(mapName,0,origine,destination);
		}
		
		public void testAleatoireDistanceToulouse() throws Exception{
			System.out.println("--Test--");
			System.out.println("--Map : Toulouse--");
			System.out.println("--Type Evaluation : Distance--");
			
			String mapName = mapsLocation + "/toulouse.mapgr";
			double max = 35000;
			double min = 0;
			
			int origine = (int)(Math.random() * (max - min));
			int destination = (int)(Math.random() * (max - min));
			
			testScenario2(mapName,1,origine,destination);
		}
		
		@Test
		public void testsAleatoiresToulouse() throws Exception{
			for (int i = 0; i < 5; i++) {
				testAleatoireTempsToulouse();
			}
			for (int i = 0; i < 5; i++) {
				testAleatoireDistanceToulouse();
			}
		}
	
}
