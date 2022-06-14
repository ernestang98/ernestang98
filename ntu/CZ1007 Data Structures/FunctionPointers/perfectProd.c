#include <stdio.h>
int perfectProd1(int num);
void perfectProd2(int num, int *prod);
int main()
{
 int number, result=0;

 printf("Enter a number: \n");
 scanf("%d", &number);
 printf("Calling perfectProd1() \n");
 printf("perfectProd1(): %d\n", perfectProd1(number));

 printf("Calling perfectProd2() \n");
 perfectProd2(number, &result);
 printf("perfectProd2(): %d\n", result);
 return 0;
}
int perfectProd1(int num)
{
int i;
int total = 1
int n, sum;
for (n=1; n<num; n++)
{
    int sum = 0;
    int i = 1;
    while (i < n)
    {
        if (n%i==0)
        {
            sum += n;
        }
        i++;
    }
    if (sum == n)
    {
      printf("Perfect number: %d \n",n);
      total = total * n;
    }
}
}
void perfectProd2(int num, int *prod)
{
 int n = 1;
 int total = 1;
 int i, sum;
 for(n=1;n<=num;n++){
    i=1;
    sum = 0;
    while(i<n){
      if(n%i==0)
      {
          sum=sum+i;
      }
          i++;
    }
    if(sum==n)
    {
      printf("Perfect number: %d \n",n);
      total = total * n;
    }
  }
  *prod = total;
}
