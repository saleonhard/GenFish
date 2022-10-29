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
 * de avaliação.
 */
 
import controlP5.*;


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
void setup() {


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


void draw() {

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
    label2.setText("RANKING:\n\n\nINDIVÍDUO "+int(prox+1)).setVisible(true);
    indCarac.setText("C: ("+ aux.getR()+ " , " +aux.getG()+ " , " +aux.getB()+")").setVisible(true);
    idCorBar.setText("B: ("+ aux.getRB()+ " , " +aux.getGB()+ " , " +aux.getBB()+")").setVisible(true);
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

        fishes.add( new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0, int(random(5) > 1)-random(0.1),f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
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

        ranking.add(i, new Fish(98, 4, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0, int(random(5) > 1)-random(0.1), f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
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


int dirSet() {
  int ranDir = 0;
  while (ranDir == 0) {
    ranDir = (int) random(-2, 2);
  }
  return ranDir;
}

float signs(float a) {
  if (a > 0) {
    return 1;
  } else if (a == 0) {
    return 0;
  } else {
    return -1;
  }
}


//os inicia a interface
void iniciarGUI() {

  cp5 = new ControlP5(this);

  PFont font  =  createFont("Arial Black", 10); 
  PFont font2  =  createFont("Arial Black", 20);
  PFont font3  =  createFont("Arial Black", 15);
  PFont font4  =  createFont("Arial Black", 11);
  PFont fontS39  =  loadFont("Stencil-39.vlw");
  PFont fontS20  =  loadFont("Stencil-20.vlw");




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
  
  cp5.addTextlabel("copyright", "Desenvolvido por theBug", 925, 618).setFont(font3);
  cp5.addTextlabel("loc", "(IFBA - SNCT 2019)", 970, 638).setFont(font4);

  cp5.addColorWheel("c", 40, 55, 245).setRGB(color(205, 254, 628)).setLabelVisible(false);


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
    .setFontIcons(#00f205, #00f204)
    //.setScale(0.9,1)
    .setSwitch(true)
    .setColorBackground(color(255, 100))
    .hideBackground();  

  cp5.addIcon("autoplay", 10).setPosition( 245, 515) .setSize(55, 35)
    .setRoundedCorners(20).setFont(createFont("fontawesome-webfont.ttf", 40))
    .setFontIcons(#00f205, #00f204)
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
void autoplay(boolean theValue) {
  println("autoplay:", theValue);
  cp5.getController("Velocidade").setVisible(theValue);
  cp5.getController("vautply").setVisible(theValue);
  ativarAutomatico = theValue;
  p = theValue;
  play = theValue;
}
void elitismo(boolean theValue) {
  println("got an event for icon", theValue);
  ag.setElitismo(theValue);
  e = theValue;
}

// botão iniciar
void INICIAR() {
  

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
  //cp5.getController("c").hide();
  //cp5.getController("cor").hide();
  cp5.getController("c").setLock(true);
  cp5.getController("cor").setLock(true);
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
    ranking.add(new Fish(98, 4, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0, int(random(5) > 1)-random(0.1),f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
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
void REINICIAR() {

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

void  antInd() {


  if (prox == 0) {
    cp5.getController("prxInd").setMousePressed(false);

    print("block voltar");
  } else {
    print(prox);
    prox--;
  }
}
void  prxInd() {

  if (prox == numfish-1) {
    cp5.getController("antInd").setMousePressed(false);

    print("block ir");
  } else {
    print(prox);
    prox++;
  }
}
void prxGer() {

  if ( !encontrou && geracao != ag.getMaxGeracoes()) {

    print(fishes.size());


    print("proxima ger");
    if (geracao == geracaoAtual) {

      ArrayList ng =  ag.novaGeracao(fishesCopia, ag.getElitismo());

      fishes.clear();
      for (int u = 0; u < numfish; u++) {
        Fish f = (Fish) ng.get(u);
        fishes.add( new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0, int(random(5) > 1)-random(0.1), f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
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
        fishes.add(u, new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0, int(random(5) > 1)-random(0.1),f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
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

      ranking.add(i, new Fish(98, 4, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0, int(random(5) > 1)-random(0.1), f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
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
      fishes.add(u, new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0, int(random(5) > 1)-random(0.1), f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));

      ag.ordenaPopulacao(fishes);
      println();

      for (int i = 0; i < fishes.size(); i++) {
        f = (Fish) fishes.get(i);

        ranking.add(i, new Fish(98, 4, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0, int(random(5) > 1)-random(0.1), f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
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
void pause() {


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

void antGer() {


  if (geracao != 1) {
    print("anterior ger");
    geracao = geracao - 1;
    ArrayList gAnterior =  geracoes.get(geracao - 1);

    fishes.clear();
    for (int u = 0; u < numfish; u++) {
      Fish f = (Fish) gAnterior.get(u);
      fishes.add(u, new Fish(0, 0, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0, int(random(5) > 1)-random(0.1), f.getR(), f.getG(), f.getB(), f.getRB(), f.getGB(), f.getBB()));
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

      ranking.add(i, new Fish(98, 4, (3*PI)/2, (6+floor(random(0, 3))*20)/360.0, int(random(5) > 1)-random(0.1), f.getR(), f.getG(), f.getB(),  f.getRB(), f.getGB(), f.getBB()));
      Fish ind = (Fish) ranking.get(i);
      ind.setFit(f.getFit());
      println( ind.getFit() +"% - "+ ind.getR()+ "," +ind.getG()+ ","+ind.getB());
    }
  }
}
