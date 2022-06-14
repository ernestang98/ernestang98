#include <stdio.h>
#include <string.h>
void insertChar(char *str1, char *str2, char ch);
int main()
{
 char a[80],b[80];
 char ch, *p;

 printf("Enter a string: \n");
 fgets(a, 80, stdin);
 if (p=strchr(a,'\n')) *p = '\0';
 printf("Enter a character to be inserted: \n");
 ch = getchar();
 insertChar(a,b,ch);
 printf("insertChar(): ");
 puts(b);
 return 0;
}
void insertChar(char *str1, char *str2, char ch)
{
    int length = strlen(str1);
    char temp;
    int add = length/3;
    int i;
    int p = 0;
    for (i=0; i < length + add; i++)
    {
        if (i % 3 != 0)
        {
            str2[p] = str1[i];
            p += 1;
        }
        else if (i == 0)
        {
            str2[p] = str1[i];
            p += 1;
        }
        else
        {
            temp = str1[i];
            str2[p] = ch;
            str2[p+1] = temp;
            p += 2;
        }
    }
}
