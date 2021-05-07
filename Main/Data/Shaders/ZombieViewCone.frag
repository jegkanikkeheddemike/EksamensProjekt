#ifdef GL_ES
precision mediump float;
#endif

uniform int zombies;
uniform int walls;
uniform float zombieX[30];
uniform float zombieY[30];
uniform float zombieRotation[30];
uniform float wallX[100];
uniform float wallY[100];
uniform float wallWidth[100];
uniform float wallHeight[100];
uniform float camX;
uniform float camY;
uniform float resX;
uniform float resY;

bool lineCollision(float x1,float y1,float x2,float y2,float x3,float y3,float x4,float y4){
    // calculate the direction of the lines
    float den=((y4-y3)*(x2-x1)-(x4-x3)*(y2-y1));
    
    float uA=((x4-x3)*(y1-y3)-(y4-y3)*(x1-x3))/den;
    float uB=((x2-x1)*(y1-y3)-(y2-y1)*(x1-x3))/den;
    // if uA and uB are between 0-1, lines are colliding
    
    return(den!=0.&&(uA>=0.&&uA<=1.&&uB>=0.&&uB<=1.));
}

float angleBetween(vec2 p1,vec2 p2){
    float dx=p2.x-p1.x;
    float dy=p2.y-p1.y;
    return atan(dy,dx);
}

const float PI=3.14159265359;

void main(){
    //translated coords
    vec2 tCoords=vec2(
        gl_FragCoord.x-resX/2.+camX,
        resY/2.-gl_FragCoord.y+camY
    );
    
    //er 0 hvis denne pixel ikke kan blive set, 1 hvis den godt kan.
    float seen=0.;
    
    for(int i=0;i<zombies;i++){
        vec2 zCoords=vec2(zombieX[i],zombieY[i]);
        
        float angleBetween=angleBetween(zCoords,tCoords);
        float relAngle=zombieRotation[i]-angleBetween;
        relAngle=mod(relAngle,2.*PI);
        
        float fov=(120./360.)*PI;
        
        bool inRange=distance(zCoords,tCoords)<600.;
        bool inAngle=relAngle<fov||relAngle>(2.*PI-fov);
        
        int nColl=0;
        for(int j=0;j<walls;j++){
            vec2 wCoords=vec2(wallX[j],wallY[j]);
            
            float realRot=-zombieRotation[i]+PI/2.;
            float distToZomb=min(distance(tCoords,zCoords),600.);
            
            //Sightcoords
            vec2 sCoords=vec2(
                zCoords.x+sin(realRot+relAngle)*distToZomb,
                zCoords.y+cos(realRot+relAngle)*distToZomb
            );
            
            bool colWithTop=lineCollision(
                wCoords.x,
                wCoords.y,
                wCoords.x+wallWidth[j],
                wCoords.y,
                sCoords.x,
                sCoords.y,
                zCoords.x,
                zCoords.y
            );
            
            bool colWithRight=lineCollision(
                wCoords.x+wallWidth[j],
                wCoords.y,
                wCoords.x+wallWidth[j],
                wCoords.y+wallHeight[j],
                sCoords.x,
                sCoords.y,
                zCoords.x,
                zCoords.y
            );
            bool colWithLeft=lineCollision(
                wCoords.x,
                wCoords.y,
                wCoords.x,
                wCoords.y+wallHeight[j],
                sCoords.x,
                sCoords.y,
                zCoords.x,
                zCoords.y
            );
            bool colWithBot=lineCollision(
                wCoords.x,
                wCoords.y+wallHeight[j],
                wCoords.x+wallWidth[j],
                wCoords.y+wallHeight[j],
                sCoords.x,
                sCoords.y,
                zCoords.x,
                zCoords.y
            );
            nColl+=int(colWithTop||colWithRight||colWithLeft||colWithBot);
        }
        
        bool noColl=nColl==0;
        seen+=float(inRange&&inAngle&&noColl);
    }
    gl_FragColor=vec4(0.,0.,1.,.1*seen);
}