package TestPcc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.junit.Test;



public class TestPcc {
	// On utilise l'algorithme de BellmanFord car il est deja codé est mis a notre disposition		
		private static Path chemin1, chemin2, chemin3, chemin4, chemin5, chemin6;
		private static AbstractSolution.Status PathA, PathB;
		private static AbstractSolution.Status nonePathA, nonePathB;


		public static void initAll() throws IOException {
			// on commence par tester sur notre carte carré
			FileInputStream File1 = new FileInputStream("C:/Users/xel-h/Desktop/Maps/extras/carre.mapgr");
			// ici on considere les cas ou on a pas de chemin existant
			FileInputStream File2 = new FileInputStream("C:/Users/xel-h/Desktop/Maps/europe/france/midi-pyrenees.mapgr");
			DataInputStream dt1 = new DataInputStream(File1);
			BinaryGraphReader bin1 = new BinaryGraphReader(dt1);
			Graph graphA = bin1.read();
			bin1.close();

			DataInputStream dt2 = new DataInputStream(File2);
			BinaryGraphReader bin2 = new BinaryGraphReader(dt2);
			Graph graphB = bin2.read();
			bin2.close();

			// data
		
			List<ArcInspector> Listeinspector = ArcInspectorFactory.getAllFilters();
			ShortestPathData data = new ShortestPathData(graphA, graphA.getNodes().get(1), graphA.getNodes().get(27),
					Listeinspector.get(0));
			ShortestPathData data2 = new ShortestPathData(graphA, graphA.getNodes().get(1), graphA.getNodes().get(1),
					Listeinspector.get(0));
			ShortestPathData data3 = new ShortestPathData(graphB, graphB.get(11700), graphB.get(676),
					Listeinspector.get(0));
			ShortestPathData data4 = new ShortestPathData(graphB, graphB.getNodes().get(83), graphB.getNodes().get(90),
					Listeinspector.get(0));

			// Algo 
			
			DijkstraAlgorithm D1 = new DijkstraAlgorithm(data);
			chemin1 = D1.run().getPath();
			BellmanFordAlgorithm B1 = new BellmanFordAlgorithm(data);
			chemin2 = B1.run().getPath();
			AStarAlgorithm A1 = new AStarAlgorithm(data);
			chemin3 = A1.run().getPath();

			// algo chemin nul
			DijkstraAlgorithm D2 = new DijkstraAlgorithm(data2);
			PathA = D2.run().getStatus();
			AStarAlgorithm A2 = new AStarAlgorithm(data2);
			PathB = A2.run().getStatus();

			// algo, chemin pas dispo
			DijkstraAlgorithm D3 = new DijkstraAlgorithm(data3);
			nonePathA = D3.run().getStatus();
			AStarAlgorithm A3 = new AStarAlgorithm(data3);
			 nonePathB = A3.run().getStatus();

			// algo, chemin existant
			DijkstraAlgorithm D4 = new DijkstraAlgorithm(data4);
			chemin4 = D4.run().getPath();
			BellmanFordAlgorithm B4 = new BellmanFordAlgorithm(data4);
			chemin5 = B4.run().getPath();
			AStarAlgorithm A4 = new AStarAlgorithm(data4);
			chemin6 = A4.run().getPath();

		} 

		//on verifier la longueure dans le cas ou on a un chemin court 
		// on utilises bellman ford
		@Test
		public void Test1() {
			/*assertEquals((long) (shortPathA.getLength()), (long) (shortPathB.getLength()));*/
			assertEquals((long) (chemin2.getLength()), (long) (chemin1.getLength()));
			/*assertEquals((long) (shortPathA4.getLength()), (long) (shortPathB4.getLength()));*/
			assertEquals((long) (chemin5.getLength()), (long) (chemin6.getLength()));

		}

		//  vérification du temps avec Bellman ford si PCC dispo
		@Test
		public void Test2() {
			assertEquals((long) (chemin3.getMinimumTravelTime()), (long) (chemin2.getMinimumTravelTime()));
			assertEquals((long) (chemin2.getMinimumTravelTime()), (long) (chemin1.getMinimumTravelTime()));
			assertEquals((long) (chemin6.getMinimumTravelTime()), (long) (chemin5.getMinimumTravelTime()));
			assertEquals((long) (chemin5.getMinimumTravelTime()), (long) (chemin4.getMinimumTravelTime()));

		}

		//Path avec longueur nulle
		// on verifie status
		@Test
		public void Test3() {
			assertTrue(PathB.equals(AbstractSolution.Status.INFEASIBLE));
			assertTrue(PathA.equals(AbstractSolution.Status.INFEASIBLE));
		}

		// no path
		// on verifie chemin ici
		@Test
		public void Test4() {
			assertTrue(nonePathA.equals(AbstractSolution.Status.INFEASIBLE));
			assertTrue(nonePathB.equals(AbstractSolution.Status.INFEASIBLE));
		}

}
