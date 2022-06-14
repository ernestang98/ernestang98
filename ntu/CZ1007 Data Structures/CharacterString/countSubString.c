#include <stdio.h>
#include <string.h>
#define INIT_VALUE -1
int countSubstring(char str[], char substr[]);
int main()
{
 char str[80], substr[80], *p;
 int result=INIT_VALUE;

 printf("Enter the string: \n");
 fgets(str, 80, stdin);
 if (p=strchr(str,'\n')) *p = '\0';
 printf("Enter the substring: \n");
 fgets(substr, 80, stdin);
 if (p=strchr(substr,'\n')) *p = '\0';
 result = countSubstring(str, substr);
 printf("countSubstring(): %d\n", result);
 return 0;
}

int countSubstring(char str[], char substr[])
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
    acount = acount + 1;
    count = 0;
    p = 0;
}
return acount;
}
