#include <stdio.h>
float power1(float num, int p);
void power2(float num, int p, float *result);
int main()
{
 int power;
 float number, result=-1;

 printf("Enter the number and power: \n");
 scanf("%f %d", &number, &power);
 printf("power1(): %.2f\n", power1(number, power));
 power2(number,power,&result);
 printf("power2(): %.2f\n", result);
 return 0;
}

float power1(float num, int p)
{
    int cp = p;
    if (cp < 0)
    {
        p = -1 * p;
    }
    int x; float total = 1;
    for (x=1; x<=p; x++)
    {
        total *= num;
    }
    if (cp < 0)
    {
        total = 1/total;
    }
    return total;
}

void power2(float num, int p, float *result)
{
    int cp = p;
    if (cp < 0)
    {
        p = -1 * p;
    }
    int x; float total = 1;
    for (x=1; x<=p; x++)
    {
        total *= num;
    }
    if (cp < 0)
    {
        total = 1/total;
    }
    *result = total;
}
