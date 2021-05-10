#include <iostream>

extern const char jarFile[];

int main() {


	system("java -jar --enable-preview \"Main.jar\"");
	std::cout<<"\n\nHusk, at for at spille dette, skal du have den nyeste version af Java runtime OG den nyeste JDK (Java development kit)!\n";
	system("Pause");
}