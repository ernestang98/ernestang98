#include <stdio.h>
typedef struct {
 char name[20];
 int age;
} Person;
void readData(Person *p);
Person findMiddleAge(Person *p);
int main()
{
 Person man[3], middle;

 readData(man);
 middle = findMiddleAge(man);
 printf("findMiddleAge(): %s %d\n", middle.name, middle.age);
 return 0;
}
void readData(Person *p)
{
int i;
for (i = 0; i<3; i++)
{
printf("Enter person %d: \n", i+1);
scanf("%s %d", &(p+i)->name, &(p+i)->age);
// printf("%s %d \n", (p+i)->name, (p+i)->age); //
}
}

Person findMiddleAge(Person *p)
{
int v1 = p->age;
// printf("%d\n", v1); //
int v2 = (p+1)->age;
// printf("%d\n", v2); //
int v3 = (p+2)->age;
// printf("%d\n", v3); //
if (v1 < v2 && v2 < v3) return *(p+1);
else if (v2 < v1 && v1 < v3) return *p;
else if (v1 < v3 && v3 < v2) return *(p+2);
else if (v2 < v3 && v3 < v1) return *(p+2);
else if (v3 < v2 && v2 < v1) return *(p+1);
else if (v3 < v1 && v1 < v2) return *p;
return *p;
}
