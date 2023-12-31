#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
//#include <pthread.h>

#define N 10000
#define NUM_STATS 28  	 // bar, rab, bao, rao, rar, bab, bdr, rdb, bdo, rdo, rdr, bdb, bnar, rnab,
						 //bnao, rnao, rnar, bnab, bndr, rndb, bndo, rndo, rndr, bndb, ro, re, bo, be

char board[N][8][8];

int numAdjacent(char of, char to, int b);
int numDiagonal(char of, char to, int b);
int numNotAdjacent(char of, char to, int b);
int numNotDiagonal(char of, char to, int b);
int numOddCol(char of, int odd, int b); 
int numOf(char of, int b);

void print(int b);
void generate(int full, int b);
void* test(void* input);
void set(int b);

int* not(int* arr);
float avg(int* arr);
float sd(int* arr, float mu);

typedef struct input {
	int start;
	int end;
	int full;
} input;

int main() {
	/*pthread_t t1;
	pthread_t t2;
	pthread_t t3;
	pthread_t t4;
	pthread_t t5;
	input in1 = { 0, N / 3, 64 };
	input in2 = { N / 3, 2 * (N / 3), 64};
	input in3 = { 2 * (N / 3), N, 64 };
	input in4 = { 0, N / 2, 32 };
	input in5 = { 0, N / 2, 32 };
	pthread_create(&t1, NULL, test, &in1);
	pthread_create(&t2, NULL, test, &in2);
	pthread_create(&t3, NULL, test, &in3);
	pthread_create(&t4, NULL, test, &in4);//4 and 5 cannot run at same time as 1,2,and 3. Only one array of boards from 1 to N.
	pthread_create(&t5, NULL, test, &in5);
	pthread_join(t1, NULL);
	pthread_join(t2, NULL);
	pthread_join(t3, NULL);
	pthread_join(t4, NULL);
	pthread_join(t5, NULL);*/

	input in0 = { 0, N, 64};
	test(&in0);
	
	
	//generate(64, 0);
    /*set(0);
	board[0][0][0] = 'b'; // 0
	board[0][1][1] = 'b'; // 1
	board[0][2][2] = 'b'; // 0
	board[0][0][2] = 'b'; // 0
	*/
	/*
	print(0);
	printf("Num of b in (odd == 0) squares: %d\n", numOddCol('b', 0, 0));
	printf("Num of b in (odd == 1) squares: %d\n", numOddCol('b', 1, 0));
	*/
	return 0;
}

//-------------------------------------------------------------------------------------------------------------------------------------------------

int numAdjacent(char of, char to, int b) {
	int count = 0;
	for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 8; j++) {
			if (board[b][i][j] == of) {
				if (j > 0 && board[b][i][j - 1] == to) {
					count++;
				}
				else if (i > 0 && board[b][i - 1][j] == to) {
					count++;
				}
				else if (i < 7 && board[b][i + 1][j] == to) {
					count++;
				}
				else if (j < 7 && board[b][i][j + 1] == to) {
					count++;
				}
			}
		}
	}
	return count;
}

int numDiagonal(char of, char to, int b) {
	int count = 0;
	for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 8; j++) {
			if (board[b][i][j] == of) {
				if (i > 0 && j > 0 && board[b][i - 1][j - 1] == to) {
					count++;
				}else if (i > 0 &&  j < 7 && board[b][i - 1][j + 1] == to) {
					count++;
				}else if (i < 7 && j > 0 && board[b][i + 1][j - 1] == to) {
					count++;
				}else if (i < 7 && j < 7 && board[b][i + 1][j + 1] == to) {
					count++;
				}
			}
		}
	}
	return count;
}

int numNotDiagonal(char of, char to, int b) { //TODO: Not Adjacent
	int count = 0;
	for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 8; j++) {
			if (board[b][i][j] == of) {
				if (i > 0 && j > 0 && board[b][i - 1][j - 1] == to) {
					continue;
				}
				else if (i > 0 && j < 7 && board[b][i - 1][j + 1] == to) {
					continue;
				}
				else if (i < 7 && j > 0 && board[b][i + 1][j - 1] == to) {
					continue;
				}
				else if (i < 7 && j < 7 && board[b][i + 1][j + 1] == to) {
					continue;
				}
				count++;
			}
		}
	}
	return count;
}

int numNotAdjacent(char of, char to, int b) { //TODO: Not Adjacent
	int count = 0;
	for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 8; j++) {
			if (board[b][i][j] == of) {
				if (j > 0 && board[b][i][j - 1] == to) {
					continue;
				}
				else if (i > 0 && board[b][i - 1][j] == to) {
					continue;
				}
				else if (i < 7 && board[b][i + 1][j] == to) {
					continue;
				}
				else if (j < 7 && board[b][i][j + 1] == to) {
					continue;
				}
				count++;
			}
		}
	}
	return count;
}

int numOddCol(char of, int odd, int b) {
	int count = 0;
	for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 8; j++) {
			if (board[b][i][j] == of && i % 2 == odd) {
				count++;
			}
		}
	}
	return count;
}

int numOf(char of, int b) {
	int count = 0;
	for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 8; j++) {
			if (board[b][i][j] == of) {
				count++;
			}
		}
	}
	return count;
}

//---------------------------------------------------------------------------------------------------------------------------------------------
void* test(void* in) {
	input* obj = (input*)in;
	int start = obj->start;
	int end = obj->end;
	int full = obj->full;

	int* stats[NUM_STATS];
	for (int i = 0; i < NUM_STATS; i++)
		stats[i] = (int*)malloc(sizeof(int) * N);
	
	char of[2] = { 'r', 'b' };
	char to[3] = { 'b', 'r', 'o' };

	if (stats) {
		for (int k = start; k < end; k++) {
			for (int l = 0; l < 6; l++) {
				generate(full, k);
				if (stats[l]) {
					stats[l][k] = numAdjacent(of[l % 2], to[l % 3], k);
					stats[l + 6][k] = numDiagonal(of[l % 2], to[l % 3], k);
					stats[l + 12][k] = numNotAdjacent(of[l % 2], to[l % 3], k);
					stats[l + 18][k] = numNotDiagonal(of[l % 2], to[l % 3], k);
				}
				else {
					printf("NullPointerException!!\n");
				}
			}
			for (int l = 0; l < 2; l++) {
				if (stats[l]) {
					stats[l + 24][k] = numOddCol(of[l % 2], l % 2, k);
					stats[l + 26][k] = numOddCol(of[l % 2], (l + 1) % 2, k);
				}
				else {
					printf("NullPointerException!!\n");
				}
			}
		}
		printf("Simulation complete...\n");
		float average = 0;
		for (int k = 0; k < 6; k++) {
			average = avg(stats[k]);
			printf("Average of %c Adjacent to %c: %f Standard Deviation: %f\n", of[k % 2], to[k % 3], average, sd(stats[k], average));
		}
		printf("\n");
		for (int k = 6; k < 12; k++) {
			average = avg(stats[k]);
			printf("Average of %c Diagonal to %c: %f Standard Deviation: %f\n", of[k % 2], to[k % 3], average, sd(stats[k], average));
		}
		printf("\n");
		for (int k = 12; k < 18; k++) {
			average = avg(stats[k]);
			printf("Average of %c Not Adjacent to %c: %f Standard Deviation: %f\n", of[k % 2], to[k % 3], average, sd(stats[k], average));
		}
		printf("\n");
		for (int k = 18; k < 24; k++) {
			average = avg(stats[k]);
			printf("Average of %c Not Diagonal to %c: %f Standard Deviation: %f\n", of[k % 2], to[k % 3], average, sd(stats[k], average));
		}
		printf("\n");
		for (int k = 24; k < 26; k++) {
			average = avg(stats[k]);
			printf("Average of %c in (odd: %d) squares: %f Standard Deviation: %f\n", of[k % 2], k % 2, average, sd(stats[k], average));
		}
		for (int k = 26; k < 28; k++) {
			average = avg(stats[k]);
			printf("Average of %c in (odd: %d) squares: %f Standard Deviation: %f\n", of[k % 2], (k+1) % 2, average, sd(stats[k], average));
		}
		printf("Test complete.\n");
	} else {
		printf("Insufficient memory, test terminated.\n");
	}
	for (int i = 0; i < NUM_STATS; i++) {
		free(stats[i]);
	}
	return 0;
}

void generate(int full, int b) {
	time_t t;
	srand((unsigned) time(&t));
	int v, r;
	char p[] = { 'r', 'o', 'b', 'o' };
	
	set(b);
	
	for (int i = 0; i < full; i++) {
		
		v = 0;
		while (v == 0) {
			r = rand() % 64;
			if (board[b][r / 8][r % 8] == '-') {
				v = 1;
				board[b][r / 8][r % 8] = p[i % 4];
			}
			else {
				v = 0;
			}
		}
	}
}

void print(int b) {
	for (int i = 0; i < 3; i++) {
		printf("\n");
	}
	for (int i = 0; i < 8; i++) {
		for (int j = 0; j < 8; j++) {
			printf("%c", board[b][i][j]);
		}
		printf("\n");
	}
}

void set(int b) {
	for (int i = 0; i < 8; i++)
		for (int j = 0; j < 8; j++)
			board[b][i][j] = '-';
}

//---------------------------------------------------------------------------------------------------------------------------------------------

float avg(int* arr) {
	int count = 0;
	for (int i = 0; i < N; i++)
		count += arr[i];
	return (float)count / N;
}

float sd(int* arr, float mu) {
	float sum = 0;
	for (int i = 0; i < N; i++)
		sum += (arr[i] - mu) * (arr[i] - mu);
	return sqrt(sum/N);
}

int* not(int* arr) {
	int* end = (int*)malloc(sizeof(int) * N);
	if (end != NULL) {
		for (int i = 0; i < N; i++) {
			end[i] = (16 - arr[i]);
		}
	}
	else {
		printf("Insufficient memeory\n");
	}
	return end;
}