#include <stdio.h>

int sumSqDigits1(int num);
void sumSqDigits2(int num, int *result);
int main()
{
    int num, result;
    printf("Enter a number: \n");
    scanf("%d", &num);
    printf("sumSqDigits1(): %d\n", sumSqDigits1(num));
    sumSqDigits2(num, &result);
    printf("sumSqDigits2(): %d\n", result);
    return 0;
}
int sumSqDigits1(int num)
{
    int sum = 0, val;
    if (num < 0)
    {
        num = num * -1;
    }
    while (num > 0)
    {
        val = num % 10;
        sum = sum + val * val;
        num = num / 10;
    }
    return sum;
}
void sumSqDigits2(int num, int *result)
{
    int sum = 0, val;
    if (num < 0)
    {
        num = num * -1;
    }
    while (num > 0)
    {
        val = num % 10;
        sum = sum + val * val;
        num = num / 10;
    }
    *result = sum;
}
