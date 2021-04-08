


void main(){
    mediump float xVal = gl_FragCoord.x / 1000.;
    gl_FragColor = vec4(xVal,1.,xVal,1.);
}