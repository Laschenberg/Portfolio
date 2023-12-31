import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class Main {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws InterruptedException, IOException {
		int numRuns = 64;
		int numThreads = 16; // Has to be an even divisor of numRuns.
		int numTests = 5;

		int popCount; // Ideal found with guess/check: 100
		float crossRate; // Typical is 0.6 to 0.8. Ideal found with guess/check: 0.6f
		float mutRate; // Typical is 0.001 to 0.005. Ideal found with guess/check: 0.5f
		int mutCount;
		int contestants;
		int newCount;
		int eliteCount;

		ArrayList<Integer>[] genCounts = new ArrayList[numTests];
		ArrayList<Integer>[] times = new ArrayList[numTests];
		ArrayList<Integer>[] runIDs = new ArrayList[numTests];
		Thread[] threads = new Thread[numThreads];
		GA[] groups = new GA[numThreads];

		for (int test = 0; test < numTests; test++) {
			genCounts[test] = new ArrayList<Integer>();
			times[test] = new ArrayList<Integer>();
			runIDs[test] = new ArrayList<Integer>();

			popCount = 50;
			crossRate = 0.6f;
			mutRate = 1f;
			mutCount = 1;
			contestants = 6;
			newCount = 0;
			eliteCount = 7;

			int numCities = 30;

			switch (test) {
			case 0:				//Test with optimal parameters(above) on population sizes
				numCities = 10;
				break;
			case 1:
				numCities = 15;
				break;
			case 2:
				numCities = 20;
				break;
			case 3:
				numCities = 25;
				break;
			case 4:
				numCities = 30;
				break;
			case 5: // If used, please wait 30-40 seconds for all runs to complete. I run an average
					// of 5-10 seconds per run, with 16 threads, 64 runs should take four times that.
				numCities = 40;
				break;
			case 6:	//If used, please wait seconds for all runs to complete. I run an average 
					//of 
				numCities = 45;
				break;
//			case 0:	//Test for Population Size
//				popCount = 30;
//				numCities = 20;
//				break;
//			case 1:
//				popCount = 40;
//				numCities = 20;
//				break;
//			case 2:
//				popCount = 50;
//				numCities = 20;
//				break;
//			case 3:
//				popCount = 60;
//				numCities = 20;
//				break;
//			case 0:	//Test for Crossover Rate
//				crossRate = 0.4f;
//				break;
//			case 1:
//				crossRate = 0.6f;
//				break;
//			case 2:
//				crossRate = 0.7f;
//				break;
//			case 3:
//				crossRate = 0.8f;
//				break;
//			case 4:
//				crossRate = 1f;
//				break;
//			case 0:	//Test for Mutation Rate
//				numCities = 20;
//				mutRate = 1f;
//				mutCount = 1;
//				break;
//			case 1:
//				numCities = 20;
//				mutRate = 1f;
//				mutCount = 1;
//				break;
//			case 2:	//worst
//				numCities = 20;
//				mutRate = 0.5f;
//				mutCount = 2;
//				break;
//			case 3://Second worst
//				numCities = 20;
//				mutRate = 0.8f;
//				mutCount = 1;
//				break;
//			case 0:
//				contestants = 5;
//				break;
//			case 1:	//Best in time, but overall this is scattered.
//				contestants = 6;
//				break;
//			case 2:
//				contestants = 7;
//				break;
//			case 3:
//				contestants = 8;
//				break;
//			case 4:
//				contestants = 10;
			}

			MapBuilder.presetMap(numCities);
			double[][] cityDistances = new double[numCities][numCities];
			File file = new File("./src/cityDistances.txt");
			Scanner sc = new Scanner(file);
			while (sc.hasNext()) {
				cityDistances[sc.nextInt()][sc.nextInt()] = sc.nextDouble();
			}
			sc.close();
			double maxFitness = MapBuilder.minCost * numCities; // ideal shortest distance for our contrived map

			// Serial Runs

//		for (int run = 0; run < numRuns; run++) {
//			GA pop = new GA(popCount, eliteCount, crossRate, mutRate, contestants,newCount);
//			// System.out.println("Run " + run + " started");
//			pop.run();
//			genCounts.add(pop.getGenerationCount());
//			System.out.println("Run #" + run + "-- Solution:" + pop.solution() + " Generations:" + genCounts.get(run));
//		}

			// Parallelized Runs
			for (int run = 0; run < numRuns; run += numThreads) {
				for (int group = 0; group < numThreads; group++) { // Run numThreads runs in parallel
					groups[group] = new GA(popCount, eliteCount, crossRate, mutRate, contestants, newCount, mutCount,
							cityDistances, maxFitness, numCities);
					threads[group] = new Thread(groups[group]);
					threads[group].start();
				}
				for (int group = 0; group < numThreads; group++) {
					threads[group].join();
					genCounts[test].add(groups[group].getGenerationCount());
					times[test].add(groups[group].getTime());
					runIDs[test].add(run + group);
					if (run == 0) {
						System.out.println("Run #" + (run + group) + " | Solution: " + groups[group].solution()
								+ " | Generations: " + groups[group].getGenerationCount() + " | Time: "
								+ groups[group].getTime());
					}
				}
			}
			System.out.println("\nNumber of Cities:" + numCities + ", Population: " + popCount + ", Elite:" + eliteCount
					+ ", Crossover: " + crossRate + ", Mutation:" + mutRate + ", New:" + newCount + ", Contestants:"
					+ contestants);
			// System.out.println("Time Elapsed (milliseconds): " + (end - start));
			int generations = (int) avgArray(genCounts[test]);
			double stdDevGens = stdDevArray(times[test],generations);
			int avgTime = (int) avgArray(times[test]);
			double stdDevTime = stdDevArray(times[test],avgTime);
			System.out.println("Average generations: " + generations);
			System.out.println("Standard Deviation Generations: " + stdDevGens);
			System.out.println("Average Time: " + avgTime);
			System.out.println("Standard Deviation Time: " + stdDevTime);
		}
		// Print Stats
		
		Visualizer.visualize(runIDs, genCounts, "Run", "Gen Solution Found");
		Visualizer.visualize(runIDs, times, "Run", "Time Solution Found");
	}

	public static void printArr(ArrayList<Integer> arr) {
		for (int i = 0; i < arr.size(); i++) {
			System.out.print(arr.get(i) + ", ");
		}
		System.out.println();
	}

	public static float avgArray(ArrayList<Integer> arr) {
		int sum = 0;
		for (int i = 0; i < arr.size(); i++) {
			sum += arr.get(i);
		}
		return sum / arr.size();
	}
	public static double stdDevArray(ArrayList<Integer> arr, int mean) {
		int sum = 0;
		for (int i = 0; i < arr.size(); i++) {
			sum += Math.pow(mean - arr.get(i),2);
		}
		return Math.sqrt(sum / arr.size());
	}
}
