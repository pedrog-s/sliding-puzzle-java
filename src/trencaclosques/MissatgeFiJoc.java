package trencaclosques;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
/**
 * Classe que mostra un missatge final quan l'usuari resol el puzzle.
 * Mostra una imatge i el temps que ha trigat el jugador.
 */
public class MissatgeFiJoc extends JFrame {
    private JButton boto;         // Botó per tancar la finestra
    private JPanel pboto;         // Panell inferior amb el botó
    private JLabel etiqueta;      // Etiqueta central amb imatge i text
    private String missatge;      // Missatge de felicitació amb el temps
    private final ImageIcon img;  // Imatge decorativa final
    /**
     * Constructor que rep el temps transcorregut i mostra la finestra final.
     * @param elapsedTimeMillis temps en milisegons des de l'inici fins al final del joc
     */
    public MissatgeFiJoc(long elapsedTimeMillis) throws IOException {
        super("Enhorabona");
        this.img = new ImageIcon("imatges/final.png");
        // Conversió de milisegons a minuts i segons
        int segons = (int) (elapsedTimeMillis / 1000);
        int minuts = segons / 60;
        segons = segons % 60;
        // Format del missatge amb HTML per centrar-lo
        this.missatge = "Has guanyat!\nTemps: " 
        + String.format("%02d:%02d", minuts, segons);

        InitComponents();
        repaint();
    }
    /**
     * Inicialitza els components grafics de la finestra emergent.
     */
    private void InitComponents() {
        this.etiqueta = new JLabel();
        this.etiqueta.setText(this.missatge);
        this.etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        this.etiqueta.setFont(new java.awt.Font("Courier", 1, 20));
        this.etiqueta.setIcon(img);
        this.etiqueta.setIconTextGap(20);
        this.getContentPane().add(etiqueta, BorderLayout.CENTER);
        this.setSize(getPreferredSize());
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        boto = new JButton("OK");
        boto.addActionListener(e -> System.exit(0));
        pboto = new JPanel();
        pboto.add(boto);
        this.getContentPane().add(pboto, BorderLayout.PAGE_END);
    }
    /**
     * Defineix la mida preferida de la finestra emergent.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(450, 300);
    }
}
