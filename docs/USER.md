### Auteurs : Raphaël BACKLAND & Gaël PAQUETTE
### Date de Création : 21/04/2024
### Dernière Modification : 10/06/2024


<h1 style="text-align:center;">Manuel Utilisateur</h1>


Cette page est dédiée au manuel utilisateur du projet ```BACKLAND_PAQUETTE_BabaIsYou```.
Dans cette page, vous trouverez la documentation pour compiler et utiliser le projet.


<h2 style="text-align:center">Sommaire</h2> 
### - Compilation du projet
### - Utilisation du jeu
### - Règles du jeu de Baba Is You


<h3 style="text-align:center">Compilation du projet</h3>

Pour compiler le projet, vous aviez à votre disposition le fichier ```build.xml```. 
Le fichier permet de nettoyer la ```classes``` qui contient les sources du project compiler, de nettoyer le fichier 
```baba.jar``` et de nettoyer le dossier ```docs/doc```.

Pour compiler le projet avec le fichier ```build.xml```, vous devez utiliser la commande ```ant```.
Ensuite, vous devez utiliser les commandes suivantes pour compiler, nettoyer et build le projet :

- ```ant compile``` pour compiler le projet
- ```ant copy-resources``` pour compiler les images du projet
- ```ant jar``` pour creer le fichier ```JAR``` ```baba.jar```
- ```ant run``` pour lancer le projet
- ```ant clean``` pour clean le build du projet


<h3 style="text-align:center">Utilisation du projet</h3>

Le projet utilise une interface graphique pour interagir avec l'utilisateur.
Le projet utilise la librairie Zen6 pour gérer l'affichage graphique et pour gérer les events clavier, souris de l'utilisateur.
Pour ce projet, seuls les events clavier sont pris en compte.

Pour deplacer le joueur sur la carte du jeu. Vous utiliserez les touches directionnelles du clavier.
- ```UP``` pour déplacer le joueur d'une case vers le haut
- ```DOWN``` pour déplacer le joueur d'une case vers le bas
- ```LEFT``` pour déplacer le joueur d'une case vers la gauche
- ```RIGHT``` pour déplacer le joueur d'une case vers la droite

Pour arrêter le jeu en cours, vous utiliserez la touche ```Q``` du clavier.


<h3 style="text-align:center">Règles du jeu de Baba Is You</h3>

Pour chaque niveau, il existe des règles immuables
- ```YOU``` représentant le joueur, si aucun nom n'est associé à ```YOU```, ou qu'il n'y a plus d'élément en jeu représentant ce nom, alors la partie est perdue (si ```Rock Is You``` mais qu'il n'y a plus de rocher en jeu... c'est fini!)
- ```You``` représentant le joueur, si aucun nom n'est associé à ```You```, ou qu'il n'y a plus d'élément en jeu représentant ce nom, alors la partie est perdue (si ```Rock Is You``` mais qu'il n'y a plus de rocher en jeu... c'est fini!)
- ```Win``` représentant l'élément qui déclenche la victoire, si aucun nom n'est associé à ```Win```, ou qu'il n'y a plus d'élément en jeu représentant ce nom, alors il n'est pas possible de finir le niveau (il vous faudra associer cette propriété à un autre nom)
- Chaque niveau est d'une taille fixée et reste borné, ce qui empêche quelconque élément de quitter la zone de jeu
- Un élément qui contient du texte, les blocs, sont implicitement poussable (```Push```). Lorsqu'ils sont en ligne ou en colonne, si on pousse le premier block, cela pousse les blocs suivant
- Un élément dont le nom n'a plus de propriété, reste figé en jeu et il peut être traversé
- Chaque niveau peut avoir des éléments décoratifs qui n'ont aucun nom les représentants (vous pouvez avoir des fleurs sur le monde sans que celles-ci aient un bloc ```Flower``` de présent), dans ce cas-là ils suivent les règles ci-dessus
