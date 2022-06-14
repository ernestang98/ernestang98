	/*edit*/

/*custom header*/

	/*end_edit*/
#include <stdio.h>
#include <string.h>
int locateFirstChar(char *str, char ch);
int main()
{
   char str[40], ch, *p;

   printf("Enter a string: \n");
   fgets(str, 40, stdin);
   if (p=strchr(str,'\n')) *p = '\0';
   printf("Enter the target character: \n");
   scanf("%c", &ch);
   printf("locateFirstChar(): %d\n", locateFirstChar(str, ch));
   return 0;
}
int locateFirstChar(char *str, char ch)
{
	/*edit*/
 int count = 0; int target = 0;
 for (int i = 0; (i < 40 && str[i] != '\0'); i++)
{
 if (str[i] == ch && count == 0)
 {
     count = count + 1;
     target = i;
 }
}
 if (count == 0)
 {
     return -1;
 }
return target;
	/*end_edit*/
}
