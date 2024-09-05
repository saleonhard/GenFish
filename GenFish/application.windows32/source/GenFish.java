import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Random; 
import java.math.BigDecimal; 
import java.math.RoundingMode; 
import controlP5.*; 
import javax.swing.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class GenFish extends PApplet {

/** //<>// //<>//
 * Componente Curricular: Estrutura de Dados
 * Autor: Leonardo Aquino GitHub: https://github.com/saleonhard
 * Data:   ‎8‎ de ‎Outubro‎ de ‎2019
 * Atualizado em: 6‎ de ‎Novembro‎ de ‎2022
 * 
 * Projeto iniciamente criado para SNCT 2019 do IFBA - Campus Feira de Santana. v 2.0
 * Atualizado para a 1º Edição do BSI Integra (De 7 a 8 de Novembro de 2022) v 2.1
 *
 * Declaro que este código foi elaborado por mim de forma individual e
 * não contém nenhum trecho de código de outro colega ou de outro autor, 
 * tais como provindos de livros e apostilas, e páginas ou documentos 
 * eletrônicos da Internet. Qualquer trecho de código de outra autoria que
 * uma citação para o  não a minha está destacado com  autor e a fonte do
 * código.
 *
 *
 * A representação visual simula a evolução de um conjunto de peixes (por geração).
 * A cada geração os indivíduos “evoluem” buscando uma melhor adaptação a cor do ambiente.
 * Será possível definir: a cor do ambiente, tamanho da população,taxa de crossover,taxa de mutação,elitismo e número máximo de gerações.
 *
 *
 * Sobre o Arquivo: Implementa os conceitos de Algoritmos Genéticos. (Classe AlgGenetico) 
 *
 *
 */
 





class AlgGenetico {

 
  private double taxaDeCrossover;
  private double taxaDeMutacao;
  private int nMaxGeracoes;
  boolean elitismo = false;
  

  
  
  AlgGenetico(){
  
  }
  //construtor
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
  
  
  // Iniciar daqui
  
  
  
  //Calcula o fit de cada inviduo ultilizando a distancia entre as cores baseado no diagrama cie <-- 
  public Double fitness(Fish fish, int r, int g, int b) { // A função recebe as cores do ambiente
    
    double labC[] = rgbToLab(fish.getR(), fish.getG(),fish.getB()); 
    double labB[] = rgbToLab(fish.getRB(), fish.getGB(),fish.getBB()); 
    double labA[] = rgbToLab(r,g,b); 
    
    BigDecimal resultCorpo = new BigDecimal(calculateDeltaE(labC[0], labC[1], labC[2], labA[0], labA[1], labA[2])).setScale(4, RoundingMode.HALF_EVEN);
    BigDecimal resultBarba = new BigDecimal(calculateDeltaE(labB[0], labB[1], labB[2], labA[0], labA[1], labA[2])).setScale(4, RoundingMode.HALF_EVEN);

    BigDecimal fit  = new BigDecimal(((Math.abs(resultCorpo.doubleValue() - 100) * 1.5f) + Math.abs(resultBarba.doubleValue() - 100))/2.5f).setScale(2, RoundingMode.HALF_EVEN);
    
    return fit.doubleValue();
  }

  
  // Calcula a distancia entre duas cores <-- Extraido de: https://github.com/wuchubuzai/OpenIMAJ/blob/master/image/image-processing/src/main/java/org/openimaj/image/analysis/colour/CIEDE2000.java  --- Autor : Jonathon Hare (jsh2@ecs.soton.ac.uk)
  public double calculateDeltaE(double L1, double a1, double b1, double L2, double a2, double b2) {
    double Lmean = (L1 + L2) / 2.0f; //ok
    double C1 =  Math.sqrt(a1*a1 + b1*b1); //ok
    double C2 =  Math.sqrt(a2*a2 + b2*b2); //ok
    double Cmean = (C1 + C2) / 2.0f; //ok
    
    double G =  ( 1 - Math.sqrt( Math.pow(Cmean, 7) / (Math.pow(Cmean, 7) + Math.pow(25, 7)) ) ) / 2; //ok
    double a1prime = a1 * (1 + G); //ok
    double a2prime = a2 * (1 + G); //ok
    
    double C1prime =  Math.sqrt(a1prime*a1prime + b1*b1); //ok
    double C2prime =  Math.sqrt(a2prime*a2prime + b2*b2); //ok
    double Cmeanprime = (C1prime + C2prime) / 2; //ok 
    
    double h1prime =  Math.atan2(b1, a1prime) + 2*Math.PI * (Math.atan2(b1, a1prime)<0 ? 1 : 0);
    double h2prime =  Math.atan2(b2, a2prime) + 2*Math.PI * (Math.atan2(b2, a2prime)<0 ? 1 : 0);
    double Hmeanprime =  ((Math.abs(h1prime - h2prime) > Math.PI) ? (h1prime + h2prime + 2*Math.PI) / 2 : (h1prime + h2prime) / 2); //ok
    
    double T =  1.0f - 0.17f * Math.cos(Hmeanprime - Math.PI/6.0f) + 0.24f * Math.cos(2*Hmeanprime) + 0.32f * Math.cos(3*Hmeanprime + Math.PI/30) - 0.2f * Math.cos(4*Hmeanprime - 21*Math.PI/60); //ok
    
    double deltahprime =  ((Math.abs(h1prime - h2prime) <= Math.PI) ? h2prime - h1prime : (h2prime <= h1prime) ? h2prime - h1prime + 2*Math.PI : h2prime - h1prime - 2*Math.PI); //ok
    
    double deltaLprime = L2 - L1; //ok
    double deltaCprime = C2prime - C1prime; //ok
    double deltaHprime =  2.0f * Math.sqrt(C1prime*C2prime) * Math.sin(deltahprime / 2.0f); //ok
    double SL =  1.0f + ( (0.015f*(Lmean - 50)*(Lmean - 50)) / (Math.sqrt( 20 + (Lmean - 50)*(Lmean - 50) )) ); //ok
    double SC =  1.0f + 0.045f * Cmeanprime; //ok
    double SH =  1.0f + 0.015f * Cmeanprime * T; //ok
    
    double deltaTheta =  (30 * Math.PI / 180) * Math.exp(-((180/Math.PI*Hmeanprime-275)/25)*((180/Math.PI*Hmeanprime-275)/25));
    double RC =  (2 * Math.sqrt(Math.pow(Cmeanprime, 7) / (Math.pow(Cmeanprime, 7) + Math.pow(25, 7))));
    double RT =  (-RC * Math.sin(2 * deltaTheta));
    
    double KL = 1;
    double KC = 1;
    double KH = 1;
    
    double deltaE = Math.sqrt(
        ((deltaLprime/(KL*SL)) * (deltaLprime/(KL*SL))) +
        ((deltaCprime/(KC*SC)) * (deltaCprime/(KC*SC))) +
        ((deltaHprime/(KH*SH)) * (deltaHprime/(KH*SH))) +
        (RT * (deltaCprime/(KC*SC)) * (deltaHprime/(KH*SH)))
        );
      
    return deltaE;
  }
  
  // Converte de RGB para LAB <--  Extraido de: https://stackoverflow.com/questions/4593469/java-how-to-convert-rgb-color-to-cie-lab  --- Autor : Thanasis1101
  public  double[] rgbToLab(int R, int G, int B) {

    double r, g, b, X, Y, Z, xr, yr, zr;

    // D65/2°
    double Xr = 95.047f;  
    double Yr = 100.0f;
    double Zr = 108.883f;


    // --------- RGB to XYZ ---------//

    r = R/255.0f;
    g = G/255.0f;
    b = B/255.0f;

    if (r > 0.04045f)
        r = Math.pow((r+0.055f)/1.055f,2.4f);
    else
        r = r/12.92f;

    if (g > 0.04045f)
        g = Math.pow((g+0.055f)/1.055f,2.4f);
    else
        g = g/12.92f;

    if (b > 0.04045f)
        b = Math.pow((b+0.055f)/1.055f,2.4f);
    else
        b = b/12.92f ;

    r*=100;
    g*=100;
    b*=100;

    X =  0.4124f*r + 0.3576f*g + 0.1805f*b;
    Y =  0.2126f*r + 0.7152f*g + 0.0722f*b;
    Z =  0.0193f*r + 0.1192f*g + 0.9505f*b;


    // --------- XYZ to Lab --------- //

    xr = X/Xr;
    yr = Y/Yr;
    zr = Z/Zr;

    if ( xr > 0.008856f )
        xr =  (float) Math.pow(xr, 1/3.f);
    else
        xr = (float) ((7.787f * xr) + 16 / 116.0f);

    if ( yr > 0.008856f )
        yr =  (float) Math.pow(yr, 1/3.f);
    else
        yr = (float) ((7.787f * yr) + 16 / 116.0f);

    if ( zr > 0.008856f )
        zr =  (float) Math.pow(zr, 1/3.f);
    else
        zr = (float) ((7.787f * zr) + 16 / 116.0f);


    double[] lab = new double[3];

    lab[0] = (116*yr)-16;
    lab[1] = 500*(xr-yr); 
    lab[2] = 200*(yr-zr); 

    return lab;

 } 

  
  //Realiza a seleção dos pais da proxima geração.  <-- Este tipo de seleção foi escolhida, pois segundo a literatura esta permite uma maior miscigenação da população (evitar maximos locais) 
  public ArrayList<Fish> selecaoTorneio() {

    ArrayList<Fish> candidatos = new ArrayList();
    ArrayList<Fish> pais = new ArrayList();
    int numPais = 0;
    int index;
    //while(numPais < 2 ){
      
      print("entrou");
        if (fishesCopia.size()> 3) { // caso a população seja maior ou  igual a quatro: quatro individuos são selcionados aleatoriamente e add a lista de possiveis pais (candidatos) 
          for (int i=0; i < 4; i++) {
            index = (int)random(0, fishesCopia.size());
            candidatos.add((Fish)fishesCopia.get(index));
            fishesCopia.remove(index); // <-- remove os caditatos da lista principal 
          }
          ordenaPopulacao(candidatos);       // ordena os candidatos e paga os dois melhores dessa rodada <-- melhoria: pegar apenas um por vez
          pais.add((Fish)candidatos.get(0)); // 
          pais.add((Fish)candidatos.get(1)); // adiciona os dois melhores a lista de pais 
          
          //fishesCopia.add((Fish)candidatos.get(1));
          fishesCopia.add((Fish)candidatos.get(2));
          fishesCopia.add((Fish)candidatos.get(3));// adiciona outros dois a lista principal novamente (não foi a vez deles :( )
          
         // numPais ++;
          
        }
        else {
          // caso a população seja menor que quatro
          println("sempre cai aqui" + fishesCopia.size() );
          //ordenaPopulacao(fishesCopia);          // ordena (sem ordenação melhora miscigenação)
          pais.add((Fish)fishesCopia.get(0));    // pega o melhor e adiciona a lista de pais 
          fishesCopia.remove(0);
          pais.add((Fish)fishesCopia.get(1));
          fishesCopia.remove(1);
          println("sempre cai aqui 2 " + fishesCopia.size() );
        }
    //}


    return pais; //retona os pais da vez (coitados)
  }


 // faz o cruzamento dos genes dos individuos, baseado no ponto de corte
  public ArrayList<Fish> crossover(Fish p1, Fish p2) {
    ArrayList<Fish> filhos = new ArrayList();

    Fish filho1 = new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(6) > 1)-random(0.1f), 0, 0, 0, 0, 0, 0);
    Fish filho2 = new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(6) > 1)-random(0.1f), 0, 0, 0, 0, 0, 0);
    
    int pntCorte = (int)random(1, 4);
    println("Ponto de corte: " + pntCorte); 


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
  
  // Realiza a mutação de um dos genes responsaveis pela cor do individuo. Esta mutação pode ocorrer tanto no corpo quanto na barbatana
  public void mutacao (Fish filho) {


    int pntMuta = (int)random(1, 4);
    int mutaC = (int)random(0, 255);
    int mutaB = (int)random(0, 255);
    int tipo = (int)random(0, 2);
   
    println("Ponto de mutação :"+ pntMuta);
    println("Tipo de mutação :"+ tipo);
    
    
    switch (pntMuta) {
    case 1:
    
      if (tipo == 1 ) {
        
        int r = filho.getR();
    
        while( r == mutaC){
         mutaC = (int)random(0, 255); 
        }
        
        println("Mutação Corpo (R):"+ mutaC);
        filho.setR(mutaC);
       
        
      } else {
        
        int rb = filho.getRB();
        
        while( rb == mutaB){
         mutaB = (int)random(0, 255); 
        }
        
        println("Mutação Barbatana (R):"+ mutaB);
        filho.setRB(mutaB);
        
      }
      break;
    case 2:
    
      
      if (tipo == 1 ) {
        
        int gb = filho.getGB();
        
        while( gb == mutaB){
         mutaB = (int)random(0, 255); 
        }
        
        println("Mutação Barbatana (G):"+ mutaB);
        filho.setGB(mutaB);
        
      } else {
        
        int g = filho.getG();
        
        while( g == mutaC){
         mutaC = (int)random(0, 255); 
        }
        
        println("Mutação Corpo (G):"+ mutaC);
        filho.setG(mutaC);  
        
      }
      break;
   case 3:
      
      if (tipo == 1 ) {
        
        int b = filho.getB();
        while( b == mutaC){
         mutaC = (int)random(0, 255); 
        }
        
        println("Mutação Corpo (B):"+ mutaC);
        filho.setB(mutaC);
     
        
      } else {
        
        int bb = filho.getBB();
         while( bb == mutaB){
         mutaB = (int)random(0, 255); 
        }
        
        println("Mutação Barbatana (B):"+ mutaB);
        filho.setBB(mutaB);
        
      }
      break;
    }
  }
  
  // ordena a população de acordo com o fit
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
  
    
  
  
  
  
  
  
  
 //primeira geração
  public void populacao(int tamPop) {

    
    for (int i = 0; i < tamPop; i++) {
      fishes.add( new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(6) > 1)-random(0.1f), (int)random(0, 255), (int)random(0, 255), (int)random(0, 255), (int)random(0, 255), (int)random(0, 255), (int)random(0, 255)));
    }
  }
  //criando uma nova geração
  public  ArrayList<Fish> novaGeracao(ArrayList<Fish> populacao, boolean elitismo) {
    Random  r = new Random();
    Fish aux;
    Fish best = populacao.get(0);
    Fish worst = populacao.get(PApplet.parseInt(numfish-1));
    
    //nova população do mesmo tamanho da antiga
    ArrayList<Fish> filhos = new ArrayList(2);
    ArrayList<Fish> novaPopulacao = new ArrayList();
    ArrayList<Fish> pais = new ArrayList();
    
    //println("Individuos : "+ populacao.size());


    //insere novos indivíduos na nova população, até atingir o tamanho máximo
    while (novaPopulacao.size() != numfish) {
      //seleciona os 2 pais por torneio
      if ( numfish - novaPopulacao.size() == 1) { // caso a população tenha um numero impar: pega dois individuos (o melhor e o pior) da população atual
      
      
        //println("Individuos restantes: "+ fishesCopia.size());
        ///println("Individuos : "+ populacao.size());
        
        //int index = (int)random(0, populacao.size());
        
        pais.add(best);
        pais.add(worst); //<-- Uma opção: pegar o individuo restante e outro aleatoriamente.
        
      } else {
        pais = selecaoTorneio(); // caso a população tenha um numero impar: pega dois individuos aleatorios da população atual
        
      }

     
      //verifica a taxa de crossover, se sim realiza o crossover, se não, mantém os pais selecionados para a próxima geração
      if (r.nextDouble() <= taxaDeCrossover) {
        println("Fez crossover :" + pais.get(0).getFit());
        println("Fez crossover :" + pais.get(1).getFit());
        
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

    // gera um valor aleatorio, caso ele seja menor que a taxa de mutação o idividuo sofrerar mutação
    for (int i = 0; i< novaPopulacao.size(); i++) {
      Double sofreMuta = r.nextDouble();
      println("Mutação random :"+ sofreMuta);
      
      if (sofreMuta < taxaDeMutacao ) {
         
        mutacao(novaPopulacao.get(i));
        println("Mutou");
      }
    } 


    //se tiver elitismo, mantém o melhor indivíduo da geração atual - substituido o pior da nova geração<-- https://repositorio.ufu.br/bitstream/123456789/14632/1/NGLMalaquiasDISPRT.pdf pags 51-52 
    if (elitismo) {
      //calcular o fit
      for (int i = 0; i < novaPopulacao.size(); i++) {
      Fish f = (Fish) novaPopulacao.get(i);
      f.setFit(fitness(f, pondR, pondG, pondB));
      }
      //ordena a nova população
      ordenaPopulacao(novaPopulacao);
      
    
      aux = novaPopulacao.get(0);
      novaPopulacao.add(PApplet.parseInt(numfish - 1), best);
      println("Elitismo aplicado");
     
      
      //if (aux.getFit() < best.getFit()) {
      //  novaPopulacao.add(0, best);
      //  println("Elitismo aplicado");
      //}
    }

    return novaPopulacao;
  }

 

  
  
  //----OS MÉTODOS/FUNCÕES ABAIXO FORAM ATUALIZADOS----///
  
  
  
  //ArrayList <Fish>  roleta() {

  //  ArrayList<Integer> pesos = new ArrayList();
  //  int c = 0;

  //  while (c < 2) {

  //    int somatorio = 0;
  //    for (int i = 0; i < fishesCopia.size(); i++) {
  //      Fish f = (Fish) fishesCopia.get(i);

  //      if (f.getFit() == 0) {
  //        somatorio += 0;
  //        pesos.add(0);
  //      } else if (f.getFit()>= 1 && f.getFit() <= 5 ) {
  //        pesos.add(1);
  //        somatorio += 1;
  //      } else if (f.getFit()>= 6 && f.getFit() <= 10 ) {
  //        pesos.add(2);
  //        somatorio += 2;
  //      } else if (f.getFit()>= 11 && f.getFit() <= 20 ) {
  //        pesos.add(3);
  //        somatorio += 3;
  //      } else if (f.getFit()>= 21 && f.getFit() <= 30 ) {
  //        pesos.add(4);
  //        somatorio += 4;
  //      } else if (f.getFit()>= 31 && f.getFit() <= 40 ) {
  //        pesos.add(5);
  //        somatorio += 5;
  //      } else if (f.getFit()>= 41 && f.getFit() <= 50 ) {
  //        pesos.add(6);
  //        somatorio += 6;
  //      } else if (f.getFit()>= 51 && f.getFit() <= 60 ) {
  //        pesos.add(7);
  //        somatorio += 7;
  //      } else if (f.getFit()>= 61 && f.getFit() <= 70 ) {
  //        pesos.add(8);
  //        somatorio += 8;
  //      } else if (f.getFit()>= 71 && f.getFit() <= 80 ) {
  //        pesos.add(9);
  //        somatorio += 9;
  //      } else if (f.getFit()>= 81 && f.getFit() <= 90 ) {
  //        pesos.add(10);
  //        somatorio += 10;
  //      } else if (f.getFit()>= 91 && f.getFit() <= 100 ) {
  //        pesos.add(11);
  //        somatorio += 11;
  //      }
  //    }

  //    int r = (int)random(0, somatorio);
  //    int posicaoEscolhida = -1;
  //    println(r);
  //    do
  //    { 
  //      posicaoEscolhida++;
  //      r = r -  (int)pesos.get(posicaoEscolhida);
  //    } 
  //    while (r > 0);
  //    println(somatorio);
  //    println(r);
  //    println(posicaoEscolhida);
  //    println("--------------------------");

  //    Fish f = (Fish) fishesCopia.get(posicaoEscolhida);
  //    selecionados.add(f);
  //    fishesCopia.remove(posicaoEscolhida);
  //    c++;
  //  }
  //  return selecionados;
  //}
  
  
  //---CALCULA O FITNESS--- ESTE METODO DE CALCULAR A DIFERENÇA ENTRE AS CORES FOI DESENVOLVIDO POR MIM, MAS POR ALGUM MOTIVO 
  // COM A AMBIENTE BRANCO (255,255,255) A ADPTAÇÃO DOS PEIXES TORNA-SE POSSIVEL EM POUQUÍSSIMAS INTERAÇÕES. OU SEJA ESTE É TENDENCIOSO.
  // (EXECUTÁVEL QUE ULTILIZA ESSE METODO DISPONIVEL NA PASTA "GenFish v2.0"  ////
  
  //Double fitness(Fish fish, int r, int g, int b) {
  //  Float   aR  = calAptidao(fish.getR(), r);
  //  Float   aG  = calAptidao(fish.getG(), g);
  //  Float   aB  = calAptidao(fish.getB(), b);
    
  //  Float   aRB  = calAptidao(fish.getRB(), r);
  //  Float   aGB  = calAptidao(fish.getGB(), g);
  //  Float   aBB  = calAptidao(fish.getBB(), b);

  //  Double fit = ajusteAp(fish, aR, aG, aB, 0) + ajusteAp(fish, aRB, aGB, aBB, 0);

  //  return Math.ceil(fit/2);
  //}
  
  //AUXILIARIES PARA CALCULAR O FITNESS//
  
  //Double ajusteAp( Fish fish, float aR, float aG, float aB, int cont) {



  //  float valores [] = {33.3, 26.64, 22.2, 17.76, 11.1, 6.66, 2.22, 0.022, 0.00};

  //  println (aR+" "+aG+" "+aB);
  //  if (aR == valores [cont] && aG == valores [cont] && aB == valores [cont]) {

  //    return  Math.ceil(aR+aG+aB);
  //  } else if ((aR ==aG && aR == valores [cont]) || (aR == aB && aR == valores [cont]) || (aB == aG && aB == valores [cont])) {

  //    return    Math.ceil((aR+aG+aB)- (valores [cont]/3));
  //  } else if ((aR == valores [cont]) || (aG == valores [cont]) || (aB == valores [cont])) {
  //    return    Math.ceil((aR+aG+aB)-(valores [cont]/2));
  //  } else {

          
  //    return ajusteAp(fish, aR, aG, aB, ++cont);
  //  }
  //}

  //float calAptidao(int x, int y) {
  //  int dif = Math.abs(x - y);
  //  float apt = 0.00;

  //  if (dif == 0) {
  //    apt = 33.3;
  //  } else if (dif >= 1 && dif <= 31) {
  //    apt = 26.64;
  //  } else if (dif >= 32 && dif <= 63) {
  //    apt = 22.2;
  //  } else if (dif >= 64 && dif <= 95) {
  //    apt = 17.76;
  //  } else if (dif >= 96 && dif <= 127) {
  //    apt = 11.1;
  //  } else if (dif >= 128 && dif <= 159) {
  //    apt = 6.66;
  //  } else if (dif >= 160 && dif <= 191) {
  //    apt = 2.22;
  //  } else if (dif >= 192 && dif <= 223) {
  //    apt = 0.022;
  //  } else if (dif >= 224 && dif <= 255) {
  //    apt = 0.00;
  //  }

  //  return apt;
  //}
}
/**
 * Componente Curricular: Estrutura de Dados
 * Autor:  Nicholas Tang
 * Adaptado por: Leonardo Aquino GitHub: https://github.com/saleonhard
 * Código Fonte Disponível em: <https://www.openprocessing.org/sketch/20105/> 
 * Data:   ‎8‎ de ‎Outubro‎ de ‎2019
 * Atualizado em: 6‎ de ‎Novembro‎ de ‎2022
 *
 * Projeto iniciamente criado para SNCT 2019 do IFBA - Campus Feira de Santana. v 2.0
 * Atualizado para a 1º Edição do BSI Integra (De 7 a 8 de Novembro de 2022) v 2.1
 *
 * Declaro que este código foi adaptado por mim de forma individual e que
 * o autor autor original está destacado bem como a fonte do
 * código.
 *
 * A representação visual simula a evolução de um conjunto de peixes (por geração).
 * A cada geração os indivíduos “evoluem” buscando uma melhor adaptação a cor do ambiente.
 * Será possível definir: a cor do ambiente, tamanho da população,taxa de crossover,taxa de mutação,elitismo e número máximo de gerações.
 *
 * Sobre o Arquivo: Modela os aspectos físicos dos peixes. (A classe Fish)
 *
 *
 *
 */

///--- ABAIXO COMENTARIOS DO AUTOR DO CÓDIGO ---

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
/** //<>//
 * Componente Curricular: Estrutura de Dados
 * Autor: Leonardo Aquino GitHub: https://github.com/saleonhard
 * Data:   ‎8‎ de ‎Outubro‎ de ‎2019
 * Atualizado em: 6‎ de ‎Novembro‎ de ‎2022
 *
 *
 * Projeto iniciamente criado para SNCT 2019 do IFBA - Campus Feira de Santana. v 2.0
 * Atualizado para a 1º Edição do BSI Integra (De 7 a 8 de Novembro de 2022) v 2.1
 *
 * Declaro que este código foi elaborado por mim de forma individual e
 * não contém nenhum trecho de código de outro colega ou de outro autor, 
 * tais como provindos de livros e apostilas, e páginas ou documentos 
 * eletrônicos da Internet. Qualquer trecho de código de outra autoria que
 * uma citação para o  não a minha está destacado com  autor e a fonte do
 * código.
 *
 *
 * A representação visual simula a evolução de um conjunto de peixes (por geração).
 * A cada geração os indivíduos “evoluem” buscando uma melhor adaptação a cor do ambiente.
 * Será possível definir: a cor do ambiente, tamanho da população,taxa de crossover,taxa de mutação,elitismo e número máximo de gerações.
 * 
 *
 *
 * Sobre o Arquivo: Cria a interface grafica e instacia as demais Classes.
 *
 */
 




ControlP5 cp5, Slider;


Textlabel labelGer, indCarac,idCorBar, cor, label2, label, fit;
String sR, sG, sB;
int velocidades [] ={15000, 1000, 0}; //<-- velocidades do autoplay
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
ArrayList<ArrayList<Fish>> geracoes = new ArrayList<ArrayList<Fish>>(); //<-- Guarda todas as gerações permitindo navegar entre elas
ArrayList<Fish> selecionados = new ArrayList(); //<-- Pais selecionas apos o torneio
ArrayList fishesCopia; //<-- Copia da geração atual (uso: manipulação no torneio)
ArrayList fishes; //<-- Geração atual 
ArrayList ranking; // <-- Adivinha? kkk


float numfish;

int prox = 0 ;

int pondR, pondG, pondB;


//configurando tamanho da janela
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
  cor.setText("COR DO AMBIENTE:\n      ("+pondR+","+pondG+","+pondB+")");
  vel = (int) cp5.getController("Velocidade").getValue();

  pondR =(c>>16)&255;
  pondG =(c>>8)&255;
  pondB =c&255;  



  //rectMode(CORNER);
 // strokeWeight(5);
  fill(pondR, pondG, pondB); 
  stroke(0);
  
  //rect(308, 50, displayHeight, 600, 500);
  rect(308, 50, 600, 570, 1000);






  shape(square, -20, 25);//<- barra da esquerda

  shape(square, 950, 25);//<- barra da direita

  t = t+1;


  for (int i = fishes.size()-1; i >= 0; i--) { 
    Fish fish1 = (Fish) fishes.get(i);


    float[] pos1 = fish1.getpos();
    float th = fish1.getth();
    float fisht = fish1.getfisht(1);
    float randt = fish1.getfisht(0);


    //int dir;
    fisht = fisht + 1;

    fish1.display(t);


    if (fisht%(50+randt) == 0) {
      fish1.setth(th+signs(random(-1, 1))*random(PI/4));

      fisht = 0;
      randt = round(random(50));
    }
    fish1.setfisht(fisht, randt);




    aux = (Fish) ranking.get(prox);
    label2.setText("RANKING:\n\n\nINDIVÍDUO "+PApplet.parseInt(prox+1)).setVisible(true);
    indCarac.setText("C: ("+ aux.getR()+ " , " +aux.getG()+ " , " +aux.getB()+")").setVisible(true);
    idCorBar.setText("B: ("+ aux.getRB()+ " , " +aux.getGB()+ " , " +aux.getBB()+")").setVisible(true);
    fit.setText("FITNESS: " + aux.getFit() +"%").setVisible(true);

    aux.display(t);
    fish1.update(); 





    if ((pos1[1] > ((width/2)+100)/25)) {
      //dir = dirSet();
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
  
// se autoplay estiver ativo  
if(play && numfish > 1  && numfish < 21){
  //
  if (!encontrou && automatico && (geracaoAtual !=  ag.getMaxGeracoes() &&  ag.getMaxGeracoes() != -1)) {

    if (millis() - timer >=  ( velocidades[vel -1])) {
      
      println("----------------------\nGeração: " +geracao);
      //criando nova geração automaticamente
      ArrayList ng =   ag.novaGeracao(fishesCopia, ag.getElitismo());
      
       //configurando os peixes com os parametros da nova geração ---ponto de partida, cor do corpo e da barbatana e etc..
      fishes.clear();
      for (int u = 0; u < numfish; u++) {
        Fish f = (Fish) ng.get(u);

        fishes.add( new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f),f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
      }
       //calculando o fit de cada individuo da geração e "setando" seu fit ..
      for (int i = 0; i < fishes.size(); i++) {
        Fish f = (Fish) fishes.get(i);
        f.setFit(  ag.fitness(f, pondR, pondG, pondB));
        println( f.getFit() +"% - "+ f.getR()+ "," +f.getG()+ ","+f.getB());
      }
      
      //oredena de acordo com o fit
      ag.ordenaPopulacao(fishes);
      println();

      for (int i = 0; i < fishes.size(); i++) {
        Fish f = (Fish) fishes.get(i);

        if (i == 0 && f.getFit() == 100 ) {

          encontrou = true;
         
        }
        //exibir peixes no ranking
        ranking.add(i, new Fish(98, 4, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f), f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
        Fish ind = (Fish) ranking.get(i);
        ind.setFit(f.getFit());
        println( ind.getFit() +"% - "+ ind.getR()+ "," +ind.getG()+ ","+ind.getB());
      }
      
      //adicionando a geração anterior a lista de gerações ---assim será possivel navegar entre as gerações
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
  //PFont fontS20  =  loadFont("Stencil-20.vlw");




  labelGer = cp5.addTextlabel("geracão", "Geração: " +geracao, 488, 8).setFont(fontS39).setColor(0);
  cor = cp5.addTextlabel("corSele", "" +geracao, 950, 16).setFont(font3).setColor(255);
  
  //label2  =  cp5.addTextlabel("rank", "", 60, 25).setFont(font3).setVisible(false);
  //indCarac = cp5.addTextlabel("idv", "", 80, 60).setVisible(false).setFont(font4);
  //idCorBar = cp5.addTextlabel("icb", "", 80, 90).setVisible(false).setFont(font4);
  //fit = cp5.addTextlabel("fitness", "", 80, 200).setVisible(false).setFont(font3);
  label  =  cp5.addTextlabel("cor", " SELECIONE A COR DO AMBIENTE: ", 1, 25).setFont(font3);
  
  //RANKING
  label2  =  cp5.addTextlabel("rank", "", 979, 176).setFont(font3).setVisible(false);
  indCarac = cp5.addTextlabel("idv", "", 970, 250).setVisible(false).setFont(font4);
  idCorBar = cp5.addTextlabel("icb", "", 970, 260).setVisible(false).setFont(font4);
  fit = cp5.addTextlabel("fitness", "", 970, 400).setVisible(false).setFont(font3);
  
  cp5.addTextlabel("ele", "ELITISMO: ", 184, 490).setFont(font);
  cp5.addTextlabel("autply", "AUTOPLAY:", 177, 524).setFont(font);
  cp5.addTextlabel("vautply", "VELOCIDADE DO AUTOPLAY:", 1, 550).setFont(font).setVisible(false);
  
  cp5.addTextlabel("copyright", "Desenvolvido por: \nLeonardo Aquino", 946, 600).setFont(font3);
  cp5.addTextlabel("loc", "(IFBA - BSI INTEGRA 2022)", 940, 638).setFont(font4);

  cp5.addColorWheel("c", 40, 55, 245).setRGB(color(205, 254, 628)).setLabelVisible(false);


  cp5.addTextfield("Tamanho da População:").setText("7").setPosition(255, 309).setSize(40, 40)
    .setFont(font).setAutoClear(false).getCaptionLabel()
    .align(ControlP5.LEFT_OUTSIDE, CENTER).getStyle().setPaddingLeft(-10);

  cp5.addTextfield("Taxa de Crossover:").setText("0.8").setPosition(255, 352).setSize(40, 40)
    .setFont(font).setAutoClear(false)
    .getCaptionLabel().align(ControlP5.LEFT_OUTSIDE, CENTER)
    .getStyle().setPaddingLeft(-10);

  cp5.addTextfield("Taxa de Mutação:").setText("0.001").setPosition(255, 395).setSize(40, 40) //<--  A taxa de mutação deve pequena: 0,001 ≤ taxa ≤ 0,1
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
    .setPosition(499, 620).setSize(100, 40)
    .setFont(font).setColorBackground( color(0, 0, 102 ))
    .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

  cp5.addButton("prxGer").setImage(loadImage("Next2_50px.png")).setLabel(">>")
    .setPosition(675, 620).setSize(100, 40).setFont(font)
    .setColorBackground( color(0, 0, 102 )).getCaptionLabel()
    .align(ControlP5.CENTER, ControlP5.CENTER);

  cp5.addButton("pause").hide().setImage(loadImage("Pause_50px.png")).setLabel("pause")
    .setPosition(590, 620).setSize(100, 40).setFont(font)
    .setColorBackground( color(0, 0, 102 )).getCaptionLabel()
    .align(ControlP5.CENTER, ControlP5.CENTER);

  cp5.addButton("antInd").hide().setLabel("<<").setPosition(920, 340)
    .setSize(30, 30).setFont(font)
    .setColorBackground( color(0, 0, 102 ))
    .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);

  cp5.addButton("prxInd").hide().setLabel(">>").setPosition(1110, 340)
    .setSize(30, 30).setFont(font).setColorBackground( color(0, 0, 102 ))
    .getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);


  if (prox==0) {
    cp5.getController("antInd").setMousePressed(false);
  }
}

//configura comportamento do botão autoplay
public void autoplay(boolean theValue) {
  
  cp5.getController("Velocidade").setVisible(theValue);
  cp5.getController("vautply").setVisible(theValue);
  ativarAutomatico = theValue;
  p = theValue;
  play = theValue;
}

//setando o elitismo
public void elitismo(boolean theValue) {
  
  ag.setElitismo(theValue);
  e = theValue;
  
}

// botão iniciar
public void INICIAR() {
  

  
  if (ativarAutomatico) {
    cp5.getController("pause").show();
  } else {
    cp5.getController("pause").hide();
  }

  String n = cp5.get(Textfield.class, "Tamanho da População:").getText();
  numfish = Integer.parseInt(n);

  //int num = Integer.parseInt(n);
  String txMuta = cp5.get(Textfield.class, "Taxa de Mutação:").getText();

  ag.setTaxaDeMutacao(Double.parseDouble(txMuta));

  String txCO = cp5.get(Textfield.class, "Taxa de Crossover:").getText();
  ag.setTaxaDeCrossover(Double.parseDouble(txCO));


  String nMax = cp5.get(Textfield.class, "N° Máximo de Gerações:").getText();
  ag.setMaxGeracoes( Integer.parseInt(nMax));


  if (numfish < 2 || numfish > 20 ){
    
   print( "população muito pequena!"); 
   println("Button B pressed");
   loop();  
   displayMessageBoxOKCancel("Tamanho da população inválido!\n Os tamanhos recomendados são entre 2 e 20");
  
   
  } 
  else{
    
  cp5.getController("Tamanho da População:").setLock(true);
  cp5.getController("Taxa de Crossover:").setLock(true);
  cp5.getController("Taxa de Mutação:").setLock(true);
  cp5.getController("N° Máximo de Gerações:").setLock(true);
  cp5.getController("elitismo").setLock(true);
  //cp5.getController("c").hide();
  //cp5.getController("cor").hide();
  cp5.getController("c").setLock(true);
  cp5.getController("cor").setLock(true);
  cp5.getController("INICIAR").hide();
  cp5.getController("REINICIAR").show();
  cp5.getController("prxInd").show();
  cp5.getController("antInd").show();
    
  
    
  //criando a primeira geração
  ag.populacao((int) numfish);

  //analisa o fit dos individuos da primeira geração
  for (int i = 0; i < fishes.size(); i++) {
    Fish f = (Fish) fishes.get(i);
    f.setFit( ag.fitness(f, pondR, pondG, pondB));
    
  }
  
  //ordena de acordo com o fit
  ag.ordenaPopulacao(fishes);
  println();
  println("-- PRIMEIRA GERAÇÃO --");
  for (int i = 0; i < fishes.size(); i++) {
    Fish f = (Fish) fishes.get(i);
    //exibir peixes no ranking
    ranking.add(new Fish(98, 4, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f),f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
    Fish ind = (Fish) ranking.get(i);
    ind.setFit(f.getFit());
    
    println( ind.getFit() +"% - "+ ind.getR()+ "," +ind.getG()+ ","+ind.getB());
  }
  
  //adiciona a primeira geração a lista de gerações -- com essa lista é possivel navegar entre as gerações 
  ArrayList<Fish> copia = new ArrayList(fishes);
  geracoes.add(copia);  
  fishesCopia = fishes; //<-- atualiza a copia da população atual
  geracao = geracao +1;
  geracaoAtual = geracaoAtual +1;
  
  //cofiguando autoplay
  if ( geracaoAtual== 1 && ativarAutomatico) {
    if (millis() - timer >=  ( velocidades[vel -1])) {
      automatico = true;
      timer = millis();
    }
  }
   cp5.getController("autoplay").update();
  iniciar = true;
 }
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
  fit.hide();
  

  
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

//INDIVIDUO ANTERIOR -- NO RANKING
public void  antInd() {


  if (prox == 0) {
    
    //INATIVAR BOTÃO CASO SEJA O PRIMEIRO
    cp5.getController("prxInd").setMousePressed(false);
  } else {
    
    prox--;
  }
}

//PROXIMO INDIVIDUO -- NO RANKING
public void  prxInd() {

  if (prox == numfish-1) {
    //INATIVAR BOTÃO CASO SEJA O ULTIMO
    cp5.getController("antInd").setMousePressed(false);

  } else {
    
    prox++;
  }
}


//NAVEGAR ENTRE AS GERAÇÕES -- PROXIMA GERAÇÃO///
public void prxGer() {
  // navegar entre as gerações --- caso não tenha encontrado o melhor e a geração não seja maior que o numero maximo de gerações
  if ( !encontrou && geracao != ag.getMaxGeracoes()) {

    

    //verifica se o numero geração atual é igual a numero ultima geração criada, caso positivo será criada uma nova geração,
    // caso contrario continua navegando entre as geraçoes já criadas
    
    println("----------------------\nGeração: " +geracao);
    if (geracao == geracaoAtual) {
      
      //criando uma nova nova geração
      ArrayList ng =  ag.novaGeracao(fishesCopia, ag.getElitismo());


      //configurando os peixes com os parametros da nova geração ---ponto de partida, cor do corpo e da barbatana e etc..
      fishes.clear();
      for (int u = 0; u < numfish; u++) {
        Fish f = (Fish) ng.get(u);
        fishes.add( new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f), f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
      }
      
      //calculando o fit de cada individuo da geração e "setando" seu fit ..
      for (int i = 0; i < fishes.size(); i++) {
        Fish f = (Fish) fishes.get(i);
        f.setFit( ag.fitness(f, pondR, pondG, pondB));
      }

      //adicionando a geração anterior a lista de gerações ---assim será possivel navegar entre as gerações
      ArrayList<Fish> copia = new ArrayList(fishes);
      geracoes.add(copia); 
      geracao = geracao +1;
      geracaoAtual = geracaoAtual +1;
      
    } else { // andando pelas gerações

      print ("entrou aquiii");
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
      
    }

    ag.ordenaPopulacao(fishes);
    println();
    for (int i = 0; i < fishes.size(); i++) {
      Fish f = (Fish) fishes.get(i);
      //exibir peixes no ranking
      ranking.add(i, new Fish(98, 4, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f), f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
      Fish ind = (Fish) ranking.get(i);

      if (i == 0 && f.getFit() == 100 ) {

        encontrou = true;
        
      }

      ind.setFit(f.getFit());
      println( ind.getFit() +"% - "+ ind.getR()+ "," +ind.getG()+ ","+ind.getB());
    }
    // navegar entre as gerações --- caso tenha encontrado o melhor e a geração seja menor ou igual a geração atual
  } else if ( encontrou && geracao < geracaoAtual) {

     
    geracao = geracao + 1;
    ArrayList gProx =  geracoes.get(geracao - 1);
    
    
    fishes.clear();
    for (int u = 0; u < numfish; u++) {
      Fish f = (Fish) gProx.get(u);
      println("Fit: "+f.getFit());
      fishes.add(u, new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f), f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
    }
    
    //ag.ordenaPopulacao(fishes);
      println();
      
        //recalcula o fit para exibir no ranking --
    for (int i = 0; i < fishes.size(); i++) {
      Fish f = (Fish) fishes.get(i);
      
      f.setFit( ag.fitness(f, pondR, pondG, pondB));
      
    }
    //ordenando peixes
    ag.ordenaPopulacao(fishes);
    println();
    for (int i = 0; i < fishes.size(); i++) {
      Fish f = (Fish) fishes.get(i);
      //exibir peixes no ranking
      ranking.add(i, new Fish(98, 4, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f), f.getR(), f.getG(), f.getB(),  f.getRB(), f.getGB(), f.getBB()));
      Fish ind = (Fish) ranking.get(i);
      ind.setFit(f.getFit());
      println( ind.getFit() +"% - "+ ind.getR()+ "," +ind.getG()+ ","+ind.getB());
    }

   }
  
}



//NAVEGAR ENTRE AS GERAÇÕES -- GERAÇÃO ANTERIOR///
public void antGer() {


  if (geracao != 1) {
   
    geracao = geracao - 1;
    println("\nGeração: " + geracao);
    
    ArrayList gAnterior =  geracoes.get(geracao - 1);

    fishes.clear();
    for (int u = 0; u < numfish; u++) {
      Fish f = (Fish) gAnterior.get(u);
      // possivel altereção: adcionar atributo Fit no objeto Fish, com isso não será preciso recalcular fit para exebir no ranking
      fishes.add(u, new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f), f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
      
    }
    
    //recalcula o fit para exibir no ranking --
    for (int i = 0; i < fishes.size(); i++) {
      Fish f = (Fish) fishes.get(i);
      
      f.setFit( ag.fitness(f, pondR, pondG, pondB));
      
    }
    //ordenando peixes
    ag.ordenaPopulacao(fishes);
    println();
    for (int i = 0; i < fishes.size(); i++) {
      Fish f = (Fish) fishes.get(i);
      //exibir peixes no ranking
      ranking.add(i, new Fish(98, 4, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0f, PApplet.parseInt(random(5) > 1)-random(0.1f), f.getR(), f.getG(), f.getB(),  f.getRB(), f.getGB(), f.getBB()));
      Fish ind = (Fish) ranking.get(i);
      ind.setFit(f.getFit());
      println( ind.getFit() +"% - "+ ind.getR()+ "," +ind.getG()+ ","+ind.getB());
    }
  }
}

//botão pause/play do autoplay
public void pause() {


  if ( cp5.getController("pause").getLabel().equals("pause")) {
    //Pausa o autoplay
    play = false;
    cp5.getController("pause").setImage(loadImage("Play_50px.png"));
    cp5.getController("pause").setLabel("play");
  } else {
    //Dá play o autoplay
    play = true;
    cp5.getController("pause").setImage(loadImage("Pause_50px.png"));
    cp5.getController("pause").setLabel("pause");
  }
}



public void displayMessageBoxOKCancel(String abc){
    switch(JOptionPane.showConfirmDialog(null,abc,"Aviso",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE)) {
      case JOptionPane.OK_OPTION:
        println("yes!");
        break;
      default:
        println("no!");
    };
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "GenFish" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
