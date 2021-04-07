# Linear Programming Solver Interface

Ce projet est une interface faisant le lien entre un programme linéaire et un résolveur.

## Getting Started


### Prérequis

Logiciel(s) nécessaires au fonctionnement du projet :

```
Lp Solve
```

### Solveurs disponibles

Liste des différents solveurs disponibles :

```
lp_solve
```

## Exécution

Ligne de commande pour lancer l'archive jar :

```
java -jar jarName.jar [solver] <file path> [solver options]
```

*remplacer [solver] par un des solveurs de la liste "Solveurs disponibles".*

*Le fichier doit être un fichier texte au format :*
```
min (Resp. max, le type d'optimisation à effectuer)

// fonction
Votre fonction de cout

// ci eq (Le numéro de la contrainte et l'égalité permettant de fixer les valeurs d'une variable
x1 5

A répéter pour toutes les valeur de la fonction de cout

// cj sup (Resp. inf, le numéro de la contrainte ainsi que le type d'inégalité,
           avec j commençant à i+1)
x1 1 (Les variables de la contrainte ainsi que la valeur de la borne)

A répéter pour toutes les contraintes du problème
```
### Documentation

* [Lp Solve](http://lpsolve.sourceforge.net/5.5/lp_solve.htm) - options pour Lp Solve