package trencaclosques;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Classe que representa una casella del trencaclosques.
 * Dibuixa un fons gris clar si és la casella buida (valor 0),
 * i una subimatge si és una casella normal.
 */
public class Casella {

    private Rectangle2D.Float rec;   // Zona grafica de la casella
    private Imatge img;              // Subimatge associada (pot ser null si és buida)
    private int fila, columna;       // Posició dins del tauler
    private boolean marcada;         // Si esta seleccionada amb el ratolí

    /**
     * Constructor.
     * @param r Rectangle que defineix la mida i posició.
     * @param valor Valor inicial (només per crear identificador).
     * @param fila Posició fila dins la matriu.
     * @param columna Posició columna dins la matriu.
     */
    public Casella(Rectangle2D.Float r, int valor, int fila, int columna) {
        this.rec = r;
        this.fila = fila;
        this.columna = columna;
        this.marcada = false;
        this.img = null; // es definira després
    }

    /**
     * Dibuixa la casella al tauler. Si valor == 0, mostra color gris clar.
     * Si té imatge, la dibuixa dins del rectangle.
     */
    public void paintComponent(Graphics g, int valor) {
        Graphics2D g2d = (Graphics2D) g;

        if (valor == 0) {
            // Casella buida  quadre gris clar
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fill(rec);
        } else if (img != null) {
            // Casella normal  dibuixa la seva imatge ajustada a la mida del rectangle
            img.paintComponent(g, rec.x, rec.y, (int) rec.width, (int) rec.height);
        }

        // Dibuixa contorn negre de la casella
        g2d.setColor(Color.BLACK);
        g2d.draw(rec);
    }

    // --- GETTERS I SETTERS ---

    public Rectangle2D.Float getRec() {
        return rec;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public void setImg(Imatge img) {
        this.img = img;
    }
    
    public Imatge getImg() {
        return this.img;
    }

    public boolean isMarcada() {
        return marcada;
    }

    public void setMarcada(boolean marcada) {
        this.marcada = marcada;
    }
}
