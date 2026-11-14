package trencaclosques;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Trencaclosques extends JFrame implements MouseListener, KeyListener {

    private Puzzle puzzle, puzzleOrd;   // puzle activo y el puzle resuelto para comparar
    private long startTime, elapsedTime;    // marca de tiempo para el cronometro
    private boolean sHaDesordenat = false;  // indica si ya hemos desordenado para empezar a contar
    private final int dimension;

    // Barra de estado
    private JLabel lblMoves;
    private JLabel lblTime;
    private javax.swing.Timer timer;    // actualiza el reloj cada segundo 

    // Menú
    private JMenuBar barraMenu;
    private JMenu menuJoc, menuAjuda;
    private JMenuItem itemInici, itemEscapçar, itemSortir, itemStats, itemAjuda;

    public Trencaclosques() {
        super("Super Puzzle!");

        // Obtengo dimensión y fichero de imagen del configurador
        ConfiguradorPuzzle config = new ConfiguradorPuzzle();
        dimension = config.getDIMENSIO();
        File imatge = config.getImatgeSeleccionada();

        try {
            puzzle    = new Puzzle(dimension, dimension, imatge);
            puzzleOrd = new Puzzle(dimension, dimension, imatge);
        } catch (IOException e) {
            System.err.println("Error carregant imatge: " + e.getMessage());
            System.exit(1);
        }
        puzzle.setPotGuanyar(false);

        initComponents();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Trencaclosques().setVisible(true);
        });
    }

    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());

        // ——— Menú “Joc” y “Ajuda” ——————————————————————————————————
        barraMenu = new JMenuBar();
        menuJoc   = new JMenu("Joc");
        menuAjuda = new JMenu("Ajuda");

        itemInici    = new JMenuItem("Inici");
        itemEscapçar = new JMenuItem("Escapçar");
        itemSortir   = new JMenuItem("Sortir");
        itemStats    = new JMenuItem("Stats");
        itemAjuda    = new JMenuItem("Ajuda");

        // Acción “Inici”
        itemInici.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
        itemInici.addActionListener(e -> {
            puzzle.ordena();
            puzzle.setPotGuanyar(false);
            sHaDesordenat = false;
            puzzle.resetMoveCount();
            lblMoves.setText("Moviments: 0");
            lblTime .setText("Temps: 00:00");
            repaint();
        });

        // Acción “Escapçar”
        itemEscapçar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
        itemEscapçar.addActionListener(e -> {
            puzzle.desordena();
            puzzle.resetMoveCount();
            lblMoves.setText("Moviments: 0");
            startTime     = System.currentTimeMillis();
            sHaDesordenat = true;
            puzzle.setPotGuanyar(true);
            repaint();
        });

        // Acción “Sortir”
        itemSortir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
        itemSortir.addActionListener(e -> System.exit(0));

        // — Acción “Stats”: muestra un JOptionPane con las estadísticas inline —
        itemStats.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        itemStats.addActionListener(e -> {
            Properties p = Stats.getStats();
            String pref      = "dim." + dimension + ".";
            long   count     = Long.parseLong(p.getProperty(pref + "count",    "0"));
            double avgTime   = Double.parseDouble(p.getProperty(pref + "avgTime",  "0"));
            double avgMoves  = Double.parseDouble(p.getProperty(pref + "avgMoves", "0"));
            long   bestTime  = Long.parseLong(p.getProperty(pref + "bestTime",  "0"));
            int    bestMoves = Integer.parseInt(p.getProperty(pref + "bestMoves","0"));

            // formateo tiempos mm:ss
            String bestTimeStr = String.format("%02d:%02d",
                (bestTime/1000)/60, (bestTime/1000)%60);
            String avgTimeStr = String.format("%02d:%02d",
                ((long)avgTime/1000)/60, ((long)avgTime/1000)%60);

            String msg = "Les teves estadistiques de " + dimension + "×" + dimension + "!\n"
            + "Has jugat " + count + " partides.\n"
            + "El teu millor temps: " + bestTimeStr + " (moviments: " + bestMoves + ")\n"
            + "Temps mitja : " + avgTimeStr + "\n"
            + String.format("Moviments mitjans: %.2f", avgMoves);


            JOptionPane.showMessageDialog(
                this,
                msg,
                "Stats " + dimension + "×" + dimension,
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        // Acción “Ajuda”
        itemAjuda.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, ActionEvent.ALT_MASK));
        itemAjuda.addActionListener(e -> new MissatgeAjuda().setVisible(true));

        menuJoc.add(itemInici);
        menuJoc.add(itemEscapçar);
        menuJoc.add(itemSortir);
        menuJoc.addSeparator();
        menuJoc.add(itemStats);
        menuAjuda.add(itemAjuda);

        barraMenu.add(menuJoc);
        barraMenu.add(menuAjuda);
        setJMenuBar(barraMenu);

        // ——— Puzzle en el centro ——————————————————————————————————————
        getContentPane().add(puzzle, BorderLayout.CENTER);

        // ——— Barra de estado (movimientos & tiempo) —————————————————————
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEmptyBorder(2,5,2,5));
        lblMoves = new JLabel("Moviments: 0");
        lblTime  = new JLabel("Temps: 00:00");
        statusBar.add(lblMoves, BorderLayout.WEST);
        statusBar.add(lblTime,  BorderLayout.EAST);
        getContentPane().add(statusBar, BorderLayout.SOUTH);

        // Timer que actualiza el reloj cada segundo
        timer = new javax.swing.Timer(1000, ev -> {
            if (sHaDesordenat) {
                long el = System.currentTimeMillis() - startTime;
                int sec = (int)(el/1000)%60;
                int min = (int)(el/1000)/60;
                lblTime.setText(String.format("Temps: %02d:%02d", min, sec));
            }
        });
        timer.start();

        // Listeners de teclado y ratón
        addKeyListener(this);
        puzzle.addMouseListener(this);

        pack();
        setLocationRelativeTo(null);
    }

    // ——— MouseListener —————————————————————————————————————————
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton()==MouseEvent.BUTTON1 && puzzle.clickADins(e.getX(), e.getY())) {
            puzzle.casellesNoMarcades();
            puzzle.marcaCasella(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton()==MouseEvent.BUTTON1 && puzzle.esCasellaBuida(e.getX(), e.getY())) {
            puzzle.moureCasella();
            lblMoves.setText("Movimients: " + puzzle.getMoveCount());
        }
        repaint();
        comprovaFi();
    }

    private void comprovaFi() {
        if (puzzle.sonIguals(puzzleOrd) && puzzle.potGuanyar() && sHaDesordenat) {
            elapsedTime = System.currentTimeMillis() - startTime;
            int moves = puzzle.getMoveCount();
            Stats.record(dimension, elapsedTime, moves);
            Ruidos.playVictory();
            try {
                new MissatgeFiJoc(elapsedTime).setVisible(true);
            } catch (IOException ex) {
                System.err.println("ERROR: " + ex.getMessage());
            }
            puzzle.setPotGuanyar(false);
            sHaDesordenat = false;
        }
    }

    @Override public void mouseClicked(MouseEvent e)  {}
    @Override public void mouseEntered(MouseEvent e)  {}
    @Override public void mouseExited(MouseEvent e)   {}

    // ——— KeyListener ————————————————————————————————————————————
    @Override public void keyTyped(KeyEvent e)   {}
    @Override public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        puzzle.moureCasella(Direccio.getDireccio(e.getKeyCode()));
        lblMoves.setText("Moviments: " + puzzle.getMoveCount());
        repaint();
        comprovaFi();
    }
}