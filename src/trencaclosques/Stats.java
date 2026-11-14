package trencaclosques;

import java.io.*;
import java.util.Properties;

/**
 * Gestiona estadísticas persistentes en stats.properties
 * por cada dimensión del puzzle.
 */
public class Stats {
    private static final File FICHERO = new File("stats.properties");
    private static final Properties props = new Properties();

    static {
        if (FICHERO.exists()) {
            try (FileInputStream in = new FileInputStream(FICHERO)) {
                props.load(in);
            } catch (IOException e) {
                System.err.println("No he pogut carregar les estadistiques: " + e.getMessage());
            }
        }
    }

    /**
     * Registra una nueva partida: tiempo (ms) y movimientos.
     */
    public static void record(int dimension, long timeMs, int moves) {
        String pref = "dim." + dimension + ".";
        String keyCount    = pref + "count";
        String keyAvgTime  = pref + "avgTime";
        String keyAvgMoves = pref + "avgMoves";
        String keyBestTime = pref + "bestTime";
        String keyBestMoves= pref + "bestMoves";
        
        //pone el contador a 0
        long count    = Long.parseLong(props.getProperty(keyCount,    "0"));
        // pone la media de tiempo a 0
        double avgT   = Double.parseDouble(props.getProperty(keyAvgTime,  "0"));
        // pone la media de movimiento a 0
        double avgM   = Double.parseDouble(props.getProperty(keyAvgMoves, "0"));

        // nuevas medias incrementales
        double newAvgT = (avgT * count + timeMs)    / (count + 1);
        double newAvgM = (avgM * count + moves)     / (count + 1);
        count++;

        props.setProperty(keyCount,    Long.toString(count));
        props.setProperty(keyAvgTime,  Double.toString(newAvgT));
        props.setProperty(keyAvgMoves, Double.toString(newAvgM));

        // mejor tiempo y movimientos
        long bestT   = Long.parseLong(props.getProperty(keyBestTime, Long.toString(timeMs)));
        int  bestM   = Integer.parseInt(props.getProperty(keyBestMoves, Integer.toString(moves)));
        if (timeMs < bestT) bestT = timeMs;
        if (moves < bestM)   bestM = moves;
        props.setProperty(keyBestTime, Long.toString(bestT));
        props.setProperty(keyBestMoves,Integer.toString(bestM));

        // guarda en fichero
        try (FileOutputStream out = new FileOutputStream(FICHERO)) {
            props.store(out, "Estadistiques del Trencaclosques");
        } catch (IOException e) {
            System.err.println("No he pogut guardar les estadistiques: " + e.getMessage());
        }
    }

    /** Devuelve todas las propiedades cargadas. */
    public static Properties getStats() {
        return props;
    }
}