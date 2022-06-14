#include <stdio.h>
#include <string.h>
#define MAX 100
typedef struct {
 char name[40];
 char telno[40];
 int id;
 double salary;
} Employee;
void printEmp(Employee *emp, int size);
int readin(Employee *emp);
int search(Employee *emp, int size, char *target);
int addEmployee(Employee *emp, int size, char *target);
int main()
{
 Employee emp[MAX];
 char name[40], *p;
 int size, choice, result;

 printf("Select one of the following options: \n");
 printf("1: readin() \n");
 printf("2: search() \n");
 printf("3: addEmployee() \n");
 printf("4: print() \n");
 printf("5: exit() \n");
 do {
 printf("Enter your choice: \n");
 scanf("%d", &choice);
 switch (choice) {
 case 1:
 size = readin(emp);
 break;

 case 2:
 printf("Enter search name: \n");
 scanf("\n");
 fgets(name, 40, stdin);
 if (p=strchr(name,'\n')) *p = '\0';
 result = search(emp,size,name);
 if (result != 1)
 printf ("Name not found! \n");
 break;
 case 3:
 printf("Enter new name: \n");
 scanf("\n");
 fgets(name, 40, stdin);
 if (p=strchr(name,'\n')) *p = '\0';
 result = search(emp,size,name);
 if (result != 1)
 size = addEmployee(emp, size, name);
 else
 printf("The new name has already existed in the database\n");
 break;
 case 4:
 printEmp(emp, size);
 break;
 default:
 break;
 }
 } while (choice < 5);
 return 0;
}
void printEmp(Employee *emp, int size)
{
 int i;

 printf("The current employee list: \n");
 if (size==0)
 printf("Empty array\n");
 else
 {
 for (i=0; i<size; i++)
 printf("%s %s %d %.2f\n",emp[i].name,emp[i].telno,emp[i].id,
 emp[i].salary);
 }
}
int readin(Employee *emp)
{
int i = 0;int size = 0; char *p, dummy;
//i = size, technically can remove one var
 while(1){
    scanf("%c", &dummy);
    printf("Enter name:\n");
    fgets((emp + i)->name, 40, stdin);
    if (p = strchr((emp+i)->name, '\n'))
        *p = '\0';
    if (strcmp((emp+i)->name, "#") == 0)
        break;
    else{
        printf("Enter tel: \n");
        scanf("%c", &dummy);
        scanf("%s", &(emp+i)->telno);
        printf("Enter id:\n");
        scanf("%c", &dummy);
        scanf("%d", &(emp+i)->id);
        printf("Enter salary:\n");
        scanf("%c", &dummy);
        scanf("%lf", &(emp+i)->salary);
        size++;
        i++;
    }
 }
 return size;
}
int search(Employee *emp, int size, char *target)
{
 int result = 0; int i;
 for( i= 0; i<size; i++){
    if( strcmp((emp+i)->name, target)== 0){
        result = 1;
        printf("Employee found at index location: %d\n", i);
        printf("%s %s %d %.2f\n",(emp+i)->name,(emp+i)->telno, (emp+i)->id, (emp+i)->salary);
        break;
    }
    else{
        result = 0;
    }
 }
return result;
}
int addEmployee(Employee *emp, int size, char *target)
{
int newsize;

printf("Enter tel:\n");
//scanf("%c", &dummy);
scanf("%s", &(emp+size)->telno);
printf("Enter id:\n");
//scanf("%c", &dummy);
scanf("%d", &(emp+size)->id);
printf("Enter salary:\n");
//scanf("%c", &dummy);
scanf("%lf", &(emp+size)->salary);
newsize = size + 1;

printf("Added at position: %d\n", size);

return newsize;
}
