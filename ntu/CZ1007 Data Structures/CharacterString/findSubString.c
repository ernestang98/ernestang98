#include <stdio.h>
#include <string.h>
#define INIT_VALUE 999
int findSubstring(char *str, char *substr);
int main()
{
 char str[40], substr[40], *p;
 int result = INIT_VALUE;
 printf("Enter the string: \n");
 fgets(str, 80, stdin);
 if (p=strchr(str,'\n')) *p = '\0';
 printf("Enter the substring: \n");
 fgets(substr, 80, stdin);
 if (p=strchr(substr,'\n')) *p = '\0';
 result = findSubstring(str, substr);
 if (result == 1)
 printf("findSubstring(): Is a substring\n");
 else if ( result == 0)
 printf("findSubstring(): Not a substring\n");
 else
 printf("findSubstring(): An error\n");
 return 0;
}
int findSubstring(char *str, char *substr)
{
int i, p, count, acount = 0;
int len1 = strlen(str);
int len2 = strlen(substr);
for (i = 0; i < len1; i++)
{
    for (p = 0; p < len2; p++)
    {
        if (substr[p] == str[i+p])
            count = count + 1;
    }
    if (count == len2)
        return 1;
    count = 0;
    p = 0;
}
return 0;
}
