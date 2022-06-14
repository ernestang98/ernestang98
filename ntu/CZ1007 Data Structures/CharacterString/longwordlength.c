#include <stdio.h>
#include <string.h>
int longWordLength(char *s);
int main()
{
 char str[80], *p;
 printf("Enter a string: \n");
 fgets(str, 80, stdin);
 if (p=strchr(str,'\n')) *p = '\0';
 printf("longWordLength(): %d\n", longWordLength(str));
 return 0;
}
int longWordLength(char *s)
{
    int length = strlen(s);
    int i;
    int count = 0;
    int max = 0;
    for (i = 0; i < length + 1; i++)
    {
        if (s[i] != ' ' && s[i] != '\0' && s[i] <= 122 && s[i] >= 65 && s[i])
            count += 1;
        else if (s[i] == ' ' || s[i] == '\0')
        {
            if (max < count)
        {
            max = count;
        }
            count = 0;
        }
    }
    return max;
}
