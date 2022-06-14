#include <stdio.h>
void specialNumbers1D(int ar[], int num, int *size);
int main()
{
 int a[20],i,size=0,num;

 printf("Enter a number (between 100 and 999): \n");
 scanf("%d", &num);
 specialNumbers1D(a, num, &size);
 printf("specialNumbers1D(): ");
 for (i=0; i<size; i++)
 printf("%d ",a[i]);
 return 0;
}
void specialNumbers1D(int ar[], int num, int *size)
{
int i, temp, val;
int valval = 0;
int p = 0;
for (i = 100; i <= num; i++)
    {
        temp = i;
        while (temp > 0)
        {
            val = temp % 10;
            valval = valval + val * val * val;
            temp = temp / 10;
        }
        if (valval == i)
        {
            ar[p] = valval;
            p++;
            (*size) = (*size) + 1;
        }
        valval = 0;
    }
}
