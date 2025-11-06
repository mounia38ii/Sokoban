package Modele;

import java.util.Objects;

/*classe Case est une structure quı represente une case du graphe quı est ıdentıfıe par ses cordonnees 
la lıgne x et la colonne y quı sont pas modıfıable (c pour ca on a mıs fınal ıntx et fınal ınt y ) */

public class Case {
    public final int x;
    public final int y;

    
    public Case(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Case)) return false;
        Case p = (Case) o;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
