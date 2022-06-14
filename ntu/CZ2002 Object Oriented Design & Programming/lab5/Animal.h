#ifndef ANIMAL_H
#define ANIMAL_H
using namespace std;

#include <iostream>
#include <string>


enum COLOR { Green, Blue, White, Black, Brown } ;

class Animal {
    private :
        string _name;
        COLOR _color ;

    public:
        Animal();
        Animal(string n, COLOR c);
        virtual ~Animal();
        virtual void speak() const;
        virtual void move() = 0;
        virtual void eat() = 0;
};

class Mammal: public Animal {
    public:
        Mammal();
        Mammal(string n, COLOR c);
        virtual ~Mammal();
        virtual void move() override;
        virtual void eat() override;
};


#endif
