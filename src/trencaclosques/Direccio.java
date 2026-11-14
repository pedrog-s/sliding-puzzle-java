package trencaclosques;

import java.io.Serializable;

public class Direccio implements Serializable {
    enum Moviment{NORD, SUD, EST, OEST}
    
    public static Moviment getDireccio(int c){
        if (((char) c == 'w' || (char) c == 'W' || c == 38 || c == 87)){
            return Moviment.NORD;
        } else if ((char) c == 'a' || (char) c == 'A' || c == 37 || c == 65){
            return Moviment.OEST;
        }else if ((char) c == 's' || (char) c == 'S' || c == 40 || c == 83){
            return Moviment.SUD;
        }else if ((char) c == 'd' || (char) c == 'D' || c == 39 || c == 68){
            return Moviment.EST;
        }
        return Moviment.NORD;
    }
}
