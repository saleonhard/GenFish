/**
 * Componente Curricular: Estrutura de Dados
 * Autor: Leonardo Aquino
 * Adaptado de: Nicholas Tang
 * Código Fonte Disponível em: <https://www.openprocessing.org/sketch/20105/> 
 * Data:   ‎8‎ de ‎Outubro‎ de ‎2019
 *
 * Declaro que este código foi elaborado por mim de forma individual e
 * não contém nenhum trecho de código de outro colega ou de outro autor, 
 * tais como provindos de livros e apostilas, e páginas ou documentos 
 * eletrônicos da Internet. Qualquer trecho de código de outra autoria que
 * uma citação para o  não a minha está destacado com  autor e a fonte do
 * código, e estou ciente que estes trechos não serão considerados para fins
 * de avaliação. Alguns trechos do código podem coincidir com de outros
 * colegas pois estes foram discutidos em sessões tutorias.
 */


//Nicholas Tang
//Thanks to Tanmay Prakash for help with the math
//2/1/11


class Fish {

  int len = 98;
  float dx = 0.4/3;
  float[][] tailPath;
  float[] pos;
  float th;
  float nth;
  float dTh = 4*dx/60;
  float phi0;
  float fcolor;
  int r,rB;
  int g,gB;
  int b,bB;
  Double fit;

  float ftint;
  float fisht;
  float randt;

  Fish(float pos1, float pos2, float th1, float fcolor1, float ftint1, int rCorpo, int gCorpo, int bCorpo, int rBarba, int gBarba, int bBarba) {
    tailPath = new float[4][len];
    pos = new float[3];
    phi0 = random(2*PI);
    th = th1;
    nth = th;
    r= rCorpo;
    g = gCorpo;
    b = bCorpo;
    
    rB= rBarba;
    gB = gBarba;
    bB = bBarba;
    
    for (int i = 1; i < len; i++) {
      tailPath[1][i] = (i-1)*dx*sin(th)+pos1;
      tailPath[2][i] = (i-1)*dx*cos(th)+pos2;
      tailPath[3][i] = th;
    }
    pos[1] = pos1;
    pos[2] = pos2;
    fcolor = fcolor1;
    ftint = ftint1;
    fisht = 0;
    randt = round(random(50));
  }

  void display(float t) {
    int len2 = -31;
    int lenfin = 40;
    float[] y2 = new float[len];
    float[] thickness = new float[len];
    float[] tailR = new float[len];
    float[] tailL = new float[len];
    float[][] pHeadR = new float[3][abs(len2)];
    float[][] pHeadL = new float[3][abs(len2)];
    float[][] pFinR = new float[3][lenfin];
    float[][] pFinL = new float[3][lenfin];
    float[][] pEyeR = new float[3][lenfin];
    float[][] pEyeL = new float[3][lenfin];
    float[][] pTailR = new float[3][len];
    float[][] pTailL = new float[3][len];
    float squareScale = (60/8)*3;
    float phi = t*(PI/15)+phi0;
    float w = 0.2*squareScale;
    for (int i = 1; i < len; i++) {
      y2[i] = (squareScale*exp(0.1/1.25*((i-1)*dx-30))-squareScale*exp(0.1/1.25*(-30)))*cos(0.2*(i-1)*dx-phi);
      thickness[i] = w*(cos(PI/(len*dx)*(i-1)*dx)+1)*0.5*int((i-1)<len);
      tailR[i] = y2[i] + thickness[i];
      tailL[i] = y2[i] - thickness[i];
      pTailR[1][i] = tailR[i]*cos(tailPath[3][i])+3*tailPath[1][i]-3*pos[1];
      pTailR[2][i] = tailR[i]*sin(tailPath[3][i])+3*tailPath[2][i]-3*pos[2];
      pTailL[1][i] = tailL[i]*cos(tailPath[3][i])+3*tailPath[1][i]-3*pos[1];
      pTailL[2][i] = tailL[i]*sin(tailPath[3][i])+3*tailPath[2][i]-3*pos[2];
    }
    for (int i = 1; i < abs(len2); i++) {
      pHeadR[1][i] = (sqrt(1-pow(((i+len2)*dx), 2)/pow(4, 2))*w)*cos(th)+3*((i+len2)*dx)*cos(th-PI/2);
      pHeadR[2][i] = (sqrt(1-pow(((i+len2)*dx), 2)/pow(4, 2))*w)*sin(th)+3*((i+len2)*dx)*sin(th-PI/2);
      pHeadL[1][i] = -(sqrt(1-pow(((i+len2)*dx), 2)/pow(4, 2))*w)*cos(th)+3*((i+len2)*dx)*cos(th-PI/2);
      pHeadL[2][i] = -(sqrt(1-pow(((i+len2)*dx), 2)/pow(4, 2))*w)*sin(th)+3*((i+len2)*dx)*sin(th-PI/2);
    }
    float scfin = (PI/3)/lenfin;
    float sceye = (2*PI)/lenfin;
    float ecc = 1.25;
    float sc = 0.15*3;
    float eyex = 2.5*3;
    float eyey = (7*w)/16;
    for (int i = 1; i < lenfin; i++) {
      pFinR[1][i] = w*cos(th)+3*2*sin(3*(i-1)*scfin)*sin((i-1)*scfin+PI/6+PI/2+PI/24-th);
      pFinR[2][i] = w*sin(th)+3*2*sin(3*(i-1)*scfin)*cos((i-1)*scfin+PI/6+PI/2+PI/24-th);
      pFinL[1][i] = -w*cos(th)+3*2*sin(3*(i-1)*scfin)*sin((i-1)*scfin+PI/6+PI/3+PI/2-PI/24-th); 
      pFinL[2][i] = -w*sin(th)+3*2*sin(3*(i-1)*scfin)*cos((i-1)*scfin+PI/6+PI/3+PI/2-PI/24-th);
      pEyeR[1][i] = eyey*cos(th)+eyex*sin(-th)+sc*(ecc*sin((i-1)*sceye)*cos(th+PI/2)-cos((i-1)*sceye)*sin(th+PI/2)); 
      pEyeR[2][i] = eyey*sin(th)+eyex*cos(-th)+sc*(cos(th+PI/2)*cos((i-1)*sceye)+ecc*sin((i-1)*sceye)*sin(th+PI/2));
      pEyeL[1][i] = -eyey*cos(th)+eyex*sin(-th)+sc*(ecc*sin((i-1)*sceye)*cos(th+PI/2)-cos((i-1)*sceye)*sin(th+PI/2)); 
      pEyeL[2][i] = -eyey*sin(th)+eyex*cos(-th)+sc*(cos(th+PI/2)*cos((i-1)*sceye)+ecc*sin((i-1)*sceye)*sin(th+PI/2));
    }
    pushMatrix();
    translate(width/2+5*pos[1], height/2+5*pos[2]);
    //translate(width/2,height/2);

    
    //fill(247,195,161);
    //stroke(247,195,161);
    //Cor das barbatanas
    fill(rB, gB, bB);
    stroke(rB, gB, bB);
    drawShape(lenfin, pFinR);
    drawShape(lenfin, pFinL);
    
    // Cor do corpo
    fill(r, g, b);
    stroke(r, g, b);
    beginShape();
    
    for (int i = 1; i < abs(len2); i++) {
      vertex(pHeadR[1][i]/.4, pHeadR[2][i]/.4);
    }
    for (int i = 1; i < len-15; i++) {
      vertex(pTailR[1][i]/.4, pTailR[2][i]/.4);
    }
    for (int i = len-1-15; i > 0; i--) {
      vertex(pTailL[1][i]/.4, pTailL[2][i]/.4);
    }
    for (int i = abs(len2)-1; i > 0; i--) {
      vertex(pHeadL[1][i]/.4, pHeadL[2][i]/.4);
    }
    endShape();
    //Cor da cauda
    //fill(247,195,161);
    //stroke(247,195,161);
    fill(rB, gB, bB);
    stroke(rB, gB, bB);
    beginShape();
    for (int i = len-15; i < len; i++) {
      vertex(pTailR[1][i]/.4, pTailR[2][i]/.4);
    }
    for (int i = len-1; i > len-1-15; i--) {
      vertex(pTailL[1][i]/.4, pTailL[2][i]/.4);
    }
    endShape();
    
    //Cor dos olhos
    fill(0, 0, 0);
    stroke(0, 0, 0);
    drawShape(lenfin, pEyeR);
    drawShape(lenfin, pEyeL);
    popMatrix();
  }

  void update() {


    nth = nth%(2*PI);
    th = th%(2*PI);
    if (sign(nth) == -1) {
      nth = nth+2*PI;
    }
    if (sign(th) == -1) {
      th = th+2*PI;
    }
    float diffth = nth-th;
    diffth = diffth%(2*PI);
    if (sign(diffth) == -1) {
      diffth = diffth+2*PI;
    }
    if (diffth > PI) {
      diffth = diffth-2*PI;
    }
    if (abs(diffth)>abs(dTh)) {
      th = th + sign(diffth)*dTh;
    } else {
      th = th;
    }

    nth = nth%(2*PI);
    th = th%(2*PI);
    if (sign(nth) == -1) {
      nth = nth+2*PI;
    }
    if (sign(th) == -1) {
      th = th+2*PI;
    }

    pos[1] = pos[1]+dx*cos(th+PI/2);
    pos[2] = pos[2]+dx*sin(th+PI/2);

    float[][] newtailPath = new float[4][len];
    newtailPath[1][1] = pos[1];
    newtailPath[2][1] = pos[2];
    newtailPath[3][1] = th;
    for (int i = 1; i < len-1; i++) {
      newtailPath[1][i+1] = tailPath[1][i];
      newtailPath[2][i+1] = tailPath[2][i];
      newtailPath[3][i+1] = tailPath[3][i];
    }
    tailPath = newtailPath;
  }

  void setth(float a) {
    nth = a;
  }

  float[] getpos() {
    return pos;
  }

  Double getFit() {
    return fit;
  }
  void setFit(Double f) {
    fit = f;
  }
  
   //get e set corpo
  int getR() {
    return r;
  }
  void setR(int r1) {
    r = r1 ;
  }
  int getG() {
    return g;
  }
   void setG(int g1) {
    g = g1 ;
   }
   int getB() {
    return b;
  }
  
  void setB(int b1) {
    b = b1 ;
  }
  
 
  
   //get e set barbatanas
  int getRB() {
    return rB;
  }
  void setRB(int r3) {
    rB = r3 ;
  }
  int getGB() {
    return gB;
  }
   
  void setGB(int g3) {
    gB = g3 ;
  }
   int getBB() {
    return bB;
  }
  void setBB(int b3) {
    bB = b3;
  }
  
  
  
  
  
  void setRGB(int r1, int g1, int b1,int r2, int g2, int b2) {
    r = r1;
    g = g1;
    b = b1;
    rB = r2;
    gB = g2;
    bB = b2;
    
  }
  float getth() {
    return th;
  }

  float getfisht(int a) {
    if (a == 1) {
      return fisht;
    } else {
      return randt;
    }
  }

  void setfisht(float a, float b) {
    fisht = a;
    randt = b;
  }
}

void drawShape(int len, float[][] shapes) {
  beginShape();
  for (int i = 1; i < len; i++) {
    vertex(shapes[1][i]/.4, shapes[2][i]/.4);
  }
  endShape();
}

float sign(float a) {
  if (a > 0) {
    return 1;
  } else if (a == 0) {
    return 0;
  } else {
    return -1;
  }
}
