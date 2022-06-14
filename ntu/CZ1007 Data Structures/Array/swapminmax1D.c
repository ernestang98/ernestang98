/*edit*/

/*custom header*/

	/*end_edit*/
#include <stdio.h>
void swapMinMax1D(int ar[], int size);
int main()
{
   int ar[50],i,size;

   printf("Enter array size: \n");
   scanf("%d", &size);
   printf("Enter %d data: \n", size);
   for (i=0; i<size; i++)
      scanf("%d",ar+i);
   swapMinMax1D(ar, size);
   printf("swapMinMax1D(): ");
   for (i=0; i<size; i++)
      printf("%d ",*(ar+i));
   return 0;
}
void swapMinMax1D(int ar[], int size)
{
	/*edit*/
    int w; int v; int n; int m;
    int max = ar[0]; int min = ar[1];
    if (min > max)
    {
        max = ar[1]; min = ar[0];
    }
    for (w=2; w<size; w++)
    {
        if (ar[w] > max)
        {
            max = ar[w];
        }
        else if (ar[w] < min)
        {
            min = ar[w];
        }
    }
    for (v=0; v<size; v++)
    {
        if (ar[v] == min)
        n = v;
        if (ar[v] == max)
        m = v;
    }
    ar[n] = max;
    ar[m] = min;
	/*end_edit*/
}
