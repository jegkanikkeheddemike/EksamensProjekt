#include <iostream>
#include <stdexcept>
#include <stdio.h>
#include <string>

int main() {
	system("java -jar --enable-preview \"Main.jar\"");
	std::cout<<"\n\nBemærk, at for at køre dette, kræver det den nyeste version af Java runtime!\n";
	system("Pause");
}