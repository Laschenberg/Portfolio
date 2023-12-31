import java.util.Arrays;
import java.util.Random;

public class GA implements Runnable {
	int generation;
	Chromosome[] population;
	boolean sorted;
	Chromosome optInd;
	long startTime;

	static double[][] distances = null;

	int popCount;
	int eliteCount;
	float crossRate;
	float mutRate;
	int mutCount;
	int contestants;
	int newCount;
	int numCities;
	double maxFitness;

	public GA(int popCount, int eliteCount, float crossRate, float mutRate, int contestants, int newCount, int mutCount,
			double[][] distances, double maxFitness, int numCities) {
		this.popCount = popCount;
		this.eliteCount = eliteCount;
		this.crossRate = crossRate;
		this.mutRate = mutRate;
		this.contestants = contestants;
		this.newCount = newCount;
		this.mutCount = mutCount;
		this.numCities = numCities;
		this.maxFitness = maxFitness;
		
		sorted = false;
		population = new Chromosome[popCount];
		for (int i = 0; i < popCount; i++) {
			population[i] = new Chromosome(numCities);
		}
		GA.distances = distances;
	}

	// Main Functions------------------------------------------------

	public void run() {
		startTime = System.currentTimeMillis();
		boolean optimal = false;
		while (!optimal) {
			for (int i = 0; i < popCount; i++) {
				if (population[i].getFitness(GA.distances) <= maxFitness) {
					optimal = true;
					optInd = population[i];
					break;
				}
			}
			if (generation % 10 == 0) {
				//System.out.println(generation + ": " + solution() + "; " +  solution().getFitness(GA.getDistances()));
//				try {
//					printPop();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			}
			// System.out.println("\n\nGeneration :" + generation);
			population = nextGeneration();
			generation++;
		}
	}

	public Chromosome[] getElite() {
		sortPop();
		Chromosome[] elite = new Chromosome[eliteCount];
		for (int i = 0; i < eliteCount; i++) {
			elite[i] = population[i];
		}
		return elite;
	}

	public Chromosome[] nextGeneration() {
		Random rand = new Random();
		Chromosome[] newPop = new Chromosome[popCount];
		Chromosome[] elite = getElite();

		for (int i = 0; i < popCount - eliteCount - newCount; i++) {
			
			// Select
			float val = rand.nextFloat();
			Chromosome parent = tournySelect();
			if (val < crossRate) {
				 //System.out.println("Breeding!");
				Chromosome parent2 = tournySelect();
				newPop[i] = parent.breedWith(parent2);
			} else {
				newPop[i] = parent;
			}

			// Mutate
			val = rand.nextFloat();
			if (val < mutRate) {
				// System.out.println("Mutating!");
				newPop[i] = newPop[i].mutate(mutCount);
			}
		}
		for (int i = popCount - eliteCount - newCount; i < popCount - eliteCount; i++) {
			newPop[i] = new Chromosome(numCities);
		}
		for (int i = popCount - eliteCount; i < popCount; i++) {
			newPop[i] = elite[i - (popCount - eliteCount)];
			// System.out.println("Elite: " + newPop[i]);
		}
		sorted = false;
		return newPop;
	}

	public static double[][] getDistances() {
		return GA.distances;
	}

	// Helper Functions ------------------------------------------------

	private void sortPop() {
		if (!sorted) {
			Arrays.sort(population);
		}
		sorted = true;
	}

	public int getGenerationCount() {
		return generation;
	}

	public Chromosome solution() {
		sortPop();
		return population[0];
	}

	public int getTime() {
		return (int) (System.currentTimeMillis() - startTime);
	}

	// Parent Selection Functions------------------------------------------
	@SuppressWarnings("unused")
	private Chromosome tournySelect() {
		sortPop();
		Random rand = new Random();
		int min = 2147483647;
		for (int i = 0; i < contestants; i++) {
			int contestant = rand.nextInt(popCount);
			if (contestant < min) {
				min = contestant;
			}
		}
		return population[min];
	}

	private Chromosome rankSelect() { // Ranked Selection
		sortPop();
		Random rand = new Random();
		int totalRank = (int) ((popCount - 1 + Math.pow(popCount - 1, 2)) / 2);
		int val = rand.nextInt(totalRank);
		int curSum = 0;
		int i = 0;
		while (curSum < val) {
			curSum += i;
			if (curSum >= val)
				return population[popCount - 1 - i];
			i++;
		}
		if (curSum >= val)
			return population[popCount - 1 - i];
		System.out.println("Error: " + curSum + ", " + val);
		return null;
	}
	
	public void printPop() throws InterruptedException {
		sortPop();
		for (int i = 0; i < popCount; i++) {
			System.out.println(population[i] + "  " + population[i].getFitness(GA.distances));
		}
		System.out.println();
		System.out.println();
		Thread.sleep(1000);
	}

//	public void printPop() throws InterruptedException {
//		sortPop();
//		for (int i = 0; i < popCount; i++) {
//			System.out.println(population[i] + "  " + population[i].getFitness(GA.getDistances()));
//		}
//		System.out.println();
//		Thread.sleep(1000);
//	}

}
