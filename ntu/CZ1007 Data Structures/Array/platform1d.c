#include <stdio.h>
int platform1D(int ar[], int size);
int main()
{
 int i,b[50],size;
 printf("Enter array size: \n");
 scanf("%d", &size);
 printf("Enter %d data: \n", size);
 for (i=0; i<size; i++)
 scanf("%d",&b[i]);
 printf("platform1D(): %d\n", platform1D(b,size));
 return 0;
}
int platform1D(int ar[], int size)
{
int val;
int consec = 0;
int max = 1;
int p = 0;
for (int i = 0; i < size; i++)
{
    val = ar[i];
    while (ar[i+p] == val)
    {
        consec = consec + 1;
        p = p + 1;
    }
    if (max < consec)
    {
        max = consec;
    }
    p = 0;
    consec = 0;
}
return max;
}
