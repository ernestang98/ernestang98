using namespace std;

#include "Animal.h"
#include <iostream>
#include <string>
#pragma once

class Dog : public Mammal {
    private:
        string _owner;

    public:
        Dog();
        Dog(string n, COLOR c, string o);
        ~Dog();
        void speak() const;
        void move() override;
        void eat() override;
};

class Cat : public Mammal {
    private:
        string _owner;

    public:
        Cat();
        Cat(string n, COLOR c);
        ~Cat();
        void speak() const;
        void move() override;
        void eat() override;
};

class Lion : public Mammal {
    private:
        string _owner;

    public:
        Lion();
        Lion(string n, COLOR c);
        ~Lion();
        void speak() const;
        void move() override;
        void eat() override;
};
