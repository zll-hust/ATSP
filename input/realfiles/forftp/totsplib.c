#include <stdio.h>
#include <math.h>

#define MAXMAT 3200
#define INF 1

int distmat[MAXMAT][MAXMAT];
int outmat[3*MAXMAT][3*MAXMAT];

main (argc, argv)
int argc;
char **argv;

{
  FILE *fp, *fopen();
  int size;
  int dist;
  int i,j;
  char c;

  if (argc < 2) {
	printf("Usage:  totsplib matrix\n");
	exit(0);
  }

  fp = fopen(argv[1], "r");
  if (fp == NULL) {
	printf("Error opening input file\n");
	exit(0);
  }

  fscanf(fp,"%d %c",&size,&c);

  if (c!='A') {
    printf("ReadR: file %s is not random distance matrix\n",argv[1]);
    exit(0);
  }

  printf("NAME: %s\n",argv[1]);
  printf("TYPE: ATSP\nCOMMENT:\nDIMENSION: %d\n",size);
  printf("EDGE_WEIGHT_TYPE: EXPLICIT\n");
  printf("EDGE_WEIGHT_FORMAT: FULL_MATRIX\n");
  printf("EDGE_WEIGHT_SECTION\n");

  
  for (i=1;i<=size;i++)
    for (j=1;j<=size;j++) {
  	fscanf(fp,"%d",&dist);
  	printf("%d\n",dist);
    }
}
