#include <stdio.h>
#define INIT_VALUE 999
int extEvenDigits1(int num);
void extEvenDigits2(int num, int *result);
int main()
{
 int number, result = INIT_VALUE;

 printf("Enter a number: \n");
 scanf("%d", &number);
 printf("extEvenDigits1(): %d\n", extEvenDigits1(number));
 extEvenDigits2(number, &result);
 printf("extEvenDigits2(): %d\n", result);
 return 0;
}
int extEvenDigits1(int num)
{
    int val = 1, sum = 0;
    int x = num;
    while (num > 0)
    {
    x = x % 10;
    if (x % 2 == 0)
    {
        sum = sum + (x * val);
        val = val * 10;
    }
    num = num / 10;
    x = num;
    }
    if (sum == 0)
    {
        sum = -1;
    }
    num = sum;
    return num;
}
void extEvenDigits2(int num, int *result)
{
    int val = 1, sum = 0;
    int x = num;
    while (num > 0)
    {
    x = x % 10;
    if (x % 2 == 0)
    {
        sum = sum + x * val;
        val = val * 10;
    }
    num = num / 10;
    x = num;
    }
        if (sum == 0)
    {
        sum = -1;
    }
    *result = sum;
}
