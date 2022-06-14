/*edit*/

/*custom header*/

	/*end_edit*/
#include <stdio.h>
#define SIZE 4
void computeTotal2D(int matrix[SIZE][SIZE]);
int main()
{
   int a[SIZE][SIZE];
   int i,j;
   printf("Enter the matrix data (%dx%d): \n", SIZE, SIZE);
   for (i=0; i<SIZE; i++)
      for (j=0; j<SIZE; j++)
         scanf("%d", &a[i][j]);
   printf("computeTotal2D(): \n");
   computeTotal2D(a);
   for (i = 0; i < SIZE; i++) {
      for (j = 0; j < SIZE; j++)
         printf("%d ", a[i][j]);
      printf("\n");
   }
   return 0;
}
void computeTotal2D(int matrix[SIZE][SIZE])
{
	/*edit*/
 int r; int c; int val=0;
 for (r=0; r<SIZE; r++)
 {
    for (c=0; c<SIZE-1; c++)
    {
    val = matrix[r][c] + val;
    }
    matrix[r][SIZE-1] = val;
    val = 0;
 }
	/*end_edit*/
}
