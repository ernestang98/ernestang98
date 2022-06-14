using namespace std;

#include "childAnimal.h"
#include <iostream>
#include <string>

Dog::Dog(): Mammal (), _owner("unknown") {
	cout << "constructing Dog object " << endl;
}
Dog::Dog(string n, COLOR c, string o) :Mammal(n, c), _owner(o) {
	cout << "constructing Dog object " << endl;
}
Dog::~Dog() {
	cout << "Deconstructing Dog object " << endl;
}
void Dog::speak() const {
	cout << "Dog woofs " << endl;
}
void Dog::move() {
	cout << "Dog moves " << endl;
}
void Dog::eat() {
	cout << "Dog eats " << endl;
}

Cat::Cat(): Mammal (){
	cout << "constructing Cat object " << endl;
}
Cat::Cat(string n, COLOR c) :Mammal(n, c){
	cout << "constructing Cat object " << endl;
}
Cat::~Cat() {
	cout << "deconstructing Cat object " << endl;
}
void Cat::speak() const {
	cout << "Cat meows " << endl;
}
void Cat::move() {
	cout << "Cat moves " << endl;
}
void Cat::eat() {
	cout << "Cat eats " << endl;
}

Lion::Lion(): Mammal () {
	cout << "constructing Lion object "<< endl;
}
Lion::Lion(string n, COLOR c) :Mammal(n, c){
	cout << "constructing Lion object " << endl;
}
Lion::~Lion() {
	cout << "deconstructing Lion object " << endl;
}
void Lion::speak() const {
	cout << "Lion roars " << endl;
}
void Lion::move() {
	cout << "Lion moves " << endl;
}
void Lion::eat() {
	cout << "Lion eats " << endl;
}
