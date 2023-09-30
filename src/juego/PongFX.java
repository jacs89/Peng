package juego;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

  

/**
 * El clásico Pong de dos jugadores donde el jugador de la izquierda mueve la
 * pala o raqueta con las teclas W y S, y el jugador de la derecha la mueve con
 * las teclas de arriba y abajo del cursor.
 * 
 * @author profesorado
 */
public class PongFX extends Application {
    //-----------------------------------------------------------------------------------------------
    //                      ATRIBUTOS ESTATICOS
    //-----------------------------------------------------------------------------------------------
    
    //                      PANTALLA
    
    /** Tamaño de la pantalla en ancho */
    public static final int ANCHURA_PANEL = 800;
    
    /** Tamaño de la pantalla en alto */
    public static final int ALTURA_PANEL = 600;
    
    
    //                      PALAS    
    
            //  DIMENSIONES
    /** Anchura de cada pala */
    public static final int ANCHURA_PALA = 7 ;
    
    /** Altura de cada pala */
    public static final int ALTURA_PALA = 50 ;
    
            //  POSICIÓN
    /** Coordenadas del eje X donde se ubica la pala izquierda */
    public static final int POSICION_X_PALA_IZQUIERDA = 100 ;
    
    /** Coordenadas del eje X donde se ubica laa pala derecha */
    public static final int POSICION_X_PALA_DERECHA = 700 ;
    
    
    //                      PELOTA
    
    /** Tamaño de la pelota  */
    public static final int TAMANO_PELOTA = 6 ;
    
    
    //                      MARCADOR
    
            //  NÚMERO DE VIDAS
    /** Número de vidas, en este caso de juegos para terminar la partida */
    public static final int NUM_VIDAS = 3 ;
    
    
            //  TAMAÑO
    /** Tamaño del texto para los marcadores */
    public static final int TEXT_SIZE = 30 ;
     
    
    //                      SONIDOS   
    
            //  SONIDO PUNTO
    /** URL del sonido que se reproduce al marcar punto alguno de los dos jugadores */
    public static final String SONIDO_PUNTO = "src/recursos/336913__the-sacha-rush__coin1.wav" ;
    
            //  SONIDO REBOTE
    /** URL del sonido que se reproduce al rebotar la pelota */
    public static final String SONIDO_REBOTE = "src/recursos/rebote.wav" ;
    
            //  SONIDO FINAL
    /** URL del sonido que se reproduce al terminar la partida */
    public static final String SONIDO_GAMEOVER = "src/recursos/pacman-dies.wav" ;
    public static final String ICONO = "file:src/recursos/palas.jpg" ;
   
    
    
    //-----------------------------------------------------------------------------------------------
    //                      ATRIBUTOS DE OBJETO
    //-----------------------------------------------------------------------------------------------
    
    //                      PELOTA
    
    // Coordenada X de la pelota. La primera vez la pelota se iniciará desde la izquierda más un poco
    private int posXpelota = POSICION_X_PALA_IZQUIERDA + 10 ;
    // La pelota irá avanzando en X según este atributo
    private int velocidadActualX = 3 ;
    
    // Coordenada Y de la pelota. 
    private int posYpelota = 30 ;
    // La pelota irá avanzando en Y según este atributo
    private int velocidadActualY = 3 ;
    
    
    //                      PALAS 
    
    /** Atributos de posición y velocidad de la pala derecha */
    private int posicionYpalaDerecha = 200 ;
    private int velocidadPalaDerecha = 0 ;
    
    /** Atributos de posición y velocidad de la pala izquierda */
    private int posicionYpalaIzquierda = 200 ;
    private int velocidadPalaIzquierda = 0 ;
    
    
    //                      MARCADOR
    
    // Contador de puntos de cada jugador
    private int puntosJugadorDerecho ;
    private int puntosJugadorIzquierdo ;
    
    // Texto para los puntos de cada jugador
    private Text puntosDer, puntosIzq ;
   
  
        
    //-----------------------------------------------------------------------------------------------
    //                      METODOS
    //-----------------------------------------------------------------------------------------------
    
    /**
     * Cuando uno de los dos jugadores gane el punto,se ejecuta este método para
     * sacar la pelota de nuevo, estableciendo los valores posXpelota,
     * posYpelota y velocidadActualX
     */
    private void iniciarSiguientePunto() {
        // Establecer la posición X de la pelota como POSICION_X_PALA_IZQUIERDA + 10 por ejemplo
        posXpelota = POSICION_X_PALA_IZQUIERDA + 10;
        
        // Establecer la velocidad actual X a 3
        velocidadActualX = 3;
        velocidadActualY = 3;
        
        // Establecer la posición Y de la pelota como un número aleatorio entero entre 0 y ALTURA_PANEL      
        posYpelota = (int) (Math.random() * ALTURA_PANEL);    
    }
   
    
    
    /**
     * Dibuja la red y los marcadores de cada jugador
     * 
     * @param root Panel donde se representa el juego
     */
    private void dibujarRedYmarcador(Pane panelDeJuego) {
        
        //      DIBUJAR LA RED
        // Dibujar red con un bucle como se ve en el turorial del tema
        for (int i = 0; i < ALTURA_PANEL; i += 30) {
            Line red = new Line (ANCHURA_PANEL / 2, i, ANCHURA_PANEL / 2, i + 10);
            red.setStroke(Color.WHITE);
            red.setStrokeWidth(4);
            panelDeJuego.getChildren().add(red);
        }
        
        
        //      DIBUJAR EL MARCADOR
        // Dibujar texto de puntuaciones, podemos usar un paneles HBox, unos Text para el texto del marcador
        HBox contenedorMarcadores = new HBox();
        contenedorMarcadores.setMinWidth(ANCHURA_PANEL);
        contenedorMarcadores.setAlignment(Pos.CENTER);
        
        
        HBox contenedorJugador1 = new HBox();
        HBox contenedorJugador2 = new HBox();
        
        
        
        // Texto del marcador de jugador 1
        Text textoJug1 = new Text("Jugador 1: ");
        textoJug1.setFont(Font.font(TEXT_SIZE) );
        textoJug1.setFill(Color.CYAN);

        puntosIzq = new Text (String.valueOf(puntosJugadorIzquierdo) );
        puntosIzq.setFont(Font.font(TEXT_SIZE) );
        puntosIzq.setFill(Color.CYAN);
        
        // Añadir el texto al layout
        contenedorJugador1.getChildren().addAll(textoJug1, puntosIzq);     
        contenedorJugador1.setPrefWidth(ANCHURA_PANEL/2);
        /* NOTA: En el video el marcador sale el marcador alineado cerca de la red, opino que queda más estético 
           centrado con el campo del jugador correspondiente, para centrarlo como en el video hay que cambiar:
           contenedorJugador1.setAlignment(Pos.CENTER_RIGHT);
           contenedorJugador2.setAlignment(Pos.CENTER_LEFT);
        */
        contenedorJugador1.setAlignment(Pos.CENTER);
      
        
         
        // Texto de puntuación del jugador 2
        Text textoJug2 = new Text("Jugador 2: ");
        textoJug2.setFont(Font.font(TEXT_SIZE) );
        textoJug2.setFill(Color.CORAL);
        
        puntosDer = new Text(String.valueOf(puntosJugadorDerecho) );
        puntosDer.setFont(Font.font(TEXT_SIZE) );
        puntosDer.setFill(Color.CORAL);
        
        //Añadir el texto al layout
        contenedorJugador2.getChildren().addAll(textoJug2, puntosDer);
        contenedorJugador2.setPrefWidth(ANCHURA_PANEL/2);
        contenedorJugador2.setAlignment(Pos.CENTER);
        
        //Añadir los contendores de los jugadores al contenedor principal
        contenedorMarcadores.getChildren().addAll(contenedorJugador1, contenedorJugador2);
        
        //Añadir el contenedor principal al panel
        panelDeJuego.getChildren().add(contenedorMarcadores);
    }
           
    
    
    /**
     * Método de arranque de la aplicación
     * @param escenario Escenario de la aplicación
     */
    @Override
    public void start(Stage escenario) {
        
                
        // Panel donde se desarrollará el juego
        Pane panelDeJuego = new Pane();
        
          
        // Escena
        Scene scene = new Scene (panelDeJuego, ANCHURA_PANEL, ALTURA_PANEL, Color.BLACK) ;
        
        
        // Dibujar red y marcador
        dibujarRedYmarcador(panelDeJuego);
        
           
        // MOVIMIENTO DE LAS PALAS        
        // Definir las teclas para el movimiento de las palas
        definirMovimientoTeclas(scene);

        
        // CREAR Y DIBUJAR LA PELOTA        
        // Crear la pelota
        Circle pelota = new Circle(posXpelota, posYpelota, TAMANO_PELOTA, Color.YELLOW);

        // Añadir la pelota al panel
        panelDeJuego.getChildren().add(pelota);
        
        
        // DIBUJAR LAS PALAS        
        // Dibujar la pala derecha y añadirla al panel
        Rectangle palaDerecha = new Rectangle(POSICION_X_PALA_DERECHA, posicionYpalaDerecha, ANCHURA_PALA, ALTURA_PALA);
        palaDerecha.setFill(Color.CORAL);
        
        panelDeJuego.getChildren().add(palaDerecha);
        
        
        // Dibular la pala izquierda y añadirla al panel
        Rectangle palaIzquierda = new Rectangle(POSICION_X_PALA_IZQUIERDA, posicionYpalaIzquierda, ANCHURA_PALA, ALTURA_PALA);
        palaIzquierda.setFill(Color.CYAN);
        
        panelDeJuego.getChildren().add(palaIzquierda);
        
        
        // Iniciar puntos de cada jugador
        puntosJugadorDerecho = 0;
        puntosJugadorIzquierdo = 0;
        
        
        
        
        
        /* Se usa una línea de tiempo para definir una animación.
           La idea básica en una animaciones JavaFX es manipular el valor de una o más propiedades de un nodo a intervalos 
           regulares. Por ejemplo, en el caso que nos ocupa, tenemos un círculo que representa una bola
           y se desea moverlo desde un lado de la pantalla hacia el otro.
        */
        Timeline animacion = new Timeline();
        
        /* Crear un KeyFrame, un punto de referencia que se agregará a la línea de tiempo.
           Una línea de tiempo funciona empleando KeyFrames como puntos de referencia en la animación.
           Observa que en la creación del objeto se pasa por parámetro un objeto KeyFrame al que se le indica cada 
           cuánto tiempo se ejecutarán las acciones, se indica 0.017 para que se ejecute cada  0.017 segundos que equivale a 
           unos 60 frames por segundo.
        */
        KeyFrame kf = new KeyFrame(Duration.seconds(0.017), (ActionEvent ae) -> {
            
            // Comprobar si es fin de juego según los puntos que lleve
            esFinDeJuego(panelDeJuego, animacion);
            
            
            // Define la posición horizontal del centro de la pelota en pixels.
            pelota.setCenterX(posXpelota);
            
            // Incrementamos el valor de atributo de la posición horizontal
            posXpelota += velocidadActualX;
            
            /* EN DESUSO
               Comprobar si la pelota debe rebotar horizontalmente, es decir, cambiar la dirección si se ha llegado al final 
               o al principio de la pantalla a la derecha o a la izquierda
            
            if (posXpelota >= ANCHURA_PANEL) {
                velocidadActualX = -3;                
            }
            if (posXpelota <= 0){
                velocidadActualX = 3; 
            }
            */
            
            // Define la posición vertical del centro de la pelota en pixels.
            pelota.setCenterY(posYpelota);
            
            // Incrementamos el valor de atributo de la posición vertical
            posYpelota += velocidadActualY;
            
            
            //REBOTAR CON TECHO Y SUELO
            // Comprobar si la pelota debe rebotar, es decir cambiar la dirección si la pelota ha llegado abajo
            if (posYpelota >= ALTURA_PANEL) {
                velocidadActualY *= (-1);
                // Reproducir sonido de rebote
                reproducirSonido(SONIDO_REBOTE);
            }
            
            // Comprobar si la pelota debe rebotar, es decir cambiar la dirección si la pelota ha llegado arriba
            if ( posYpelota <= 0) {
                velocidadActualY *= (-1);
                // Reproducir sonido de rebote
                reproducirSonido(SONIDO_REBOTE);
            } 
                               
                    
            // Actualizar la posición de las palas cuidando que no se salgan de la pantalla
            actualizarPosicionPalas(palaIzquierda, palaDerecha);
            
            
            // REBOTE CON PALA
            
            Shape shapeColisionPalaDerecha = Shape.intersect(pelota, palaDerecha);
            boolean colisionPalaDerecha = shapeColisionPalaDerecha.getBoundsInLocal().isEmpty();

            if (colisionPalaDerecha == false) {       //Colision detectada, Rebotar
                velocidadActualX = -3;
                calcularVelocidadPelotaPalaDer(getZonaColisionEnLaPala (pelota, palaDerecha) );
                reproducirSonido(SONIDO_REBOTE);
            }
            
            
            Shape shapeColisionPalaIzquierda = Shape.intersect(pelota, palaIzquierda);
            boolean colisionPalaIzquierda = shapeColisionPalaIzquierda.getBoundsInLocal().isEmpty();
            
            if (colisionPalaIzquierda == false ) {
                velocidadActualX = 3;
                calcularVelocidadPelotaPalaIzq(getZonaColisionEnLaPala(pelota, palaDerecha));
                reproducirSonido(SONIDO_REBOTE);
            }
            
            
            // Comprobar colisión con pala derecha        
            int zonaColision = getZonaColisionEnLaPala(pelota, palaDerecha) ;
            calcularVelocidadPelotaPalaDer(zonaColision) ;

            // Comprobar colisión con pala izquierda            
            zonaColision = getZonaColisionEnLaPala(pelota, palaIzquierda) ;
            calcularVelocidadPelotaPalaIzq(zonaColision) ;
            
           });
        
        // Añadir el keyframe a la animación
        animacion.getKeyFrames().addAll(kf);
        // Establecer el ciclo de la nimación indefinido
        animacion.setCycleCount(Timeline.INDEFINITE);
        // Poner en marcha la animación        
        animacion.play();

        // Título de la ventana
        escenario.setTitle("- P E N G -");       //No quiero un pleito con Atari :)
        // Icono de la aplicación
        escenario.getIcons().add(new Image(ICONO));
        // Establecer la escena        
        escenario.setScene(scene);
        // Mostrar el escenario
        escenario.show();
    }
    
    
    /** 
     * Método para averiguar la zona de contacto de la pelota con la pala.
     * La variable offsetBallStick almacena la distancia que se detecta entre 
     * la posición del rectángulo (la pala) y centro de la bola. Si ese valor 
     * es menor que el 10% de la altura de la pala se considerará que ha
     * contactado en la zona 1. Si no es ese caso, se comprueba si ha contactado
     * de la mitad de la pala hacia arriba, en cuyo caso sería la zona 2.
     * Se considera que ha contactado en la zona 3 si la posición de la bola es
     * mayor al centro de la pala y menor que el 90% de su altura. La zona de
     * contacto se considera la 4 si el punto de contacto es mayor que el 90% 
     * del tamaño de la pala. Ver en: https://javiergarciaescobedo.es/programacion-en-java/93-proyectos-de-aula/478-programacion-java-desarrollando-el-juego-pong-con-javafx-parte-ix
     * 
     * @param pelota    Pelota
     * @param pala      Pala
     * @return          Valor 0 si no hay contacto o bien 1, 2 o 3 según la zona.
     */
    private int getZonaColisionEnLaPala(Circle pelota, Rectangle pala) {
        
        int valor = 0 ;
        
        //Comprobacion de si la bola a entrado en contacto con la pala
        if (Shape.intersect(pelota, pala).getBoundsInLocal().isEmpty() ) {
            valor = 0;      //Si no hace contacto, el método devuelve 0
        }
        //Si hace contacto existen estas posibilidades
        else{
            double offsetBallStick = pelota.getCenterY() - pala.getY();
            
            //Si ese valor es menor que el 10% de la longitudad de la pala (la zona 1 de la pala)
            if (offsetBallStick < pala.getHeight() * 0.1){
                valor = 1;
            }
            //Contacto en la zona 2 de la pala si la pelota hace contacto por debajo de la mitad de la pala
            else if (offsetBallStick < pala.getHeight() / 2) {
                valor = 2;
            }
            //Ha hecho contacto en la zona 3 si la posicion de la pelota es mayor que el centro de la pala y menor que el 90% de ella
            else if (offsetBallStick >= pala.getHeight() / 2 && offsetBallStick < pala.getHeight() * 0.9) {
                valor = 3;
            }
            //Contacto en la zona 4 de la pala si no es ninguna de las condiciones anteriores
            else{
                valor = 4;
            }
        }      
       
        return valor ;
    }

    

    /**
     * Según la zona de la pala derecha donde impacte la pelota se modifican los 
     * atributos de velocidadActualX y velocidadActualY de la pelota para
     * que en la animación se vayan incrementando o decrementando la posición,
     * las coordenadas de la pelota segúnm corresponda.
     * 
     * @param zonaDeColision Entero de 0 a 4 que representa la zona de la pala
     *                donde impacta la pelota.
     */
    private void calcularVelocidadPelotaPalaDer(int zonaDeColision) {
        
        switch (zonaDeColision) {    
            // No hay colisión de la pala con la pelota.
            case 0:    
              break;
                
            // Hay colisión en la esquina superior de la pala
            case 1: 
                velocidadActualY = -6;
                
               break;
                
            // Hay colisión en el lado superior de la pala
            case 2: 
                velocidadActualY = -3;
                break;
                
            // Hay colisión en el lado inferior de la pala
            case 3: 
                velocidadActualY =  3;
                break;
                
            // Hay colisión en la esquina inferior de la pala
            case 4: 
                velocidadActualY =  6;
                break;
        }
    }
    
    
    /**
     * Según la zona de la pala izquierda donde impacte la pelota se modifican los 
     * atributos de velocidadActualX y velocidadActualY de la pelota para
     * que en la animación se vayan incrementando o decrementando la posición,
     * las coordenadas de la pelota segúnm corresponda.
     * 
     * @param zonaDeColision Entero de 0 a 4 que representa la zona de la pala
     *                donde impacta la pelota.
     */
    private void calcularVelocidadPelotaPalaIzq(int zonaDeColision) {
        
        // Según la zona de colisión
        switch (zonaDeColision) {
            //No hay colisión
            case 0:
               break;
                
            // Hay colisión en la esquina superior
            case 1: 
                velocidadActualY = -6;
                break;
                
            // Hay colisión en el lado superior
            case 2: 
                velocidadActualY = -3;
                break;
                
            // Hay colisión en el lado inferior    
            case 3: 
                velocidadActualY =  3;
                break;
              
            // Hay colisión en la esquina inferior
            case 4: 
                velocidadActualY =  6;
                break;
        }
    }
    
    
    /**
     * Presenta dos botones: uno para jugar de nuevo una partida y otro para
     * terminar la aplicación y salir al sistema operativo.
     * 
     * @param animacion     Animación que rige el juego.
     * @param panelPadre    Panel donde se desarrolla el juego 
     */
    private void seguirOterminar(Timeline animacion, Pane panelPadre) {
        
        // Parar la animación
        animacion.stop();
        
        // Crear un panel ancla
        AnchorPane anclaSeguiroTerminar = new AnchorPane();      
        panelPadre.setBackground(Background.fill(Color.BLACK) );        
        
        // Crear el botón de nueva partida
        Button botonJugar = new Button("Nueva Partida");
        botonJugar.setStyle("-fx-font-size: 2em; -fx-background-radius: 10");
        
        // Añadir el manejador al botón de nueva partida
        botonJugar.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                // Poner marcadores a 0
                puntosJugadorIzquierdo = 0;
                puntosJugadorDerecho = 0;
                
                puntosDer.setText(String.valueOf(puntosJugadorDerecho) );
                puntosIzq.setText(String.valueOf(puntosJugadorIzquierdo) );
                
                //Borrar el panel donde tenemos los botones de jugar o salir
                anclaSeguiroTerminar.setVisible(false);
                
                // Sacar la pelota de nuevo
                posXpelota = posicionYpalaIzquierda + 10;
                velocidadActualX = 3;
                
                // Arrancar animación
                animacion.play();
            }
        });  
         
        AnchorPane.setTopAnchor   (botonJugar, 120.0);
        botonJugar.setMinWidth(220);
        
        // Crear el botón de salir de la aplicación
        Button botonTerminar = new Button("Salir del juego");
        botonTerminar.setStyle("-fx-font-size: 2em; -fx-background-radius: 10");
        botonTerminar.setLayoutX(botonJugar.getLayoutX() + botonJugar.getPrefWidth() + 5);
        botonTerminar.setPrefWidth( 200);
        
        // Añadir EventHandler al botón
        botonTerminar.setOnAction( (ActionEvent e) -> {          
            // Salir del programa
            System.exit(0);
            
        });
        
        AnchorPane.setTopAnchor  (botonTerminar, 120.0);
        botonTerminar.setMinWidth(220);
        botonTerminar.setLayoutX( (ANCHURA_PANEL/4) + 40);
        
        // Añadir los botones en la parte superior del panel
        anclaSeguiroTerminar.getChildren().addAll(botonJugar, botonTerminar);
        
        // Añadir el panel ancla al panel padre
        panelPadre.getChildren().add(anclaSeguiroTerminar);
        anclaSeguiroTerminar.setLayoutX( (panelPadre.getWidth() / 4) - 30);
    }

    
    /**
     * Comprobar si la pelota ha rebasado la pala y por tanto es punto, además
     * de si se ha llegado al número total de vidas, invocar al método que
     * se encargue de dibujar los botones de jugar otra partida o salir del juego
     * 
     * @param root Panel donde se representa el juego
     * @param animacion Animación que rige el juego 
     */
    private void esFinDeJuego(Pane root, Timeline animacion) {
        
        /* Me resulta más estético marcar un punto cuando la pelota llega a tocar la pared del fondo en lugar de inmediatamente despues 
           de llegar a la posición de la pala. Para dejarlo como en el video hay que cambiar los condicionales de puntuación, es decir:
        
           Si la pelota está más allá de la pala derecha, el jugador izquierdo hace punto
              if (posXpelota >= POSICION_X_PALA_DERECHA)
        
           Si la pelota está más allá de la pala izquierda, el jugador derecho hace punto
              if (posXpelota <= POSICION_X_PALA_IZQUIERDA)
        */
        // Si la pelota está más allá de la pala derecha, el jugador izquierdo hace punto
        if (posXpelota >= ANCHURA_PANEL) {
            // Incrementar puntos del jugador izquierdo
            puntosJugadorIzquierdo++;
            
            // Establecer en el texto del marcador del jugador izquierdo
            puntosIzq.setText(Integer.toString(puntosJugadorIzquierdo) );
            
            // Reproducir sonido al anotar punto
            reproducirSonido(SONIDO_PUNTO);

            // Comprobar si se acabó la partida, si los puntos del jugador
            // izquierdo es igual a la constante NUM_VIDAS
            if (puntosJugadorIzquierdo == NUM_VIDAS) {
                // Reproducir sonido al acabar la partida
                 reproducirSonido(SONIDO_GAMEOVER);
                // Preguntar si jugar otra vez o terminar
                seguirOterminar(animacion, root);               
            } 
            else {
                // Sacar de nuevo la pelota
                iniciarSiguientePunto();                 
            }
        }

        // Si la pelota está más allá de la pala izquierda, el jugador derecho hace punto
        if (posXpelota <= 0) {
            // Incrementar puntos del jugador derecho
            puntosJugadorDerecho++;
            // Establecer en el texto del marcador del jugador derecho
            puntosDer.setText(Integer.toString(puntosJugadorDerecho) );
            // Reproducir sonido al anotar punto
            reproducirSonido(SONIDO_PUNTO);

            // Comprobar si se acabó la partida, o sea si los puntos del jugador
            // derecho son igual a la constante NUM_VIDAS
            if (puntosJugadorDerecho == NUM_VIDAS) {
                // Reproducir sonido al acabar la partida
                 reproducirSonido(SONIDO_GAMEOVER);
                // Preguntar si jugar otra vez o terminar
                seguirOterminar(animacion, root);
            } 
            else
                // Sacar de nuevo la pelota
                iniciarSiguientePunto();
        }
    }

    
    /**
     * Actualizar la posición de las palas cuidando que no se salgan de la pantalla.
     * 
     * @param palaIzquierda Objeto de la clase Rectangle que es la pala izquierda.
     * @param palaDerecha   Objeto de la clase Rectangle que es la pala derecha.
     */
    private void actualizarPosicionPalas(Rectangle palaIzquierda, Rectangle palaDerecha) {
        
        // Para la pala derecha, actualizar posición de la pala, es decir,
        // acumular en la posición Y de la pala derecha lo que se tenga más velocidadPalaDerecha
        posicionYpalaDerecha += velocidadPalaDerecha;
        
        //Controlar que la palaDerecha no salga de la pantalla. Así
        // Si la posición Y de la pala derecha es menor que 0 entonces la ponemos a 0
        if (posicionYpalaDerecha < 0) {
            //No sobrepasar el borde superior de la ventana
            posicionYpalaDerecha = 0;
        // Si no es así    
        } 
        else {
            // Si la posición Y es mayor que la altura menos la altura de la pala
            if (posicionYpalaDerecha > ALTURA_PANEL - ALTURA_PALA){
                // hacemos que la posición Y sea la altura menos la altura de la pala
                posicionYpalaDerecha = ALTURA_PANEL - ALTURA_PALA;
            } 
        }
        
        // Establecer con setY la posición Y de la pala derecha
        palaDerecha.setY(posicionYpalaDerecha);
        
        
        
        // AQUI hacer lo mismo pero para la pala izquierda
        posicionYpalaIzquierda += velocidadPalaIzquierda;
        
        //Controlar que la palaDerecha no salga de la pantalla. Así
        // Si la posición Y de la pala izquierda es menor que 0 entonces la ponemos a 0
        if (posicionYpalaIzquierda < 0) {
            //No sobrepasar el borde superior de la ventana
            posicionYpalaIzquierda= 0;
        // Si no es así    
        } 
        else {
            // Si la posición Y es mayor que la altura menos la altura de la pala
            if (posicionYpalaIzquierda > ALTURA_PANEL - ALTURA_PALA){
                // hacemos que la posición Y sea la altura menos la altura de la pala
                posicionYpalaIzquierda = ALTURA_PANEL - ALTURA_PALA;
            } 
        }
        
        // Establecer con setY la posición Y de la pala izquierda
        palaIzquierda.setY(posicionYpalaIzquierda);
    }
    
    
    /**
     * Reproducir el sonido cuya ruta se indique en el parámetro.
     * 
     * @param nombreSonido Ruta con el nombre del sonido a reproducir
     */
    public void reproducirSonido(String nombreSonido){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(nombreSonido).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        }
        catch(UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
           System.err.println("Error al reproducir el sonido.");
        }
     }
        

    /**
     * Definir las teclas para mover las palas arriba y abajo o dejar de 
     * moverlas al dejar de presionar las teclas.
     * 
     * @param scene La escena donde se desarrolla el juego
     */
    private void definirMovimientoTeclas(Scene scene) {
        
        // Tratar los eventos de teclado en la escena
        scene.setOnKeyPressed( (KeyEvent event) -> {
            
            switch (event.getCode() ) {
                case UP:
                    // Pulsada tecla arriba sube la pala derecha
                    velocidadPalaDerecha = -6;
                    break;

                case DOWN:
                    // Pulsada tecla abajo baja la pala derecha
                    velocidadPalaDerecha = 6;
                    break;

                case W:
                    // Pulsada tecla W sube la pala izquierda
                    velocidadPalaIzquierda = -6;
                    break;

                case S:
                    // Pulsada tecla S baja la pala izquierda 
                    velocidadPalaIzquierda = 6;
                    break;
            }
        });
        
        
        /**
         * Definir qué pasa cuando se dejen de pulsar las teclas.
         * Cuando se libere la pulsación de las teclas de la pala, se dejarán
         * de desplazar por la pantalla, por tanto, dado que el movimiento es
         * en la vertical, velocidadPalaDerecha o bien velocidadPalaIzquierda
         * será establecido a 0, según la pala que sea, o sea, según la tecla
         * de la pala que se haya dejadp de pulsar
         */
        scene.setOnKeyReleased( (KeyEvent event) -> {
            
            // Detenemos el movimiento de la pala al dejar de presionar               
            switch (event.getCode() ) {
                case UP:
                    // Soltada tecla arriba pala derecha
                    velocidadPalaDerecha = 0;
                    break;

                case DOWN:
                    // Soltada tecla abajo pala derecha
                    velocidadPalaDerecha = 0;
                    break;

                case W:
                    // Soltada tecla arriba pala izquierda
                    velocidadPalaIzquierda = 0;
                    break;

                case S:
                    // Soltada tecla arriba pala izquierda 
                    velocidadPalaIzquierda = 0;
                    break;                        
            }     
        });
    }
  
    /**
     * Programa principal, lanza la aplicación.
     * @param args Argumentos o parámetros (no hay en este caso)
     */
    public static void main(String[] args) {
        launch(args);
    }
}