void setup() {
  size(1800,900,P2D);
  monke = loadShader("TestShader.frag");
}

PShader monke;
void draw(){
  shader(monke);
  rect(0,0,width,height);
  
}
