package Modele;

import Global.Configuration;
import Structures.Sequence;
import java.util.*;

/*La classe Noeud est une structure quı represente un noeud ou un sommet du graphe ou pour chqaue sommet on enregıstre l'etat actuel 
du jeu a cet ınstant (position du pousseur + caisses)et aussı le cout actuel tel que le cout represente l'heurıstıque + la dıstance parcourue jusqu'a ce sommet la*/ 

public class Noeud { //Etat + f 
    Etat etat;
    int f;

    Noeud(Etat e, int fScore) {
        this.etat = e;
        this.f = f;
    }
}