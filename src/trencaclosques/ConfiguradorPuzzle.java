    package trencaclosques;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Aquesta classe permet que l'usuari triar el nombre de files, columnes
 * i una imatge per jugar al puzzle. Si no tria cap imatge, es posa una per defecte.
 */
public class ConfiguradorPuzzle {
    private int DIMENSIO;
    private File imatgeSeleccionada;

    /**
     * Constructor que mostra el formulari per triar parametres.
     */
    public ConfiguradorPuzzle() {
        // Camps de text per introduir el nombre de files i columnes
        JTextField inputDIMENSIO = new JTextField("3");

        // Panell amb disposició vertical
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Número de dimensio:"));
        panel.add(inputDIMENSIO);


        // Mostrar dialeg de confirmació
        int result = JOptionPane.showConfirmDialog(null, panel, "Configuració del Puzzle",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                DIMENSIO = Integer.parseInt(inputDIMENSIO.getText());

                // Validació que no siguin menors que 2
                if (DIMENSIO < 2 ) throw new NumberFormatException("La dimensio minima es 2");
                
                if(DIMENSIO > 10) throw new NumberFormatException("La dimensio maxima es 10 ");
                // Dialeg per seleccionar imatge
                JFileChooser selector = new JFileChooser("imatges");
                int seleccionat = selector.showOpenDialog(null);
                if (seleccionat == JFileChooser.APPROVE_OPTION) {
                    imatgeSeleccionada = selector.getSelectedFile();
                } else {
                    imatgeSeleccionada = new File("imatges/puzzle.png");
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }

    public int getDIMENSIO() {
        return DIMENSIO;
    }

    public File getImatgeSeleccionada() {
        return imatgeSeleccionada;
    }
}
