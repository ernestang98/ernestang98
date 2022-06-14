#include <stdio.h>
   #define SIZE 100
   void compress2D(int data[SIZE][SIZE], int rowSize, int colSize);
   int main()
   {
      int data[SIZE][SIZE];
      int i,j;
      int rowSize, colSize;
      printf("Enter the array size (rowSize, colSize): \n");
      scanf("%d %d", &rowSize, &colSize);
      printf("Enter the matrix (%dx%d): \n", rowSize, colSize);
      for (i=0; i<rowSize; i++)
         for (j=0; j<colSize; j++)
            scanf("%d", &data[i][j]);
      printf("compress2D(): \n");
      compress2D(data, rowSize, colSize);
      return 0;
   }
   void compress2D(int data[SIZE][SIZE], int rowSize, int colSize)
{
    int c, r;
    int count;
    int start;
    for (r = 0; r < rowSize; r++)
    {
        start = data[r][0];
        printf("%d", start);
        for (c = 1; c < colSize; c++)
        {
            if (data[r][c] == start)
            {
                count += 1;
                if (c + 1 == colSize) printf("%d", count);
            }
            else
            {
                printf("%d", count);
                start = data[r][c];
                printf("%d", start);
                count = 1;
            }
        }
        count = 1;
        printf("\n");
    }
}
