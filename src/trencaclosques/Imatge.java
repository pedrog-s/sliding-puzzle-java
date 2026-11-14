package trencaclosques;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
/**
 * Aquesta classe encapsula una subimatge assignada a una casella del puzzle.
 * Ara rep directament un BufferedImage, ja tallat del puzzle global.
 */
public class Imatge {
    private BufferedImage img;
    /**
     * Constructor que rep la imatge ja tallada (subimatge).
     * @param img La subimatge assignada a una casella.
     */
    public Imatge(BufferedImage img) {
        this.img = img;
    }
    /**
     * Metode per dibuixar la imatge dins una casella.
     * @param g Context grafic.
     * @param x Coordenada X dins del panell.
     * @param y Coordenada Y dins del panell.
     * @param valor El valor numeric de la casella (0 = buida).
     */
    public void paintComponent(Graphics g, float x, float y, int amplada, int alcada) {
    if (img != null) {
        g.drawImage(img, (int) x, (int) y, amplada, alcada, null);  // escala la imatge al tamany real
    }
}
}
