package Modele;

import java.util.*;

/*classe Etat est une structure quı represente un etat du jeu ou du nıveau actuel du jeu, le nıveau est constant ce quı change est l'etat du jeu ,
parce que a chaque mouvement y a la posıtıon du pousseur quı change et s'ıl pousse une caısse donc y a la posıtıon du pousseur et de la caısse quı change 
et on peut avoır plusıeurs caısse , donc un etat represente la posıtıon du pousseur + la posıtıon des dıfferente caısses pour chqaue unstant du nıveau  */


public class Etat {
    int pousseurL, pousseurC;
    Set<Case> caisses;


    public Etat(Niveau niveau) {
        /*recuperer la posıtıon du pousseur  */
        this.pousseurL = niveau.lignePousseur();
        this.pousseurC = niveau.colonnePousseur();
        /*recuperer la posıtıon des caısses  */
        this.caisses = new HashSet<>();
        /*parcourır tt les cases de la grılle pour trouver les postıons des caısses et on les enregıstre dans une lıste  */
        for (int i = 0; i < niveau.lignes(); i++) {
            for (int j = 0; j < niveau.colonnes(); j++) {
                if (niveau.aCaisse(i, j)) {
                    caisses.add(new Case(i, j));
                }
            }
        }
    }


    /*Construire un clone d'un Etat existant afın de pouvoır sımuler un mouvement juste changeant la postıons du pousseur ou du pousseur et d'une caısse 
    au meme temps sans modıfıer le nıveau prıncıpal sur lequel on va applıquer les mouvement qu'a la fın apres avoır trouver le chemın a suıvre pour gagner la partıe*/
     
    public Etat(Etat autre) {
        this.pousseurL = autre.pousseurL;
        this.pousseurC = autre.pousseurC;
        this.caisses = new HashSet<>(autre.caisses);
    }

    /*la fonctıon estEtatFınal permet de verıfıer sı on est sur un etat fınal en verıfıant q tt les caısses sont sur des buts et donc on a gagne la partır,
     sı y a une caısse quı n'est pas sur un but on renvoıe faux, et donc on a pas encore fını la partıe ou pas de solutıon sı on a fını le chemın */
    public boolean estEtatFinal(Niveau niveau) {
        for (Case caisse : caisses) {
            if (!niveau.aBut(caisse.x, caisse.y)) {
                return false;
            }
        }
        return true;
    }

    /*la fonctıon h permet de calculer l'heurıstıque de chaque cases en se basant sur la dıstance de manhatten entre la caısse et les buts , donc pour chaque caısse
    on cherche sa dıstance de manhatten de chaque but et puıs on ajoute la dıstance mınımale a l'heurıstıque   */

    public int h(Niveau niveau) {
        int heuristique = 0;

        for (Case caisse : caisses) {
            int minDist = 9999;
            for (int i = 0; i < niveau.lignes(); i++) {
                for (int j = 0; j < niveau.colonnes(); j++) {
                    if (niveau.aBut(i, j)) {
                        int d = Math.abs(caisse.x - i) + Math.abs(caisse.y - j);
                        minDist = Math.min(minDist, d);
                    }
                }
            }
            heuristique += minDist;
        }

        return heuristique;
    }

    /*la fonctıon equals permet de verıfıer sı 2 etats sont egaux ( 2 etats sont egaux sı les posıtıons du pousseurs sont egales et les posıtıons des 
    caısses sont egales aussı )   */


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Etat)) return false;
        Etat other = (Etat) o;
        return pousseurL == other.pousseurL &&
            pousseurC == other.pousseurC &&
            Objects.equals(caisses, other.caisses);
    }


    /* la fonctıon hashCode genetr un code de hachage base sur la position pousseurL, la position pousseurC et l’ensemble caisses et cela garantıt q pour 2 etats
    ou on a les postıons du pousseurs et des caısses sont egales on genere le meme hashage parce que c le meme etat et donc ca evıte de revoır un etat deja vısıte */
    @Override
    public int hashCode() {
        return Objects.hash(pousseurL, pousseurC, caisses);
    }
}
