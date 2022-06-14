	/*edit*/

/*custom header*/

	/*end_edit*/
#include <stdio.h>
void strIntersect(char *str1, char *str2, char *str3);
int main()
{
   char str1[50],str2[50],str3[50];

   printf("Enter str1: \n");
   scanf("%s",str1);
   printf("Enter str2: \n");
   scanf("%s",str2);
   strIntersect(str1, str2, str3);
   if (*str3 == '\0')
      printf("strIntersect(): null string\n");
   else
      printf("strIntersect(): %s\n", str3);
   return 0;
}
void strIntersect(char *str1, char *str2, char *str3)
{
	/*edit*/
 int a, b;
 int c = 0;
 for (a=0; (a < 50 && str1[a] != '\0'); a++)
 {
    for (b=0; (b < 50 && str2[b] != '\0'); b++)
    {
    if (str1[a] == str2[b])
    {
        str3[c] = str1[a];
        c = c + 1;
    }
    }
    if (c == 0)
    {
        *str3 = '\0';
    }
 }
	/*end_edit*/
}
