#include <iostream>
#include <stdexcept>
#include <stdio.h>
#include <string>

int main() {
	system("java -jar --enable-preview \"Main.jar\"");
	std::cout<<"Bemærk, at for at køre dette, kræver det den nyeste version af Java runtime!\n";
	system("Pause");
}