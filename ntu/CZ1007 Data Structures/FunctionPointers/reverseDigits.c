#include <stdio.h>
#include <math.h>

int reverseDigits1(int num);
void reverseDigits2(int num, int *result);
int main()
{
    int num, result=999;
    printf("Enter a number: \n");
    scanf("%d", &num);
    printf("reverseDigits1(): %d\n", reverseDigits1(num));
    reverseDigits2(num, &result);
    printf("reverseDigits2(): %d\n", result);
    return 0;
}
int reverseDigits1(int num)
{
    int count = 0;
    int val = num;
    int con;
    int sum = 0;
    int mul = 1;
    int a = 0;
    while (num > 0)
    {
        count += 1;
        num = num / 10;
    }
    count = count - 1;
    while (val > 0)
    {
        con = val % 10;
        mul = 1;
        for (a = 0; a < count; a++)
        {
            mul = 10 * mul;
        }
        sum = sum + con * mul;
        val = val / 10;
        count = count - 1;
    }
    return sum;
}
void reverseDigits2(int num, int *result)
{
    int count = 0;
    int val = num;
    int con;
    int sum = 0;
    int mul = 1;
    int a = 0;
    while (num > 0)
    {
        count += 1;
        num = num / 10;
    }
    count = count - 1;
    while (val > 0)
    {
        con = val % 10;
        mul = 1;
        for (a = 0; a < count; a++)
        {
            mul = 10 * mul;
        }
        sum = sum + con * mul;
        val = val / 10;
        count = count - 1;
    }
    *result = sum;
}
