package trencaclosques;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Aquesta classe representa una finestra emergent que mostra un missatge d’ajuda
 * per explicar com jugar al joc del trencaclosques.
 */
public class MissatgeAjuda extends JFrame implements ActionListener {

    private JButton boto;           // Botó per tancar la finestra
    private JPanel pBoto;           // Panell que conté el botó
    private JLabel titol, text1;    // Etiquetes per mostrar el títol i el text explicatiu

    // Constructor: inicialitza la finestra
    public MissatgeAjuda() {
        super("Ajuda");             // Estableix el títol de la finestra
        initComponents();           // Crea i configura els components grafics
        repaint();                  // Redibuixa la finestra (opcional aquí)
    }

    // Metode per inicialitzar i configurar els components de la finestra
    private void initComponents() {
        // Títol de la finestra
        titol = new JLabel();
        titol.setText("Ajuda");                                 // Text del títol
        titol.setHorizontalAlignment(SwingConstants.CENTER);    // Centrat horitzontalment
        titol.setFont(new java.awt.Font("Courier", 1, 50));     // Font tipus Courier en negreta
        titol.setBounds(0, 0, 400, 100);                         // Posició i mida del títol
        this.getContentPane().add(titol);                       // Afegeix el títol a la finestra

        // Text explicatiu amb instruccions
        // Text explicatiu amb instruccions
        // Es necesari html ja que en JLabel no conta \n com un salt de linea, per tant l'equivalent es <br>
        text1 = new JLabel();
        text1.setText(
        "<html>Hola! Benvingut al joc del Trencaclosques.<br><br>"
        + "Per completar el puzzle, ho pots fer de 2 maneres:<br><br>"
        + "- Moure amb el teclat (A U S D) i amb les fletxes de la dreta<br>"
        + "- Moure amb el ratolí, clicant a una imatge i arrossegant-la fins a la posició buida que vols"
        + "</html>"
        );      
     
        
        text1.setHorizontalAlignment(SwingConstants.CENTER);    // Centrat horitzontalment
        text1.setFont(new java.awt.Font("Courier", 0, 12));     // Font tipus Courier normal
        text1.setBounds(200, 400, 400, 500);                     // Posició (no es fa servir amb BorderLayout)
        this.getContentPane().add(text1);                       // Afegeix el text a la finestra

        // Configuració general de la finestra
        this.setSize(getPreferredSize());                       // Estableix la mida preferida
        this.pack();                                            // Ajusta la mida segons els components
        this.setResizable(false);                               // Evita redimensionar la finestra
        this.setLocationRelativeTo(null);                       // Centra la finestra a la pantalla
        this.setAlwaysOnTop(true);                              // Mostra sempre per sobre de la resta

        // Botó per tancar la finestra
        boto = new JButton("OK");
        boto.addActionListener(this);                           // Assigna acció al botó

        // Panell que conté el botó
        pBoto = new JPanel();
        pBoto.setBounds(0, 350, 400, 400);                      // Mida (no necessaria amb BorderLayout)
        pBoto.add(boto);                                        // Afegeix el botó al panell
        this.getContentPane().add(pBoto, BorderLayout.PAGE_END); // Col·loca el panell a la part inferior
    }

    // Retorna la mida preferida de la finestra
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    // Acció a realitzar quan es fa clic al botó
    @Override
    public void actionPerformed(ActionEvent e) {
        this.setVisible(false);  // Oculta la finestra (no la destrueix)
    }
}