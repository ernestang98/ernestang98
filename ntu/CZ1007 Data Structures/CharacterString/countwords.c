#include <stdio.h>
#include <string.h>
int countWords(char *s);
int main()
{
 char str[50], *p;
 printf("Enter the string: \n");
 fgets(str, 80, stdin);
 if (p=strchr(str,'\n')) *p = '\0';
 printf("countWords(): %d", countWords(str));
 return 0;
}
int countWords(char *s)
{
int length = strlen(s);
int i;
int count = 1;
for (i = 0; i < length; i++)
{
    if (s[i] == ' ')
        count += 1;
}
return count;
}
