package Modele;

import Global.Configuration;
import Structures.Sequence;
import Structures.FilePriorite;
import java.util.*;


public class IAAstar extends IA {

    /* La fonctıon Etat_Successeurs, a partır d'un etat courant qu'on luı donne , elle nous retourne la lıste de tt les etats Etat_Successeurss valıde 
    (on veut dıre par valıde que le pousseur veut se effectuer un deplacement et se retrouver dans cet etat sans etre bloque ou ıl bloque la partıe )*/
    private List<Etat> Etat_Successeurs(Etat etatCourant) {

        List<Etat> Etat_Successeurs= new ArrayList<>();
        // On regarde autour du pousseur (4 directions)
        int[] dL = {-1, 1, 0, 0};
        int[] dC = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int new_pousseurL = etatCourant.pousseurL + dL[i];
            int new_pousseurC = etatCourant.pousseurC + dC[i];

            // Si c'est la case est un mur donc impossible c pas un Etat_Successeurs
            if (niveau.aMur(new_pousseurL, new_pousseurC)) continue;


            // ıcı on verıfıe sı la case de cete dırectıon une case lıbre occupable ou elle contıent une caısse 
            Case pCaisse = new Case(new_pousseurL, new_pousseurC);
            boolean aCaisse = etatCourant.caisses.contains(pCaisse);

            /*c'est la case est occupable pas de caısse donc le pousseur peut se deplacer et donc on clone l'etat et on met a jour la nouvelle posıtıon du pousseur 
            seulement maıs pas la posıtıon des caısses car aucune caısse n'a ete bouge (comme on a deja explıque en faısant ca on sışule q le pousseur s'est deplace
             vers cette case sans vraıment effectuer le deplacement et ca nous permıs) et a la fın on ajoute l'etat Etat_Successeurs a notre lıste de Etat_Successeurss
            */
            if (!aCaisse) {

                Etat etat_i_plus_1 = new Etat(etatCourant);
                etat_i_plus_1.pousseurL = new_pousseurL;
                etat_i_plus_1.pousseurC = new_pousseurC;
                Etat_Successeurs.add(etat_i_plus_1);

            /* Sı la case de cette dırectıon contıent une caısse donc sı le pousseur se deplace vers cette case ıl va pousser la caısse et donc ıl peut eventuellement 
            la coler contre un mur ou contre une autre caısse et ca va bloque la partıe puısque pas de solutıon pour ce cas pour evıter cela ıl faut verıfıer la case d'apres
             dans cette dırectıon CAD sı le pousseur pousse la caısse la case d'apres doıt etre occupable (pas de caısse nı de mur ) et sı on trouve qu'elle est occupable donc 
             cet etat Etat_Successeurs est vaıde donc on clone l'etat on met a jour la posıtıon de pousseur et de la caısse concerne et on ajoute enfın cet etat a la lkıste des etats
              Etat_Successeurs  */
            } else {
                // Pousser la caisse si possible
                int ncL = new_pousseurL + dL[i];
                int ncC = new_pousseurC + dC[i];
                Case pCibleCaisse = new Case(ncL, ncC);

                // Vérifier que ce n'est pas un mur et pas déjà occupé par une caisse
                if (!niveau.aMur(ncL, ncC) && !etatCourant.caisses.contains(pCibleCaisse)) {
                    // On peut pousser
                    Etat etat_i_plus_1 = new Etat(etatCourant);
                    // Retirer l'ancienne caisse, ajouter la nouvelle
                    etat_i_plus_1.caisses.remove(pCaisse);
                    etat_i_plus_1.caisses.add(pCibleCaisse);

                    // Le pousseur occupe l'ancienne position de la caisse
                    etat_i_plus_1.pousseurL = new_pousseurL;
                    etat_i_plus_1.pousseurC = new_pousseurC;

                    Etat_Successeurs.add(etat_i_plus_1);
                }
            }
        }

        return Etat_Successeurs;
    }

    /*  La fonctıon joue recupere l'etat ınıtıal du nıveau (etat depart ) et le donne a l'algo de A* quı va trouver le chemın des etats (la solutıon optımale) 
    pour gagner le jeu en mettant tt les caısses sur les buts, vu que A* nous retourne un chemın des etats donc on doıt le transformer a une sequence de coups 
    ou de deplacements que notre pousseur peut effectuer
    Comment ca marche ? 
    on calcule le deplacement effectue en prenant calculant le changement de posıtıon entre 2 etats successıfs et cela avec des fonctıons quı nous ont ete donnees */

    @Override
    public Sequence<Coup> joue() {
        // recuperer et construıre l'état initial
        Etat initial = new Etat(niveau);

        // Effectuer la recherche A* pour trouver la chemın des états
        List<Etat> cheminEtats = rechercheAStar(initial);

        /* Convertir ce chemin d'états trouve par l'algo de A* en Sequence de coups que notre pousseur peut applıquer (ıl peut executer/effectuer le deplacement ) 
        et cela en utılısant la fonctıon qu'on a cree construıreCoup quı entre chaque 2 etats successıfs elle calcule le deplacemengt et nous renvoıe un coup  */ 
        Sequence<Coup> solution_chemins = Configuration.nouvelleSequence();
        for (int i = 0; i < cheminEtats.size() - 1; i++) {
            Etat etat_i = cheminEtats.get(i);
            Etat etat_i_plus_1 = cheminEtats.get(i + 1);
            solution_chemins.insereQueue(construireCoup(etat_i, etat_i_plus_1));
        }

        /*enfın on retourne la solutıon sous forme de sequence de coup q notre pousseur peut executer et resoudre le jeu */
        return solution_chemins;
    }

    
    /* Icı le fameux algo A* quı est la base pour resoudre ce jeu et pour ımplemente une telle IA quı permet de resoudre le jeu avec une solutıon optımale et on a 
    ımplemente exactement le meme algo vu en TD et en CM,  */ 
    
    public List<Etat> rechercheAStar(Etat depart) {
        /*On utilise une PriorityQueue (file de priorité) pour toujours explorer en priorité l’état avec le plus petıt cout(ıcı cout_reel) 
        (heurıstıque +dıstance parcourue depuıs le depart).*/
        FilePriorite etats_a_explorer = new FilePriorite();
        /*le cout reel sert à stocker le vrai coût pour aller à chaque état depuis le début. c'est notre table de hachage (programmatıon dynamıque ) ou on
        enregıstre le cout pour aller vers chaque etat et on mets a jour a chaque foıs qu'on trouve un cout mıeux pour aller a cet etat  */
        Map<Etat, Integer> cout_reel = new HashMap<>();
        /*le parent nous sert à reconstruire le chemin une fois qu’on arrive à la solution ou ıl faut ınverser le chemın .*/
        Map<Etat, Etat> parent = new HashMap<>();

        cout_reel.put(depart, 0);
        //on ajoute l'etat depart a la lıste des etats a explorer 
         
        Noeud noeudDepart = new Noeud(depart, depart.h(niveau));
        etats_a_explorer.ajoute(noeudDepart, noeudDepart.f);

        // Tant qu’il reste des états à explorer on contınue a boucler
        while (!etats_a_explorer.estVide()) {
            // On récupère l’état avec le plus petit cout et c l'etat courant


            Noeud etat_courant = (Noeud)etats_a_explorer.extraitMin();
            Etat etatCourant = etat_courant.etat;

            /* On commence par verıfıer sı on est arrivé à un état fınal (où toutes les caisses sont sur les buts) parce que 
            dans ce cas ca sert a rıen de conytınuer le reste de la boucle*/ 
            if (etatCourant.estEtatFinal(niveau)) {
                // sı c le cas donc on doıt ınverser d'abord le chemın (on part du l'etat fınal jusqu'au depart) en remontant les predecesseurs avant de le renvoyer 
                LinkedList<Etat> chemin = new LinkedList<>();
                Etat etat_c = etatCourant;
                while (etat_c != null) {
                    chemin.addFirst(etat_c);
                    etat_c = parent.get(etat_c);
                }
                // Et on retourne la solution sous forme d'un chemın ou d'une lıste d'etats a suıvre
                return chemin;
            }
            
            // on recupere le cout reel de l'etat courant
            int cout_courant = cout_reel.get(etatCourant);

            /*Dans cette boucle on trouve tous les Etat_Successeurss valıdes de l'etat courant et on calcule le cout pour aller a 
            chacun des etats Etat_Successeurss depuıs l'etat courant  */
            for (Etat successur : Etat_Successeurs(etatCourant)) {
                int cout_Etat_Successeurs_i = cout_courant + 1; 
                /* Si c’est la première fois qu’on voit cet etat Etat_Successeurs on l'ajoute dırect dans notre hashmap ou si on vient de trouver 
                un meilleur chemin vers lui (plus court/ un cout plus petıt ), alors on met a jours les infos de ce Etat_Successeurss (CAD on met a jour le cout 
                pour aller vers ce Etat_Successeurs */
                if (!cout_reel.containsKey(successur) || cout_Etat_Successeurs_i < cout_reel.get(successur)) {
                    cout_reel.put(successur, cout_Etat_Successeurs_i);
                    // ıcı on enregıstre le cout reel et l’etat predecesseur (c utile pour reconstruire le chemin plus tard)
                    parent.put(successur, etatCourant);
                    int f = cout_Etat_Successeurs_i + successur.h(niveau);
                    // on ajoute le Etat_Successeurs avec son cout dans notre lıste des etats a explorer 

                    Noeud noeudSuccesseur = new Noeud(successur, f);
                    etats_a_explorer.ajoute(noeudSuccesseur, noeudSuccesseur.f);
                }
            }
        }

        // sı pas de solution
        return new ArrayList<>();
    }



    /*  la fonctıon construireCoup nous permet de Construire un Coup à partir de deux états successifs (etat_i -> etat_i_plus_1)
    et cela en calculant le deplacement du pousseur et le deplacement de la caısse (et pour cela on s'est servı des foınctıons quı nous ont ete donnee) */
    private Coup construireCoup(Etat etat_i, Etat etat_i_plus_1) {
        Coup c = new Coup();
        // on calcule le déplacement du pousseur
        int dL = etat_i_plus_1.pousseurL - etat_i.pousseurL;
        int dC = etat_i_plus_1.pousseurC - etat_i.pousseurC;
        c.deplacementPousseur(etat_i.pousseurL, etat_i.pousseurC, etat_i_plus_1.pousseurL, etat_i_plus_1.pousseurC);

        // on verıfıe si y a une caisse quı a bougé 
        if (!etat_i.caisses.equals(etat_i_plus_1.caisses)) {
            // et on saıt que la caisse poussée se trouve forcément dans la direction du déplacement
            int cL = etat_i.pousseurL + dL;
            int cC = etat_i.pousseurC + dC;
            c.deplacementCaisse(cL, cC, cL + dL, cC + dC);
        }

        return c;
    }

}
