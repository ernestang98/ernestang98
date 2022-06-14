#include <stdio.h>

int divide1(int m, int n, int *r);
void divide2(int m, int n, int *q, int *r);
int main()
{
    int m, n, q, r;
    printf("Enter two numbers (m and n): \n");
    scanf("%d %d", &m, &n);
    q = divide1(m, n, &r);
    printf("divide1(): quotient %d remainder %d\n", q, r);
    divide2(m, n, &q, &r);
    printf("divide2(): quotient %d remainder %d\n", q, r);
    return 0;
}
int divide1(int m, int n, int *r)
{
    if (n == 0)
    {
        printf("Error, divisor cannot be 0\n");
    }
    else
        if (m == n)
    {
        int q;
        q = 1;
        *r = 0;
        return q;
        return *r;
    }
    else if (m < n)
    {
        int q;
        q = 0;
        *r = m;
        return q;
        return *r;
    }
    else
    {
        int q = 0;
        while ((m - n) >= 0)
        {
            m = m - n;
            q++;
        }
        *r = m;
        return q;
        return *r;
    }
}
void divide2(int m, int n, int *q, int *r)
{
    if (n == 0)
    {
        printf("Error, divisor cannot be 0\n");
    }
    else
        if (m == n)
    {
        int *q;
        *q = 1;
        *r = 0;
    }
    else if (m < n)
    {
        int *q;
        *q = 0;
        *r = m;
    }
    else
    {
        int *q = 0;
        while ((m - n) >= 0)
        {
            m = m - n;
            *q++;
        }
        *r = m;
    }
}
