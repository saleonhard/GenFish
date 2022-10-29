import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Random; 
import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class AG extends PApplet {

/**
 * Componente Curricular: Estrutura de Dados
 * Autor: Leonardo Aquino
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
 


class AlgGenetico {

 
  private double taxaDeCrossover;
  private double taxaDeMutacao;
  private int nMaxGeracoes;
  boolean elitismo = false;
  
  
  AlgGenetico(){
  
  }
  
  AlgGenetico(Double tm, Double tc, int  mg) {

    taxaDeCrossover = tc;
    taxaDeMutacao = tm;
    nMaxGeracoes = mg;
  }


  public void setTaxaDeMutacao(Double tm) {

    taxaDeMutacao = tm;
  }
  public void setTaxaDeCrossover(double tc) {

    taxaDeCrossover = tc;
  }

  public void setMaxGeracoes(int  max) {

    nMaxGeracoes = max;
  }
  public int getMaxGeracoes() {

    return nMaxGeracoes;
  }
  
  
  public boolean getElitismo() {

     return elitismo;
  }
  
   public void setElitismo(boolean e) {

    elitismo = e;
  }
  

  public void populacao(int tamPop) {

    
    for (int i = 0; i < tamPop; i++) {
      fishes.add( new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(6) > 1)-random(0.1f), (int)random(0, 255), (int)random(0, 255), (int)random(0, 255), (int)random(0, 255), (int)random(0, 255), (int)random(0, 255)));
    }
  }

  public  ArrayList<Fish> novaGeracao(ArrayList<Fish> populacao, boolean elitismo) {
    Random  r = new Random();
    Fish aux;
    Fish best = populacao.get(0);
    //nova população do mesmo tamanho da antiga
    ArrayList<Fish> filhos = new ArrayList(2);
    ArrayList<Fish> novaPopulacao = new ArrayList();
    ArrayList<Fish> pais = new ArrayList();
    println("inicial: "+populacao.size());


    //insere novos indivíduos na nova população, até atingir o tamanho máximo
    while (novaPopulacao.size() != numfish) {
      //seleciona os 2 pais por torneio
      if ( numfish - novaPopulacao.size() == 1) {
        pais.add(populacao.get(0));
        pais.add(best);
      } else {
        pais = selecaoTorneio();
      }

      println(novaPopulacao.size());
      println(populacao.size());
      //verifica a taxa de crossover, se sim realiza o crossover, se não, mantém os pais selecionados para a próxima geração
      if (r.nextDouble() <= taxaDeCrossover) {
        println("fez crossover");
        filhos = crossover(pais.get(0), pais.get(1));
      } else {
        filhos.add((pais.get(0)));
        filhos.add(pais.get(1));
      }

      //adiciona os filhos na nova geração
      if ( numfish - novaPopulacao.size() == 1) {
        if (r.nextBoolean()) {
          aux = filhos.get(0);
          novaPopulacao.add(new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(6) > 1)-random(0.1f), aux.getR(), aux.getG(), aux.getB(),aux.getRB(), aux.getGB(), aux.getBB()));
        } else {
          aux = filhos.get(1);
          novaPopulacao.add(new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(6) > 1)-random(0.1f), aux.getR(), aux.getG(), aux.getB(),aux.getRB(), aux.getGB(), aux.getBB()));
        }
      } else {

        aux = filhos.get(0);
        novaPopulacao.add(new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(6) > 1)-random(0.1f), aux.getR(), aux.getG(), aux.getB(),aux.getRB(), aux.getGB(), aux.getBB()));
        aux = filhos.get(1);
        novaPopulacao.add(new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(6) > 1)-random(0.1f), aux.getR(), aux.getG(), aux.getB(),aux.getRB(), aux.getGB(), aux.getBB()));
      }

      pais.clear();
      filhos.clear();
    }


    for (int i = 0; i< novaPopulacao.size(); i++) {
      Double sofreMuta = r.nextDouble();
      println("random muta :"+ sofreMuta);
      if (sofreMuta < taxaDeMutacao ) {
        println("Mutou"); 
        mutacao(novaPopulacao.get(i));
      }
    } 


    //se tiver elitismo, mantém o melhor indivíduo da geração atual
    if (elitismo) {
      //calcular o fit
      for (int i = 0; i < novaPopulacao.size(); i++) {
      Fish f = (Fish) novaPopulacao.get(i);
      f.setFit(fitness(f, pondR, pondG, pondB));
      }
      //ordena a nova população
      ordenaPopulacao(novaPopulacao);
      aux = novaPopulacao.get(0);
      println("melhor" + aux.getFit());
      if (aux.getFit() < best.getFit()) {
        novaPopulacao.add(0, best);
        println("elitismo aplicado");
      }
    }

    return novaPopulacao;
  }


  public Double fitness(Fish fish, int r, int g, int b) {
    Float   aR  = calAptidao(fish.getR(), r);
    Float   aG  = calAptidao(fish.getG(), g);
    Float   aB  = calAptidao(fish.getB(), b);
    
    Float   aRB  = calAptidao(fish.getRB(), r);
    Float   aGB  = calAptidao(fish.getGB(), g);
    Float   aBB  = calAptidao(fish.getBB(), b);

    Double fit = ajusteAp(fish, aR, aG, aB, 0) + ajusteAp(fish, aRB, aGB, aBB, 0);

    return Math.ceil(fit/2);
  }

  public Double ajusteAp( Fish fish, float aR, float aG, float aB, int cont) {



    float valores [] = {33.3f, 26.64f, 22.2f, 17.76f, 11.1f, 6.66f, 2.22f, 0.022f, 0.00f};

    println (aR+" "+aG+" "+aB);
    if (aR == valores [cont] && aG == valores [cont] && aB == valores [cont]) {

      return  Math.ceil(aR+aG+aB);
    } else if ((aR ==aG && aR == valores [cont]) || (aR == aB && aR == valores [cont]) || (aB == aG && aB == valores [cont])) {

      return    Math.ceil((aR+aG+aB)- (valores [cont]/3));
    } else if ((aR == valores [cont]) || (aG == valores [cont]) || (aB == valores [cont])) {
      return    Math.ceil((aR+aG+aB)-(valores [cont]/2));
    } else {

      println ("loop: " + valores [cont]);        
      return ajusteAp(fish, aR, aG, aB, ++cont);
    }
  }

  public float calAptidao(int x, int y) {
    int dif = Math.abs(x - y);
    float apt = 0.00f;

    if (dif == 0) {
      apt = 33.3f;
    } else if (dif >= 1 && dif <= 31) {
      apt = 26.64f;
    } else if (dif >= 32 && dif <= 63) {
      apt = 22.2f;
    } else if (dif >= 64 && dif <= 95) {
      apt = 17.76f;
    } else if (dif >= 96 && dif <= 127) {
      apt = 11.1f;
    } else if (dif >= 128 && dif <= 159) {
      apt = 6.66f;
    } else if (dif >= 160 && dif <= 191) {
      apt = 2.22f;
    } else if (dif >= 192 && dif <= 223) {
      apt = 0.022f;
    } else if (dif >= 224 && dif <= 255) {
      apt = 0.00f;
    }

    return apt;
  }

  public ArrayList <Fish>  roleta() {

    ArrayList<Integer> pesos = new ArrayList();
    int c = 0;

    while (c < 2) {

      int somatorio = 0;
      for (int i = 0; i < fishesCopia.size(); i++) {
        Fish f = (Fish) fishesCopia.get(i);

        if (f.getFit() == 0) {
          somatorio += 0;
          pesos.add(0);
        } else if (f.getFit()>= 1 && f.getFit() <= 5 ) {
          pesos.add(1);
          somatorio += 1;
        } else if (f.getFit()>= 6 && f.getFit() <= 10 ) {
          pesos.add(2);
          somatorio += 2;
        } else if (f.getFit()>= 11 && f.getFit() <= 20 ) {
          pesos.add(3);
          somatorio += 3;
        } else if (f.getFit()>= 21 && f.getFit() <= 30 ) {
          pesos.add(4);
          somatorio += 4;
        } else if (f.getFit()>= 31 && f.getFit() <= 40 ) {
          pesos.add(5);
          somatorio += 5;
        } else if (f.getFit()>= 41 && f.getFit() <= 50 ) {
          pesos.add(6);
          somatorio += 6;
        } else if (f.getFit()>= 51 && f.getFit() <= 60 ) {
          pesos.add(7);
          somatorio += 7;
        } else if (f.getFit()>= 61 && f.getFit() <= 70 ) {
          pesos.add(8);
          somatorio += 8;
        } else if (f.getFit()>= 71 && f.getFit() <= 80 ) {
          pesos.add(9);
          somatorio += 9;
        } else if (f.getFit()>= 81 && f.getFit() <= 90 ) {
          pesos.add(10);
          somatorio += 10;
        } else if (f.getFit()>= 91 && f.getFit() <= 100 ) {
          pesos.add(11);
          somatorio += 11;
        }
      }

      int r = (int)random(0, somatorio);
      int posicaoEscolhida = -1;
      println(r);
      do
      { 
        posicaoEscolhida++;
        r = r -  (int)pesos.get(posicaoEscolhida);
      } 
      while (r > 0);
      println(somatorio);
      println(r);
      println(posicaoEscolhida);
      println("--------------------------");

      Fish f = (Fish) fishesCopia.get(posicaoEscolhida);
      selecionados.add(f);
      fishesCopia.remove(posicaoEscolhida);
      c++;
    }
    return selecionados;
  }
  
  public ArrayList<Fish> selecaoTorneio() {

    ArrayList<Fish> candidatos = new ArrayList();
    ArrayList<Fish> pais = new ArrayList();

    int index;
    if (fishesCopia.size()>= 4) {
      for (int i=0; i < 4; i++) {
        index = (int)random(0, fishesCopia.size());
        candidatos.add((Fish)fishesCopia.get(index));
        fishesCopia.remove(index);
      }
      ordenaPopulacao(candidatos); 
      pais.add((Fish)candidatos.get(0));
      pais.add((Fish)candidatos.get(1));

      fishesCopia.add((Fish)candidatos.get(2));
      fishesCopia.add((Fish)candidatos.get(3));
    } else {
      ordenaPopulacao(fishesCopia);
      pais.add((Fish)fishesCopia.get(0));
      fishesCopia.remove(0);
      pais.add((Fish)fishesCopia.get(0));
      fishesCopia.remove(0);
    }



    return pais;
  }



  public ArrayList<Fish> crossover(Fish p1, Fish p2) {
    ArrayList<Fish> filhos = new ArrayList();

    Fish filho1 = new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(6) > 1)-random(0.1f), 0, 0, 0, 0, 0, 0);
    Fish filho2 = new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(6) > 1)-random(0.1f), 0, 0, 0, 0, 0, 0);
    ;
    int pntCorte = (int)random(1, 4);
    System.out.println(pntCorte); 


    switch (pntCorte) {
    case 1:
      filho1.setRGB(p2.getR(), p1.getG(), p1.getB(),p1.getRB(), p2.getGB(), p2.getBB());
      filho2.setRGB(p1.getR(), p2.getG(), p2.getB(),p2.getRB(), p1.getGB(), p1.getBB()); 
      break;
    case 2:
      filho1.setRGB(p2.getR(), p2.getG(), p1.getB(),p1.getRB(), p1.getGB(), p2.getBB());
      filho2.setRGB(p1.getR(), p1.getG(), p2.getB(),p2.getRB(), p2.getGB(), p1.getBB());
      break;
    default:
      filho1.setRGB(p1.getR(), p2.getG(), p1.getB(),p2.getRB(), p1.getGB(), p2.getBB());
      filho2.setRGB(p2.getR(), p1.getG(), p2.getB(),p1.getRB(), p2.getGB(), p1.getBB());
      break;
    }
    filhos.add(filho1);
    filhos.add(filho2);

    return filhos;
  }

  public void mutacao (Fish filho) {


    int pntMuta = (int)random(1, 4);
    int muta = (int)random(0, 255);
    int tipo = (int)random(0, 2);
    System.out.println(pntMuta); 

    switch (pntMuta) {
    case 1:

      int r = filho.getR();
      int rb = filho.getRB();
      if (tipo == 1 ) {
        r  = r + muta;
        rb  = rb + muta;
        filho.setR((r > 255? 255: r));
       
        filho.setRB((rb > 255? 255: rb));
      } else {
        r  = r - muta;
       
        rb  = rb - muta;
        filho.setR((r < 0? 0: r));
        
        filho.setRB((rb < 0? 0: rb));
      }
      break;
    case 2:
      int g = filho.getG();
      
      int gb = filho.getGB();
      if (tipo == 1 ) {
        g  = g + muta;
        
        gb  = gb + muta;
        filho.setG((g > 255? 255: g));
       
        filho.setGB((gb > 255? 255: gb));
      } else {
        g  = g - muta;
        
        gb  = gb - muta;
        filho.setG((g < 0? 0: g));
        
        filho.setG((gb < 0? 0: gb));
      }
      break;
    default:
      int b = filho.getB();
     
      int bb = filho.getBB();
      if (tipo == 1 ) {
        b  = b + muta;
        
        bb  = bb + muta;
        filho.setB((b > 255? 255: b));
       
        filho.setBB((bb > 255? 255: bb));
      } else {
        b  = b - muta;
       
        bb  = bb - muta;
        
        filho.setB((b < 0? 0: b));
       
        filho.setBB((bb < 0? 0: bb));
      }
      break;
    }
  }
  
  public void ordenaPopulacao(ArrayList <Fish> vetor) {

    for (int i = 1; i < vetor.size(); i++) {

      Fish aux = vetor.get(i);
      int j = i;

      while ((j > 0) && (vetor.get(j-1).getFit()< aux.getFit())) {
        vetor.set(j, vetor.get(j-1));
        j -= 1;
      }
      vetor.set(j, aux);
    }
  }
}
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
  float dx = 0.4f/3;
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

  public void display(float t) {
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
    float w = 0.2f*squareScale;
    for (int i = 1; i < len; i++) {
      y2[i] = (squareScale*exp(0.1f/1.25f*((i-1)*dx-30))-squareScale*exp(0.1f/1.25f*(-30)))*cos(0.2f*(i-1)*dx-phi);
      thickness[i] = w*(cos(PI/(len*dx)*(i-1)*dx)+1)*0.5f*PApplet.parseInt((i-1)<len);
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
    float ecc = 1.25f;
    float sc = 0.15f*3;
    float eyex = 2.5f*3;
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
      vertex(pHeadR[1][i]/.4f, pHeadR[2][i]/.4f);
    }
    for (int i = 1; i < len-15; i++) {
      vertex(pTailR[1][i]/.4f, pTailR[2][i]/.4f);
    }
    for (int i = len-1-15; i > 0; i--) {
      vertex(pTailL[1][i]/.4f, pTailL[2][i]/.4f);
    }
    for (int i = abs(len2)-1; i > 0; i--) {
      vertex(pHeadL[1][i]/.4f, pHeadL[2][i]/.4f);
    }
    endShape();
    //Cor da cauda
    //fill(247,195,161);
    //stroke(247,195,161);
    fill(rB, gB, bB);
    stroke(rB, gB, bB);
    beginShape();
    for (int i = len-15; i < len; i++) {
      vertex(pTailR[1][i]/.4f, pTailR[2][i]/.4f);
    }
    for (int i = len-1; i > len-1-15; i--) {
      vertex(pTailL[1][i]/.4f, pTailL[2][i]/.4f);
    }
    endShape();
    
    //Cor dos olhos
    fill(0, 0, 0);
    stroke(0, 0, 0);
    drawShape(lenfin, pEyeR);
    drawShape(lenfin, pEyeL);
    popMatrix();
  }

  public void update() {


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

  public void setth(float a) {
    nth = a;
  }

  public float[] getpos() {
    return pos;
  }

  public Double getFit() {
    return fit;
  }
  public void setFit(Double f) {
    fit = f;
  }
  
   //get e set corpo
  public int getR() {
    return r;
  }
  public void setR(int r1) {
    r = r1 ;
  }
  public int getG() {
    return g;
  }
   public void setG(int g1) {
    g = g1 ;
   }
   public int getB() {
    return b;
  }
  
  public void setB(int b1) {
    b = b1 ;
  }
  
 
  
   //get e set barbatanas
  public int getRB() {
    return rB;
  }
  public void setRB(int r3) {
    rB = r3 ;
  }
  public int getGB() {
    return gB;
  }
   
  public void setGB(int g3) {
    gB = g3 ;
  }
   public int getBB() {
    return bB;
  }
  public void setBB(int b3) {
    bB = b3;
  }
  
  
  
  
  
  public void setRGB(int r1, int g1, int b1,int r2, int g2, int b2) {
    r = r1;
    g = g1;
    b = b1;
    rB = r2;
    gB = g2;
    bB = b2;
    
  }
  public float getth() {
    return th;
  }

  public float getfisht(int a) {
    if (a == 1) {
      return fisht;
    } else {
      return randt;
    }
  }

  public void setfisht(float a, float b) {
    fisht = a;
    randt = b;
  }
}

public void drawShape(int len, float[][] shapes) {
  beginShape();
  for (int i = 1; i < len; i++) {
    vertex(shapes[1][i]/.4f, shapes[2][i]/.4f);
  }
  endShape();
}

public float sign(float a) {
  if (a > 0) {
    return 1;
  } else if (a == 0) {
    return 0;
  } else {
    return -1;
  }
}
/**
 * Componente Curricular: Estrutura de Dados
 * Autor: Leonardo Aquino
 * Data:   ‎8‎ de ‎Outubro‎ de ‎2019
 *
 * Declaro que este código foi elaborado por mim de forma individual e
 * não contém nenhum trecho de código de outro colega ou de outro autor, 
 * tais como provindos de livros e apostilas, e páginas ou documentos 
 * eletrônicos da Internet. Qualquer trecho de código de outra autoria que
 * uma citação para o  não a minha está destacado com  autor e a fonte do
 * código, e estou ciente que estes trechos não serão considerados para fins
 * de avaliação.
 */
 



ControlP5 cp5, Slider;


Textlabel labelGer, indCarac,idCorBar, cor, label2, label, fit;
String sR, sG, sB;
int velocidades [] ={15000, 1000, 0};
boolean e, p;
AlgGenetico ag = new AlgGenetico();


PShape square;
PShape aquario;
int geracao = 0;
int geracaoAtual = 0;
int timer = 0;
int vel;
boolean ativarAutomatico = false;
boolean automatico = false;
boolean iniciar = false;
boolean encontrou = false;
boolean play = false;

Fish melhor;
Fish aux;

float t;
ArrayList<ArrayList<Fish>> geracoes = new ArrayList<ArrayList<Fish>>();
ArrayList<Fish> selecionados = new ArrayList();
ArrayList fishesCopia;
ArrayList fishes;
ArrayList ranking;


float numfish;

int prox = 0 ;

int pondR, pondG, pondB;



public void settings() {
  size(1140, 670);
  

 
}



int c = color(100);
public void setup() {


  //PImage icon = loadImage("fish2.png");
  //surface.setIcon(icon);
  surface.setLocation(100, 20);

  
  colorMode(RGB, 255);

  //surface.setResizable(true); //<-- Maximizar tela

  //frameRate (-30);

  square = createShape(RECT, -30, -30, 350, displayHeight);
  square.setFill(color(0, 0, 255));
  square.setFill(color(0, 0, 0));
  square.setStroke(false);




  stroke(0);
  smooth();
  t = 0;
  fishes = new ArrayList();
  ranking = new ArrayList();
  fishesCopia = new ArrayList();



  iniciarGUI();
}


public void draw() {

  background(255, 255, 255);

  labelGer.setText("Geração: " +geracao);
  cor.setText("Cor do Ambiente: ("+pondR+","+pondG+","+pondB+")");
  vel = (int) cp5.getController("Velocidade").getValue();

  pondR =(c>>16)&255;
  pondG =(c>>8)&255;
  pondB =c&255;  



  //rectMode(CORNER);
  fill(pondR, pondG, pondB); 
  stroke(color(0));
  //rect(308, 50, displayHeight, 600, 500);
  rect(308, 50, 600, 600, 1000);


  shape(square, -20, 25);//<- barra da esquerda

  shape(square, 950, 25);//<- barra da direita

  t = t+1;


  for (int i = fishes.size()-1; i >= 0; i--) { 
    Fish fish1 = (Fish) fishes.get(i);


    float[] pos1 = fish1.getpos();
    float th = fish1.getth();
    float fisht = fish1.getfisht(1);
    float randt = fish1.getfisht(0);


    int dir;
    fisht = fisht + 1;

    fish1.display(t);


    if (fisht%(50+randt) == 0) {
      fish1.setth(th+signs(random(-1, 1))*random(PI/4));

      fisht = 0;
      randt = round(random(50));
    }
    fish1.setfisht(fisht, randt);




    aux = (Fish) ranking.get(prox);
    label2.setText("RANKING: INDIVÍDUO "+PApplet.parseInt(prox+1)).setVisible(true);
    indCarac.setText("Corpo: ("+ aux.getR()+ " , " +aux.getG()+ " , " +aux.getB()+")").setVisible(true);
    idCorBar.setText("Barbatanas: ("+ aux.getRB()+ " , " +aux.getGB()+ " , " +aux.getBB()+")").setVisible(true);
    fit.setText("FITNESS: " + aux.getFit() +"%").setVisible(true);

    aux.display(t);
    fish1.update(); 





    if ((pos1[1] > ((width/2)+100)/25)) {
      dir = dirSet();
      fish1.setth(th+signs(1));
      aux.setth(th+signs(1)); 

      // println("dir" + dir);
    }
    if ((pos1[1] < -((width/2)+100)/50)) {

      fish1.setth(th+signs(2));
      aux.setth(th+signs(2));
      //println("esq");
    }
    if ((pos1[2] > ((height/2)+100)/25)) {
      fish1.setth(th+signs(1));
      aux.setth(th+signs(1));
      //println("baixo");
    }
    if ( (pos1[2] < -((height/2)+100)/45)) {
      fish1.setth(th+signs(1));
      aux.setth(th+signs(1));

      //println("cima");
    }
  }

  if (ativarAutomatico && iniciar) {

    automatico = true;
    cp5.getController("pause").show();
  } else {
    automatico = false;
     cp5.getController("pause").hide();
  }


  if ( geracaoAtual != 0 && (geracaoAtual ==   ag.getMaxGeracoes())) {
    automatico = false;  
    cp5.getController("pause").setMousePressed(false);
  }

if(play){

  if (!encontrou && automatico && (geracaoAtual !=  ag.getMaxGeracoes() &&  ag.getMaxGeracoes() != -1)) {

    if (millis() - timer >=  ( velocidades[vel -1])) {
      println ("teste");
      print("proxima ger");
      ArrayList ng =   ag.novaGeracao(fishesCopia, ag.getElitismo());

      fishes.clear();
      for (int u = 0; u < numfish; u++) {
        Fish f = (Fish) ng.get(u);

        fishes.add( new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f),f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
      }
      for (int i = 0; i < fishes.size(); i++) {
        Fish f = (Fish) fishes.get(i);
        f.setFit(  ag.fitness(f, pondR, pondG, pondB));
        println( f.getFit() +"% - "+ f.getR()+ "," +f.getG()+ ","+f.getB());
      }

      ag.ordenaPopulacao(fishes);
      println();

      for (int i = 0; i < fishes.size(); i++) {
        Fish f = (Fish) fishes.get(i);

        if (i == 0 && f.getFit() == 100 ) {

          encontrou = true;
        }

        ranking.add(i, new Fish(-80, -39, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f), f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
        Fish ind = (Fish) ranking.get(i);
        ind.setFit(f.getFit());
        println( ind.getFit() +"% - "+ ind.getR()+ "," +ind.getG()+ ","+ind.getB());
      }

      ArrayList<Fish> copia = new ArrayList(fishes);
      geracoes.add(copia); 
      geracao = geracao +1;
      geracaoAtual = geracaoAtual +1;
      timer = millis();
    }
  }
}
}


public int dirSet() {
  int ranDir = 0;
  while (ranDir == 0) {
    ranDir = (int) random(-2, 2);
  }
  return ranDir;
}

public float signs(float a) {
  if (a > 0) {
    return 1;
  } else if (a == 0) {
    return 0;
  } else {
    return -1;
  }
}


//os inicia a interface
public void iniciarGUI() {

  cp5 = new ControlP5(this);

  PFont font  =  createFont("Arial Black", 10); 
  PFont font2  =  createFont("Arial Black", 20);
  PFont font3  =  createFont("Arial Black", 15);
  PFont font4  =  createFont("Arial Black", 11);
  PFont fontS39  =  loadFont("Stencil-39.vlw");
  PFont fontS20  =  loadFont("Stencil-20.vlw");




  labelGer = cp5.addTextlabel("geracão", "Geração: " +geracao, 488, 8).setFont(fontS39).setColor(0);
  cor = cp5.addTextlabel("corSele", "" +geracao, 810, 16).setFont(fontS20).setColor(0);
  indCarac = cp5.addTextlabel("idv", "", 80, 60).setVisible(false).setFont(font4);
  idCorBar = cp5.addTextlabel("icb", "", 80, 90).setVisible(false).setFont(font4);
  fit = cp5.addTextlabel("fitness", "", 80, 200).setVisible(false).setFont(font3);
  label  =  cp5.addTextlabel("cor", " SELECIONE A COR DO AMBIENTE: ", 1, 25).setFont(font3);
  label2  =  cp5.addTextlabel("rank", "", 60, 25).setFont(font3).setVisible(false);
  cp5.addTextlabel("ele", "ELITISMO: ", 184, 490).setFont(font);
  cp5.addTextlabel("autply", "AUTOPLAY:", 177, 524).setFont(font);
  cp5.addTextlabel("vautply", "VELOCIDADE DO AUTOPLAY:", 1, 550).setFont(font).setVisible(false);
  cp5.addTextlabel("copyright", "Desenvolvido por theBug", 50, 668).setFont(font3);
  cp5.addTextlabel("loc", "(IFBA - SNCT 2019)", 92, 690).setFont(font4);

  cp5.addColorWheel("c", 40, 55, 245).setRGB(color(0, 0, 0)).setLabelVisible(false);


  cp5.addTextfield("Tamanho da População:").setText("7").setPosition(255, 309).setSize(40, 40)
    .setFont(font).setAutoClear(false).getCaptionLabel()
    .align(ControlP5.LEFT_OUTSIDE, CENTER).getStyle().setPaddingLeft(-10);

  cp5.addTextfield("Taxa de Crossover:").setText("0.8").setPosition(255, 352).setSize(40, 40)
    .setFont(font).setAutoClear(false)
    .getCaptionLabel().align(ControlP5.LEFT_OUTSIDE, CENTER)
    .getStyle().setPaddingLeft(-10);

  cp5.addTextfield("Taxa de Mutação:").setText("0.2").setPosition(255, 395).setSize(40, 40)
    .setFont(font).setAutoClear(false)
    .getCaptionLabel().align(ControlP5.LEFT_OUTSIDE, CENTER)
    .getStyle().setPaddingLeft(-10);

  cp5.addTextfield("N° Máximo de Gerações:").setText("1000").setPosition(255, 438).setSize(40, 40)
    .setFont(font).setAutoClear(false)
    .getCaptionLabel().align(ControlP5.LEFT_OUTSIDE, CENTER)
    .getStyle().setPaddingLeft(-10);                                   

  cp5.addIcon("elitismo", 10).setPosition(245, 481) .setSize(55, 35)
    .setRoundedCorners(20).setFont(createFont("fontawesome-webfont.ttf", 40))
    .setFontIcons(0xff00f205, 0xff00f204)
    //.setScale(0.9,1)
    .setSwitch(true)
    .setColorBackground(color(255, 100))
    .hideBackground();  

  cp5.addIcon("autoplay", 10).setPosition( 245, 515) .setSize(55, 35)
    .setRoundedCorners(20).setFont(createFont("fontawesome-webfont.ttf", 40))
    .setFontIcons(0xff00f205, 0xff00f204)
    //.setScale(0.9,1)
    .setSwitch(true)
    .setColorBackground(color(255, 100))
    .hideBackground(); 

  Slider s = cp5.addSlider("Velocidade").setSliderMode(0).setVisible(false).setDecimalPrecision(0)
    .setPosition(170, 555).setSize(117, 15)
    .setRange(1, 3).setNumberOfTickMarks(3);

  cp5.getController("Velocidade").setLabelVisible(false);                                      
  s.getTickMark(0).setLabel("Lenta")
    .setPaddingX(-15).setFont( createFont("Arial Black", 9));
  s.getTickMark(1).setLabel("Média")
    .setPaddingX(-16).setFont( createFont("Arial Black", 9));
  s.getTickMark(2).setLabel("Rápida").setPaddingX(-20)
    .setFont( createFont("Arial Black", 9));

  cp5.addButton("INICIAR").setPosition(2, 600).setSize(296, 65)
    .setFont(font2).setColorBackground( color( 0, 102, 0 ))
    .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

  cp5.addButton("REINICIAR").hide().setPosition(2, 600).setSize(296, 65)
    .setFont(font2).setColorBackground( color(102, 0, 0 ))
    .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

  cp5.addButton("antGer").setImage(loadImage("Back2_50px.png")).setLabel("<<")
    .setPosition(350, 646).setSize(100, 40)
    .setFont(font).setColorBackground( color(0, 0, 102 ))
    .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

  cp5.addButton("prxGer").setImage(loadImage("Next2_50px.png")).setLabel(">>")
    .setPosition(850, 646).setSize(100, 40).setFont(font)
    .setColorBackground( color(0, 0, 102 )).getCaptionLabel()
    .align(ControlP5.CENTER, ControlP5.CENTER);

  cp5.addButton("pause").hide().setImage(loadImage("Pause_50px.png")).setLabel("pause")
    .setPosition(608, 646).setSize(100, 40).setFont(font)
    .setColorBackground( color(0, 0, 102 )).getCaptionLabel()
    .align(ControlP5.CENTER, ControlP5.CENTER);

  cp5.addButton("antInd").hide().setLabel("<<").setPosition(-1, 150)
    .setSize(30, 30).setFont(font)
    .setColorBackground( color(0, 0, 102 ))
    .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

  cp5.addButton("prxInd").hide().setLabel(">>").setPosition(270, 150)
    .setSize(30, 30).setFont(font).setColorBackground( color(0, 0, 102 ))
    .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);


  if (prox==0) {
    cp5.getController("antInd").setMousePressed(false);
  }
}
public void autoplay(boolean theValue) {
  println("autoplay:", theValue);
  cp5.getController("Velocidade").setVisible(theValue);
  cp5.getController("vautply").setVisible(theValue);
  ativarAutomatico = theValue;
  p = theValue;
  play = theValue;
}
public void elitismo(boolean theValue) {
  println("got an event for icon", theValue);
  ag.setElitismo(theValue);
  e = theValue;
}

// botão iniciar
public void INICIAR() {
  

  println("ta ativo" + cp5.getController("autoplay").isMousePressed());
  if (ativarAutomatico) {
    cp5.getController("pause").show();
  } else {
    cp5.getController("pause").hide();
  }

  String n = cp5.get(Textfield.class, "Tamanho da População:").getText();
  numfish = Integer.parseInt(n);

  int num = Integer.parseInt(n);
  String txMuta = cp5.get(Textfield.class, "Taxa de Mutação:").getText();

  ag.setTaxaDeMutacao(Double.parseDouble(txMuta));

  String txCO = cp5.get(Textfield.class, "Taxa de Crossover:").getText();
  ag.setTaxaDeCrossover(Double.parseDouble(txCO));


  String nMax = cp5.get(Textfield.class, "N° Máximo de Gerações:").getText();
  ag.setMaxGeracoes( Integer.parseInt(nMax));


  cp5.getController("Tamanho da População:").setLock(true);
  cp5.getController("Taxa de Crossover:").setLock(true);
  cp5.getController("Taxa de Mutação:").setLock(true);
  cp5.getController("N° Máximo de Gerações:").setLock(true);
  cp5.getController("elitismo").setLock(true);
  cp5.getController("c").hide();
  cp5.getController("cor").hide();
  cp5.getController("INICIAR").hide();
  cp5.getController("REINICIAR").show();
  cp5.getController("prxInd").show();
  cp5.getController("antInd").show();




  ag.populacao((int) numfish);

  //ANALISE PEIXE
  for (int i = 0; i < fishes.size(); i++) {
    Fish f = (Fish) fishes.get(i);
    f.setFit( ag.fitness(f, pondR, pondG, pondB));
    println( f.getFit() +"% - "+ f.getR()+ "," +f.getG()+ ","+f.getB());
  }

  ag.ordenaPopulacao(fishes);
  println();
  for (int i = 0; i < fishes.size(); i++) {
    Fish f = (Fish) fishes.get(i);
    ranking.add(new Fish(-80, -39, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f),f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
    Fish ind = (Fish) ranking.get(i);
    ind.setFit(f.getFit());
    println( ind.getFit() +"% - "+ ind.getR()+ "," +ind.getG()+ ","+ind.getB());
  }

  ArrayList<Fish> copia = new ArrayList(fishes);
  geracoes.add(copia);  
  fishesCopia = fishes;
  geracao = geracao +1;
  geracaoAtual = geracaoAtual +1;

  if ( geracaoAtual== 1 && ativarAutomatico) {
    if (millis() - timer >=  ( velocidades[vel -1])) {
      automatico = true;
      timer = millis();
    }
  }
   cp5.getController("autoplay").update();
  iniciar = true;
}


//botão reiniciar
public void REINICIAR() {

  cp5.getController("Tamanho da População:").setLock(false);
  cp5.getController("Taxa de Crossover:").setLock(false);
  cp5.getController("Taxa de Mutação:").setLock(false);
  cp5.getController("N° Máximo de Gerações:").setLock(false);
  cp5.getController("elitismo").setLock(false);
  cp5.getController("c").setLock(false);
  cp5.getController("INICIAR").show();
  cp5.getController("REINICIAR").hide();
  cp5.getController("prxInd").hide();
  cp5.getController("antInd").hide();
  cp5.getController("c").show();
  cp5.getController("cor").show();
  cp5.getController("rank").hide();
  idCorBar.hide();

  fishes.clear();
  ranking.clear();
  fishesCopia.clear();
  selecionados.clear();
  geracoes.clear();
  geracao = 0;
  geracaoAtual =0;
  ag.setMaxGeracoes(0);
  cp5.getController("elitismo");
  encontrou =  false;
  ativarAutomatico = p;
  ag.setElitismo(e);
  cp5.getController("idv").setVisible(false);
  cp5.getController("icc").setVisible(false);
  cp5.getController("icb").setVisible(false);
  if ( cp5.getController("pause").getLabel().equals("pause")) {
    cp5.getController("pause").hide();
  } else {
    cp5.getController("pause").setImage(loadImage("Pause_50px.png"));
    cp5.getController("pause").setLabel("pause");
  }
  cp5.getController("pause").hide();
  play = false;
  iniciar = false;
  
  timer = 0;
}

public void  antInd() {


  if (prox == 0) {
    cp5.getController("prxInd").setMousePressed(false);

    print("block voltar");
  } else {
    print(prox);
    prox--;
  }
}
public void  prxInd() {

  if (prox == numfish-1) {
    cp5.getController("antInd").setMousePressed(false);

    print("block ir");
  } else {
    print(prox);
    prox++;
  }
}
public void prxGer() {

  if ( !encontrou && geracao != ag.getMaxGeracoes()) {

    print(fishes.size());


    print("proxima ger");
    if (geracao == geracaoAtual) {

      ArrayList ng =  ag.novaGeracao(fishesCopia, ag.getElitismo());

      fishes.clear();
      for (int u = 0; u < numfish; u++) {
        Fish f = (Fish) ng.get(u);
        fishes.add( new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f), f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
      }
      for (int i = 0; i < fishes.size(); i++) {
        Fish f = (Fish) fishes.get(i);
        f.setFit( ag.fitness(f, pondR, pondG, pondB));
        println( f.getFit() +"% - "+ f.getR()+ "," +f.getG()+ ","+f.getB());
      }


      ArrayList<Fish> copia = new ArrayList(fishes);
      geracoes.add(copia); 
      geracao = geracao +1;
      geracaoAtual = geracaoAtual +1;
    } else {

      print("nao encontrou : px ger");
      geracao = geracao + 1;
      ArrayList gAnterior =  geracoes.get(geracao -1);

      fishes.clear();
      for (int u = 0; u < numfish; u++) {
        Fish f = (Fish) gAnterior.get(u);
        fishes.add(u, new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f),f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
      }
    }

    for (int i = 0; i < fishes.size(); i++) {
      Fish f = (Fish) fishes.get(i);
      f.setFit( ag.fitness(f, pondR, pondG, pondB));
      println( f.getFit() +"% - "+ f.getR()+ "," +f.getG()+ ","+f.getB());
    }

    ag.ordenaPopulacao(fishes);
    println();
    for (int i = 0; i < fishes.size(); i++) {
      Fish f = (Fish) fishes.get(i);

      ranking.add(i, new Fish(-80, -39, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f), f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
      Fish ind = (Fish) ranking.get(i);

      if (i == 0 && f.getFit() == 100 ) {

        encontrou = true;
        println("encontrou");
      }

      ind.setFit(f.getFit());
      println( ind.getFit() +"% - "+ ind.getR()+ "," +ind.getG()+ ","+ind.getB());
    }
  } else if ( encontrou && geracao <= geracaoAtual) {

    print("encontrou : px ger");
    geracao = geracao + 1;
    ArrayList gAnterior =  geracoes.get(geracao -1);

    fishes.clear();
    for (int u = 0; u < numfish; u++) {
      Fish f = (Fish) gAnterior.get(u);
      fishes.add(u, new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f), f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));

      ag.ordenaPopulacao(fishes);
      println();

      for (int i = 0; i < fishes.size(); i++) {
        f = (Fish) fishes.get(i);

        ranking.add(i, new Fish(-80, -39, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f), f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
        Fish ind = (Fish) ranking.get(i);

        if (i == 0 && f.getFit() == 100 ) {

          encontrou = true;
          println("encontrou");
        }

        ind.setFit(f.getFit());
        println( ind.getFit() +"% - "+ ind.getR()+ "," +ind.getG()+ ","+ind.getB());
      }
    }
  }
}


//botão pause/play
public void pause() {


  if ( cp5.getController("pause").getLabel().equals("pause")) {
    print("pause");
    play = false;
    cp5.getController("pause").setImage(loadImage("Play_50px.png"));
    cp5.getController("pause").setLabel("play");
  } else {
    print("play");
    play = true;
    cp5.getController("pause").setImage(loadImage("Pause_50px.png"));
    cp5.getController("pause").setLabel("pause");
  }
}

public void antGer() {


  if (geracao != 1) {
    print("anterior ger");
    geracao = geracao - 1;
    ArrayList gAnterior =  geracoes.get(geracao - 1);

    fishes.clear();
    for (int u = 0; u < numfish; u++) {
      Fish f = (Fish) gAnterior.get(u);
      fishes.add(u, new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f), f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
    }
    for (int i = 0; i < fishes.size(); i++) {
      Fish f = (Fish) fishes.get(i);
      f.setFit( ag.fitness(f, pondR, pondG, pondB));
      println( f.getFit() +"% - "+ f.getR()+ "," +f.getG()+ ","+f.getB());
    }

    ag.ordenaPopulacao(fishes);
    println();
    for (int i = 0; i < fishes.size(); i++) {
      Fish f = (Fish) fishes.get(i);

      ranking.add(i, new Fish(-80, -39, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f), f.getR(), f.getG(), f.getB(),  f.getRB(), f.getGB(), f.getBB()));
      Fish ind = (Fish) ranking.get(i);
      ind.setFit(f.getFit());
      println( ind.getFit() +"% - "+ ind.getR()+ "," +ind.getG()+ ","+ind.getB());
    }
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "AG" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
