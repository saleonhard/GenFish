/** //<>//
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
 
import java.util.Random;
import java.math.BigDecimal;
import java.math.RoundingMode;


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
      fishes.add( new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0, int(random(6) > 1)-random(0.1), (int)random(0, 255), (int)random(0, 255), (int)random(0, 255), (int)random(0, 255), (int)random(0, 255), (int)random(0, 255)));
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
          novaPopulacao.add(new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0, int(random(6) > 1)-random(0.1), aux.getR(), aux.getG(), aux.getB(),aux.getRB(), aux.getGB(), aux.getBB()));
        } else {
          aux = filhos.get(1);
          novaPopulacao.add(new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0, int(random(6) > 1)-random(0.1), aux.getR(), aux.getG(), aux.getB(),aux.getRB(), aux.getGB(), aux.getBB()));
        }
      } else {

        aux = filhos.get(0);
        novaPopulacao.add(new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0, int(random(6) > 1)-random(0.1), aux.getR(), aux.getG(), aux.getB(),aux.getRB(), aux.getGB(), aux.getBB()));
        aux = filhos.get(1);
        novaPopulacao.add(new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0, int(random(6) > 1)-random(0.1), aux.getR(), aux.getG(), aux.getB(),aux.getRB(), aux.getGB(), aux.getBB()));
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
  
  Double fitness(Fish fish, int r, int g, int b) {
    
    double labC[] = rgbToLab(fish.getR(), fish.getG(),fish.getB()); 
    double labB[] = rgbToLab(fish.getRB(), fish.getGB(),fish.getBB()); 
    double labA[] = rgbToLab(r,g,b); 
    
    BigDecimal resultCorpo = new BigDecimal(calculateDeltaE(labC[0], labC[1], labC[2], labA[0], labA[1], labA[2])).setScale(4, RoundingMode.HALF_EVEN);
    BigDecimal resultBarba = new BigDecimal(calculateDeltaE(labB[0], labB[1], labB[2], labA[0], labA[1], labA[2])).setScale(4, RoundingMode.HALF_EVEN);

    BigDecimal fit  = new BigDecimal(((Math.abs(resultCorpo.doubleValue() - 100) * 1.5) + Math.abs(resultBarba.doubleValue() - 100))/2.5).setScale(2, RoundingMode.HALF_EVEN);
    
    return fit.doubleValue();
  }

  Double ajusteAp( Fish fish, float aR, float aG, float aB, int cont) {



    float valores [] = {33.3, 26.64, 22.2, 17.76, 11.1, 6.66, 2.22, 0.022, 0.00};

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

  float calAptidao(int x, int y) {
    int dif = Math.abs(x - y);
    float apt = 0.00;

    if (dif == 0) {
      apt = 33.3;
    } else if (dif >= 1 && dif <= 31) {
      apt = 26.64;
    } else if (dif >= 32 && dif <= 63) {
      apt = 22.2;
    } else if (dif >= 64 && dif <= 95) {
      apt = 17.76;
    } else if (dif >= 96 && dif <= 127) {
      apt = 11.1;
    } else if (dif >= 128 && dif <= 159) {
      apt = 6.66;
    } else if (dif >= 160 && dif <= 191) {
      apt = 2.22;
    } else if (dif >= 192 && dif <= 223) {
      apt = 0.022;
    } else if (dif >= 224 && dif <= 255) {
      apt = 0.00;
    }

    return apt;
  }
  
  // Calcula a distancia entre duas cores
  public double calculateDeltaE(double L1, double a1, double b1, double L2, double a2, double b2) {
    double Lmean = (L1 + L2) / 2.0; //ok
    double C1 =  Math.sqrt(a1*a1 + b1*b1); //ok
    double C2 =  Math.sqrt(a2*a2 + b2*b2); //ok
    double Cmean = (C1 + C2) / 2.0; //ok
    
    double G =  ( 1 - Math.sqrt( Math.pow(Cmean, 7) / (Math.pow(Cmean, 7) + Math.pow(25, 7)) ) ) / 2; //ok
    double a1prime = a1 * (1 + G); //ok
    double a2prime = a2 * (1 + G); //ok
    
    double C1prime =  Math.sqrt(a1prime*a1prime + b1*b1); //ok
    double C2prime =  Math.sqrt(a2prime*a2prime + b2*b2); //ok
    double Cmeanprime = (C1prime + C2prime) / 2; //ok 
    
    double h1prime =  Math.atan2(b1, a1prime) + 2*Math.PI * (Math.atan2(b1, a1prime)<0 ? 1 : 0);
    double h2prime =  Math.atan2(b2, a2prime) + 2*Math.PI * (Math.atan2(b2, a2prime)<0 ? 1 : 0);
    double Hmeanprime =  ((Math.abs(h1prime - h2prime) > Math.PI) ? (h1prime + h2prime + 2*Math.PI) / 2 : (h1prime + h2prime) / 2); //ok
    
    double T =  1.0 - 0.17 * Math.cos(Hmeanprime - Math.PI/6.0) + 0.24 * Math.cos(2*Hmeanprime) + 0.32 * Math.cos(3*Hmeanprime + Math.PI/30) - 0.2 * Math.cos(4*Hmeanprime - 21*Math.PI/60); //ok
    
    double deltahprime =  ((Math.abs(h1prime - h2prime) <= Math.PI) ? h2prime - h1prime : (h2prime <= h1prime) ? h2prime - h1prime + 2*Math.PI : h2prime - h1prime - 2*Math.PI); //ok
    
    double deltaLprime = L2 - L1; //ok
    double deltaCprime = C2prime - C1prime; //ok
    double deltaHprime =  2.0 * Math.sqrt(C1prime*C2prime) * Math.sin(deltahprime / 2.0); //ok
    double SL =  1.0 + ( (0.015*(Lmean - 50)*(Lmean - 50)) / (Math.sqrt( 20 + (Lmean - 50)*(Lmean - 50) )) ); //ok
    double SC =  1.0 + 0.045 * Cmeanprime; //ok
    double SH =  1.0 + 0.015 * Cmeanprime * T; //ok
    
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
  
  // Converte de RGB para LAB
  public  double[] rgbToLab(int R, int G, int B) {

    double r, g, b, X, Y, Z, xr, yr, zr;

    // D65/2°
    double Xr = 95.047;  
    double Yr = 100.0;
    double Zr = 108.883;


    // --------- RGB to XYZ ---------//

    r = R/255.0;
    g = G/255.0;
    b = B/255.0;

    if (r > 0.04045)
        r = Math.pow((r+0.055)/1.055,2.4);
    else
        r = r/12.92;

    if (g > 0.04045)
        g = Math.pow((g+0.055)/1.055,2.4);
    else
        g = g/12.92;

    if (b > 0.04045)
        b = Math.pow((b+0.055)/1.055,2.4);
    else
        b = b/12.92 ;

    r*=100;
    g*=100;
    b*=100;

    X =  0.4124*r + 0.3576*g + 0.1805*b;
    Y =  0.2126*r + 0.7152*g + 0.0722*b;
    Z =  0.0193*r + 0.1192*g + 0.9505*b;


    // --------- XYZ to Lab --------- //

    xr = X/Xr;
    yr = Y/Yr;
    zr = Z/Zr;

    if ( xr > 0.008856 )
        xr =  (float) Math.pow(xr, 1/3.);
    else
        xr = (float) ((7.787 * xr) + 16 / 116.0);

    if ( yr > 0.008856 )
        yr =  (float) Math.pow(yr, 1/3.);
    else
        yr = (float) ((7.787 * yr) + 16 / 116.0);

    if ( zr > 0.008856 )
        zr =  (float) Math.pow(zr, 1/3.);
    else
        zr = (float) ((7.787 * zr) + 16 / 116.0);


    double[] lab = new double[3];

    lab[0] = (116*yr)-16;
    lab[1] = 500*(xr-yr); 
    lab[2] = 200*(yr-zr); 

    return lab;

 } 

  ArrayList <Fish>  roleta() {

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
  
  ArrayList<Fish> selecaoTorneio() {

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



  ArrayList<Fish> crossover(Fish p1, Fish p2) {
    ArrayList<Fish> filhos = new ArrayList();

    Fish filho1 = new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0, int(random(6) > 1)-random(0.1), 0, 0, 0, 0, 0, 0);
    Fish filho2 = new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0, int(random(6) > 1)-random(0.1), 0, 0, 0, 0, 0, 0);
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

  void mutacao (Fish filho) {


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
