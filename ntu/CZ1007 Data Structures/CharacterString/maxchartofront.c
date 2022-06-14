#include <stdio.h>
#include <string.h>
void maxCharToFront(char *str);
int main()
{
 char str[80], *p;

 printf("Enter a string: \n");
 fgets(str, 80, stdin);
 if (p=strchr(str,'\n')) *p = '\0';
 printf("maxCharToFront(): ");
 maxCharToFront(str);
 puts(str);
 return 0;
}
void maxCharToFront(char *str)
{
int i;
int counter;
char temp;
char max = str[0];
int length = strlen(str);
for (i = 0; i < length; i++)
{
    if (max <= str[i])
    {
        max = str[i];
        counter = i;
    }
}
temp = str[0];
str[0] = max;
i = 1;
for (i = counter; i > 0; i--)
{
    if (i != counter)
    {
        str[i+1] = str[i];
    }
    else continue;
}
str[1] = temp;
}
