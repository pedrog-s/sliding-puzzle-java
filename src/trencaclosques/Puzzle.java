package trencaclosques;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
/**
 * Classe que representa el trencaclosques. Cada casella pot tenir una imatge,
 * excepte una (valor 0) que representa l'espai buit. 
 * Permet desordenar, ordenar i moure les peces.
 */
public class Puzzle extends JPanel {
    private int files;                      // Nombre de files
    private int columnes;                   // Nombre de columnes
    private int costat;                     // Costat en píxels de cada casella
    private Casella[][] t;                  // Matriu de caselles (interfície)
    private int[][] m;                      // Matriu de valors numerics (model)
    private int moveCount = 0;  // nuevo contador
    private boolean potGuanyar;             // Indica si es pot guanyar o no
    private int filaBuida, colBuida;        // Posició de la casella buida
    private BufferedImage imatgeOriginal;   // Imatge completa original
    /**
     * Constructor del puzzle amb mida i imatge configurables.
     */
    public Puzzle(int files, int columnes, File imatgeArxiu) throws IOException {
        this.files = files;
        this.columnes = columnes;
        this.potGuanyar = false;
        // Carrega la imatge completa
        this.imatgeOriginal = ImageIO.read(imatgeArxiu);
        // Calcula la mida de les subimatges i de cada casella
        int subample = imatgeOriginal.getWidth() / columnes;
        int subaltura = imatgeOriginal.getHeight() / files;
        this.costat = Math.min(subample, subaltura);
        this.setPreferredSize(new java.awt.Dimension(columnes * costat, files * costat));
        // Inicialitza les matrius de valors i de caselles
        this.m = new int[files][columnes];
        this.t = new Casella[files][columnes];
        // Assigna els valors ordenats (1 a n-1) i 0 al final
        int valor = 1;
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (i == files - 1 && j == columnes - 1) {
                    m[i][j] = 0; // buida
                } else {
                    m[i][j] = valor++;
                }
            }
        }
        filaBuida = files - 1;
        colBuida = columnes - 1;
        // Crea les caselles i assigna subimatges
        for (int i = 0; i < files; i++) {
            int y = i * subaltura;
            for (int j = 0; j < columnes; j++) {
                int x = j * subample;
                Rectangle2D.Float r = new Rectangle2D.Float(j * costat, i * costat, costat, costat);
                t[i][j] = new Casella(r, m[i][j], i, j);
                if (m[i][j] != 0) {
                    BufferedImage sub = imatgeOriginal.getSubimage(x, y, subample, subaltura);
                    t[i][j].setImg(new Imatge(sub));
                }
            }
        }
    }
    /**
     * Dibuixa totes les caselles del tauler.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                t[i][j].paintComponent(g, m[i][j]);
            }
        }
    }
    /**
     * Desordena aleatoriament les caselles del puzzle.
     */
    public void desordena() {
        Random rng = new Random();
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                int x = rng.nextInt(files);
                int y = rng.nextInt(columnes);
                int tmp = m[i][j];
                m[i][j] = m[x][y];
                m[x][y] = tmp;
                Imatge tmpImg = t[i][j].getImg();
                t[i][j].setImg(t[x][y].getImg());
                t[x][y].setImg(tmpImg);
            }
        }
        trobarCasellaBuida();
        potGuanyar = true;
        repaint();
    }
    /**
     * Ordena el puzzle deixant-lo com l’inicial.
     */
    public void ordena() {
        int val = 1;
        int subample = imatgeOriginal.getWidth() / columnes;
        int subaltura = imatgeOriginal.getHeight() / files;
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (i == files - 1 && j == columnes - 1) {
                    m[i][j] = 0;
                    t[i][j].setImg(null);
                } else {
                    m[i][j] = val++;
                    BufferedImage sub = imatgeOriginal.getSubimage(j * subample, i * subaltura, subample, subaltura);
                    t[i][j].setImg(new Imatge(sub));
                }
            }
        }
        filaBuida = files - 1;
        colBuida = columnes - 1;
        repaint();
    }
    /**
     * Compara l'estat actual amb un altre puzzle.
     */
    public boolean sonIguals(Puzzle altre) {
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (this.m[i][j] != altre.m[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * Mou la peça segons la direcció especificada.
     */
    public void moureCasella(Direccio.Moviment mov) {
        trobarCasellaBuida();
        int origenFila = filaBuida, origenCol = colBuida;
        switch (mov) {
            case NORD: origenFila += 1; break;
            case SUD:  origenFila -= 1; break;
            case EST:  origenCol -= 1; break;
            case OEST: origenCol += 1; break;
        }
        if (origenFila >= 0 && origenFila < files && origenCol >= 0 && origenCol < columnes) {
            intercanvia(origenFila, origenCol);
        }
    }
    /**
     * Mou la casella marcada si és contigua a la buida.
     */
    public void moureCasella() {
        trobarCasellaBuida();
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (t[i][j].isMarcada() && contigua(i, j)) {
                    intercanvia(i, j);
                    return;
                }
            }
        }
    }
    /**
     * Intercanvia la casella buida amb la de posició (i, j).
     */
    private void intercanvia(int i, int j) {
        int tmp = m[filaBuida][colBuida];
        m[filaBuida][colBuida] = m[i][j];
        m[i][j] = tmp;
        Imatge tmpImg = t[filaBuida][colBuida].getImg();
        t[filaBuida][colBuida].setImg(t[i][j].getImg());
        t[i][j].setImg(tmpImg);
        filaBuida = i;
        colBuida = j;
        potGuanyar = true;
        moveCount++;            // incrementamos el contador
        repaint();
        Ruidos.playMove();   // <— sonido de movimiento
    }
    /**
     * Troba la posició de la casella buida (valor 0).
     */
    private void trobarCasellaBuida() {
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (m[i][j] == 0) {
                    filaBuida = i;
                    colBuida = j;
                    return;
                }
            }
        }
    }
    /**
     * Comprova si s'ha clicat dins d'alguna casella.
     */
    public boolean clickADins(int x, int y) {
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (t   [i][j].getRec().contains(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Marca una casella clicada si és contigua a la buida.
     */
    public void marcaCasella(int x, int y) {
        trobarCasellaBuida();
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (t[i][j].getRec().contains(x, y) && contigua(i, j)) {
                    t[i][j].setMarcada(true);
                }
            }
        }
    }
    /**
     * Comprova si una casella esta al costat de la buida.
     */
    private boolean contigua(int i, int j) {
        return (Math.abs(i - filaBuida) == 1 && j == colBuida)
            || (Math.abs(j - colBuida) == 1 && i == filaBuida);
    }
    /**
     * Comprova si s'ha fet clic sobre la casella buida.
     */
    public boolean esCasellaBuida(int x, int y) {
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                if (t[i][j].getRec().contains(x, y) && m[i][j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Desmarca totes les caselles del tauler.
     */
    public void casellesNoMarcades() {
        for (int i = 0; i < files; i++) {
            for (int j = 0; j < columnes; j++) {
                t[i][j].setMarcada(false);
            }
        }
    }
    public boolean potGuanyar() {
        return potGuanyar;
    }
    public void setPotGuanyar(boolean pot) {
        this.potGuanyar = pot;
    }
    /** Devuelve el número de movimientos realizados */
    public int getMoveCount() {
        return moveCount;
    }
    /** Reinicia el contador de movimientos (p. ej. al reiniciar el puzzle) */
    public void resetMoveCount() {
        moveCount = 0;
    }
    
    /**
     * Retorna la mida del panell per ajustar la finestra.
     */
    public java.awt.Dimension getMidaTotal() {
        return new java.awt.Dimension(columnes * costat, files * costat);
    }
}
