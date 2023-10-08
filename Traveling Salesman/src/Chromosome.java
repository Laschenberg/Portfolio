import java.util.Random;
import java.util.HashSet;
import java.util.stream.IntStream;
import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("unused")
public class Chromosome implements Comparable<Object> {
	int routeLength;
	int[] route;

	long fitness = 0;

	public Chromosome(int numCities) {
		routeLength = numCities;
		route = new int[routeLength];
		for (int i = 0; i < routeLength; i++) {
			route[i] = -1;
		}
		Random rand = new Random();
		for (int i : IntStream.range(0, routeLength).toArray()) {
			int index = 0;
			while (true) {
				index = rand.nextInt(routeLength);
				if (route[index] == -1)
					break;
			}
			route[index] = i;
		}
		fitness = -1;
	}

	public Chromosome(int numCities, int[] route) {
		routeLength = numCities;
		this.route = new int[routeLength];
		for (int i = 0; i < routeLength; i++) {
			this.route[i] = route[i];
		}
		fitness = -1;
	}

	public double getFitness(double[][] distances) {
		if (fitness == -1)
			return calcFitness(distances);
		return fitness;
	}

	public double calcFitness(double[][] distances) {
		long sum = 0;
		for (int i = 0; i < distances.length - 1; i++) {
			sum += distances[route[i]][route[i + 1]];
		}
		sum += distances[route[distances.length - 1]][route[0]];
		fitness = sum;
		return fitness;
	}

	@Override
	public String toString() {
		String end = "";
		for (int i = 0; i < routeLength; i++) {
			end += route[i] + ", ";
		}
		return end;
	}

	@Override
	public int compareTo(Object input) {
		Chromosome other = (Chromosome) input;
		double thisFit = this.getFitness(GA.getDistances());
		double otherFit = other.getFitness(GA.getDistances());
		if (thisFit > otherFit) {
			return 1;
		} else if (thisFit < otherFit) {
			return -1;
		} else {
			return 0;
		}
	}

	public Chromosome mutate(int mutCount) {
		Random rand = new Random();
		int[] newRoute = new int[routeLength];
		for (int i = 0; i < routeLength; i++) {
			newRoute[i] = route[i];
		}
		for (int j = 0; j < mutCount; j++) {
			int point1 = rand.nextInt(routeLength);
			int point2 = rand.nextInt(routeLength);
			if (point2 > point1) {
				int[] rev = new int[point2 - point1];
				int count = rev.length;
				for (int i = point1; i < point2; i++) {
					count--;
					rev[count] = newRoute[i];
				}
				for (int i = point1; i < point2; i++) {
					newRoute[i] = rev[i - point1];
				}
			} else if (point1 > point2) {
				int[] rev = new int[point1 - point2];
				int count = rev.length;
				for (int i = point2; i < point1; i++) {
					count--;
					rev[count] = newRoute[i];
				}
				for (int i = point2; i < point1; i++) {
					newRoute[i] = rev[i - point2];
				}
			}
		}
		return new Chromosome(routeLength, newRoute);
	}

	private class Pair implements Comparable<Object> {
		public int one;
		public int two;

		public Pair(int o, int t) {
			one = o;
			two = t;
		}

		public Pair(Pair pair) {
			one = pair.one;
			two = pair.two;
		}

		public int other(int i) throws Exception {
			if (i == one) {
				return two;
			} else if (i == two) {
				return one;
			} else {
				throw new Exception();
			}
		}

		public boolean overlaps(Pair pair) {
			if (pair.one == one || pair.two == one || pair.one == two || pair.two == two) {
				return true;
			}
			return false;
		}

		public int min() {
			if (one > two) {
				return one;
			}
			return two;
		}

		@Override
		public int compareTo(Object o) {
			Pair other = (Pair) o;
			double thisMin = this.min();
			double otherMin = other.min();
			if (thisMin > otherMin) {
				return 1;
			} else if (thisMin < otherMin) {
				return -1;
			} else {
				return 0;
			}
		}

		public boolean equals(Object o) {
			Pair other = (Pair) o;
			if (other.one == this.one || other.two == this.one) {
				if (other.one == this.two || other.two == this.two) {
					return true;
				}
			}
			return false;
		}

		public String toString() {
			return "(" + one + ", " + two + ")";
		}

	}

	// 1:Find subRoutes(connections present in both parents)
	// 2:Start with a random subRoute, and find all consecutive subRoutes.
	// 3:Add another random subRoute when all consecutive subRoutes are gone.
	// 4:Repeat steps 2 and 3 until route is valid or there are no more unused
	// subRoutes.
	// 5:Add cities not in subroutes until route is valid.

	public Chromosome breedWith(Chromosome other) {
		ArrayList<Pair> subRoutes = getSubRoutes(other);
		int[] end = new int[routeLength];
		int left = routeLength - 1;
		int right = 0;
		Random rand = new Random();
		if (subRoutes.size() > 1) { // If there are any similar patterns between the parents, preserve them.
			Pair cur = subRoutes.get(0);
			end[left] = cur.one;
			end[right] = cur.two;
			subRoutes.remove(0);
			boolean overlapped = true;
			while (left - right > 2 && subRoutes.size() > 0) {
				// Add as many consecutive connections(pairs) to the list as possible
				while (overlapped) {
					overlapped = false;
					for (int i = 0; i < subRoutes.size(); i++) {
						Pair inner = new Pair(subRoutes.get(i));
						if (inner.overlaps(cur)) {
							try { // Try/Catch is for "Other" method.
								if (inner.one == end[left] || inner.two == end[left]) {
									left--;
									end[left] = inner.other(end[left + 1]);
								} else if (inner.one == end[right] || inner.two == end[right]) {
									right++;
									end[right] = inner.other(end[right - 1]);
								} else {
									System.out.println("Overlap Error Inner");
								}
							} catch (Exception e) {
								System.out.println("Overlap Error Outer");
							}
							cur = new Pair(end[left], end[right]);
							subRoutes.remove(i);
							overlapped = true;
							break;
						}
					}
					if (left - right <= 2 || subRoutes.size() == 0) {// if broken out of inner loop after overlap
						break;
					}
				}
				if (left - right <= 2 || subRoutes.size() == 0) {// if broken out of inner loop after overlap
					break;
				}

				// After adding all consecutive overlaps, Add a random unused pair to the
				// list.
				//for (int i = 0; i < subRoutes.size(); i++) {
				int i = 0;	//Solution times out if the loop is used.
					if (!endContains(end, left, right, subRoutes.get(i).one)
							&& !endContains(end, left, right, subRoutes.get(i).two)) {// If statement is necessary
																						// because pairs that do not
																						// overlap with either
						// end may overlap with the current contents of the list.
						if (rand.nextBoolean()) {
							if (rand.nextBoolean()) {
								end[left - 1] = subRoutes.get(i).one;// Add both parts of unused pair next to eachother
																		// to
																		// preserve connection.
								end[left - 2] = subRoutes.get(i).two;
								left -= 2;
								if (left - right <= 1) {
									break;
								}
							} else {
								end[left - 1] = subRoutes.get(i).two;
								end[left - 2] = subRoutes.get(i).one;
								left -= 2;
								if (left - right <= 1) {
									break;
								}
							}
						} else {
							if (rand.nextBoolean()) {
								end[right + 1] = subRoutes.get(i).two;
								end[right + 2] = subRoutes.get(i).one;
								right += 2;
								if (left - right <= 1) {
									break;
								}
							} else {
								end[right + 1] = subRoutes.get(i).one;
								end[right + 2] = subRoutes.get(i).two;
								right += 2;
								if (left - right <= 1) {
									break;
								}
							}
						}
						subRoutes.remove(i);
						cur = new Pair(end[left], end[right]);
						break;
					}
					else {
						break;
					}
				//}
			}
		}
//			System.out.println("Bounds: " + right + ", " + left);
//			for (int i = 0; i < routeLength; i++) {
//				System.out.print(end[i] + ", ");
//			}
//			System.out.println();s

		HashSet<Integer> contained = new HashSet<Integer>();
		contained.add(-1);
		while (left - right > 1) { // After all pairs exhausted, complete circuit with random unused numbers.
			int i = -1;
			// System.out.println("Starting");
			while (contained.contains(i))
				i = rand.nextInt(routeLength);
			// System.out.println("Ending");
			if (!endContains(end, left, right, i)) {
				right++;
				end[right] = i;
			}
			contained.add(i);
		}
		return new Chromosome(routeLength, end);
	}

	private boolean endContains(int[] end, int left, int right, int i) {
		boolean contains = false;
		for (int j = 0; j <= right; j++) {
			if (end[j] == i) {
				contains = true;
				break;
			}
		}
		if (!contains) {
			for (int j = left; j < routeLength; j++) {
				if (end[j] == i) {
					contains = true;
					break;
				}
			}
		}
		return contains;
	}

	private ArrayList<Pair> getSubRoutes(Chromosome other) {
		ArrayList<Pair> subRoutes = new ArrayList<Pair>();
		for (int i = 1; i < routeLength - 1; i++) {
			for (int j = 1; j < routeLength - 1; j++) {
				if (this.route[i] == other.route[j]) {
					if (this.route[i + 1] == other.route[j + 1] || this.route[i + 1] == other.route[j - 1]) {
						Pair connect = new Pair(this.route[i], this.route[i + 1]);
						if (!subRoutesContain(subRoutes, connect)) { // Necessary? Is there another double count?
							subRoutes.add(connect);
							// System.out.print(connect + ", ");
						}
					} else if (this.route[i - 1] == other.route[j + 1] || this.route[i - 1] == other.route[j - 1]) {
						Pair connect = new Pair(this.route[i], this.route[i - 1]);
						if (!subRoutesContain(subRoutes, connect)) { // Prevents double count for when
																		// i+1 == j+1 --> i-1 == j-1
							subRoutes.add(connect);
							// System.out.print(connect + ", ");
						}
					}
					break; // Should be unnecessary, as there should only be one of each number in each
							// array.
				}
			}
		}
		// Collections.sort(subRoutes);
//		for (int i = 0; i < subRoutes.size(); i++) {
//			System.out.print(subRoutes.get(i) + ", ");
//		}
		// System.out.println();
		return subRoutes;
	}

	private boolean subRoutesContain(ArrayList<Pair> subRoutes, Pair connect) {
		boolean contains = false;
		for (int k = 0; k < subRoutes.size(); k++) {
			if (connect.equals(subRoutes.get(k))) {
				contains = true;
				break;
			}
		}
		return contains;
	}
}
