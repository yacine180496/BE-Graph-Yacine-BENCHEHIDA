package org.insa.graphs.algorithm.shortestpath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;

import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;

import org.insa.graphs.model.io.BinaryGraphReader;
import java.io.DataInputStream;
import java.io.FileInputStream;

import org.junit.BeforeClass;
import org.junit.Test;



public class TestComparaison {
	// On va se servir de l'algorithme de BellmanFord comme référence : c'est notre oracle

		// Attributs 
		private static Path a, b, c, d, e, f ;
		private static AbstractSolution.Status emptyPathD, emptyPathA;
		private static AbstractSolution.Status nonexistentPathD, nonexistentPathA;

		@BeforeClass
		public static void initAll() throws IOException {
			// Test sur la carte carré
			// On récupère les données sur la carte carré
			FileInputStream input1 = new FileInputStream("D:/BE-Graph/BE-Graph-Yacine-BENCHEHIDA/Maps/carre.mapgr");
			
			// Contient des chemins inexistants
			FileInputStream input2 = new FileInputStream("D:/BE-Graph/BE-Graph-Yacine-BENCHEHIDA/Maps/guyane.mapgr");

			DataInputStream dataInput1 = new DataInputStream(input1);
			BinaryGraphReader binary1 = new BinaryGraphReader(dataInput1);
			Graph graph1 = binary1.read();
			binary1.close();

			DataInputStream dataInput2 = new DataInputStream(input2);
			BinaryGraphReader binary2 = new BinaryGraphReader(dataInput2);
			Graph graph2 = binary2.read();
			binary2.close();

			// On va créer des données 
			
			List<ArcInspector> Listeinspector = ArcInspectorFactory.getAllFilters();
			ShortestPathData data = new ShortestPathData(graph1, graph1.getNodes().get(3), graph1.getNodes().get(23),
					Listeinspector.get(0));
			ShortestPathData data2 = new ShortestPathData(graph1, graph1.getNodes().get(2), graph1.getNodes().get(2),
					Listeinspector.get(0));
			ShortestPathData data3 = new ShortestPathData(graph2, graph2.get(11872), graph2.get(579),
					Listeinspector.get(0));
			ShortestPathData data4 = new ShortestPathData(graph2, graph2.getNodes().get(63), graph2.getNodes().get(90),
					Listeinspector.get(0));

			// Après avoir créer des données : on peut lancer les algorithmes 
			
			DijkstraAlgorithm D1 = new DijkstraAlgorithm(data);
			a = D1.run().getPath();
			BellmanFordAlgorithm B1 = new BellmanFordAlgorithm(data);
			b = B1.run().getPath();
			AStarAlgorithm A1 = new AStarAlgorithm(data);
			c = A1.run().getPath();

			// Exécution de Dijkstra et AStar avec comme paramètre une longueur null pour un court chemin 
			
			DijkstraAlgorithm D2 = new DijkstraAlgorithm(data2);
			emptyPathD = D2.run().getStatus();
			AStarAlgorithm A2 = new AStarAlgorithm(data2);
			emptyPathA = A2.run().getStatus();

			// Exécution de Dijkstra et AStar avec comme paramètre chemin inexistant entre deux points 
			DijkstraAlgorithm D3 = new DijkstraAlgorithm(data3);
			nonexistentPathD = D3.run().getStatus();
			AStarAlgorithm A3 = new AStarAlgorithm(data3);
			nonexistentPathA = A3.run().getStatus();

			// Exécution de Dijkstra et AStar avec comme paramètre chemin existant entre deux points
			DijkstraAlgorithm D4 = new DijkstraAlgorithm(data4);
			d = D4.run().getPath();
			BellmanFordAlgorithm B4 = new BellmanFordAlgorithm(data4);
			e = B4.run().getPath();
			AStarAlgorithm A4 = new AStarAlgorithm(data4);
			f = A4.run().getPath();

		} 

		// court chemin existant, on compare la longueur obtenue du chemin  par Dijkstra et AStar la longueur obtenue du chemin  par Bellman-Ford 
		@Test
		public void Test1() {
			assertEquals((long) (b.getLength()), (long) (a.getLength()));
			assertEquals((long) (e.getLength()), (long) (d.getLength()));

		}

		// court chemin existant, on compare le temps obtenu du chemin par Dijkstra et AStar avec le temps obtenu du chemin par Bellman-Ford 
		@Test
		public void Test2() {
			assertEquals((long) (c.getMinimumTravelTime()), (long) (b.getMinimumTravelTime()));
			assertEquals((long) (b.getMinimumTravelTime()), (long) (a.getMinimumTravelTime()));
			assertEquals((long) (f.getMinimumTravelTime()), (long) (e.getMinimumTravelTime()));
			assertEquals((long) (e.getMinimumTravelTime()), (long) (d.getMinimumTravelTime()));

		}

		// Chemin de longueur nulle, on vérifie qu'on retourne bien status= " INFEASIBLE "
		@Test
		public void Test3() {
			assertTrue(emptyPathA.equals(AbstractSolution.Status.INFEASIBLE));
			assertTrue(emptyPathD.equals(AbstractSolution.Status.INFEASIBLE));
		}

		// Chemin inexistant, on vérifie qu'on retourne bien status= " INFEASIBLE "
		@Test
		public void Test4() {
			assertTrue(nonexistentPathD.equals(AbstractSolution.Status.INFEASIBLE));
			assertTrue(nonexistentPathA.equals(AbstractSolution.Status.INFEASIBLE));
		}

}
