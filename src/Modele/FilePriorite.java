package Structures;

import java.util.*;

public class FilePriorite {
    public final List<ElementAvecPriorite> elements;

    public FilePriorite() {
        elements = new LinkedList<>();
    }

    // Ajoute un élément avec une priorité
    public void ajoute(Object element, int priorite) {
        ElementAvecPriorite nouveau = new ElementAvecPriorite(element, priorite);
        int i = 0;
        while (i < elements.size() && elements.get(i).priorite <= priorite) {
            i++;
        }
        elements.add(i, nouveau); // insertion triée
    }

    // Extrait l’élément avec la plus petite priorité
    public Object extraitMin() {
        if (estVide()) return null;
        return elements.remove(0).element;
    }

    
    public boolean estVide() {
        return elements.isEmpty();
    }

    // Classe interne pour stocker un élément et sa priorité
    public static class ElementAvecPriorite {
        Object element;
        int priorite;

        ElementAvecPriorite(Object element, int priorite) {
            this.element = element;
            this.priorite = priorite;
        }
    }
}
