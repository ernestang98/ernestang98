using namespace std;
#include "Animal.h"
#include <iostream>
#include <string>

Animal::Animal(): _name("unknown") {
	cout << "constructing Animal object " << _name << endl;
}
Animal::Animal(string n, COLOR c) : _name(n), _color(c) {
	cout << "constructing Animal object " << _name << " with color " << _color << endl;
}
Animal::~Animal() {
	cout << "deconstructing Animal Object " << endl;
}
void Animal::speak() const {
	cout << "Animal speaks " << endl;
}

Mammal:: Mammal() {};

Mammal::Mammal(string n, COLOR c) :Animal(n, c) {
	cout << "constructing Mammal object " << endl;
}
Mammal::~Mammal() {
	cout << "deconstructing Mammal object " << endl;
}
void Mammal::eat() {
	cout << "Mammal eat " << endl;
}
void Mammal::move() {
	cout << "Mammal move " << endl;
}
