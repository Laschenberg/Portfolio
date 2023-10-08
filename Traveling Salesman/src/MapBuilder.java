import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class MapBuilder {
	
	//static int numCities = 10;
	public static int minCost = 10;
	static int scale = 5;
	static boolean preset = true;
	
	public static void presetMap(int numCities) throws IOException {
		int[][] cityDistances = new int[numCities][numCities];
		int neighborRight, neighborLeft;
		for (int c=0; c<numCities; c++) {
			for (int t=0; t<numCities; t++) {
				cityDistances[c][t]=1000;
			}
		}
		
		for (int c=0; c<numCities; c++) {
			cityDistances[c][c]=0;
			for (int n=1; n<=numCities/2; n++) {
				neighborRight = (c+n)%numCities;
				neighborLeft = (c+(numCities-n))%numCities;
				//System.out.printf("%dth neighborhood %d %d %d \n",n,neighborLeft, c, neighborRight);
				cityDistances[c][neighborRight] = minCost*n;
				cityDistances[c][neighborLeft] = minCost*n;
				//cityDistances[neighborRight][c] = 10*n;
				//cityDistances[neighborLeft][c] = 10*n;
			}
			
		}
		
		FileWriter fw = new FileWriter(new File ("./src/cityDistances.txt"), false);
		for (int c=0; c<numCities; c++) {
			for (int t=0; t<numCities; t++) {
				fw.write(c+"\t"+t+"\t"+cityDistances[c][t]+"\n");
			}
		}
		fw.close();
	}
	
	public static void randomMap(int numCities) throws IOException {
		FileWriter fw = new FileWriter(new File ("./src/cityCoords.txt"), false);
		int gridSize = numCities*scale;
		
		Random rand = new Random();
		
		Point[] cities = new Point[numCities];
		for (int c=0; c<numCities; c++) {
			cities[c] = new Point (rand.nextInt(gridSize),rand.nextInt(gridSize));
			fw.write(c+"\t"+cities[c].x+"\t"+cities[c].y + "\n");
			
		}
		fw.close();
		
		fw = new FileWriter(new File ("./src/cityDistances.txt"), false);
		for (int c=0; c<numCities; c++) {
			for (int t=0; t<numCities; t++) {
				fw.write(c+"\t"+t+"\t"+cities[c].distance(cities[t])+"\n");
			}
		}
		fw.close();
	}
	public static void main (String[] args) throws IOException {
		
		if (preset)
			presetMap(10);
		else
			randomMap(10);
		
	}
}
