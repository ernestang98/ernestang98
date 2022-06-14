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
    for (int row = 0; row < rowSize; row++)
    {
        int temp_num = data[row][0], count = 0;
        for (int col = 0; col < colSize; col++)
        {
            if (data[row][col] == temp_num) count++;
            else
            {
                printf("%d %d ", temp_num, count);
                temp_num = data[row][col];
                count = 1;
            }
        }
        printf("%d %d ", temp_num, count);
        printf("\n");
    }
}
