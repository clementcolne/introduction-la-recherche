# Linear Programming Solver Interface

Ce projet est une interface faisant le lien entre un programme linéaire et un solveur.
Il a été créé dans le cadre d'un projet visant à réestimer des coûts de transformations de requête. 
En entrée est fournie une fonction de coût qui attribue une valeur à chaque transformation de requête, ainsi qu'un ensemble d'inégalités.
Elles correspondent à des contraintes d'utilisateur(s) sur l'ordre de priorité des types de modifications d'une requête.
Le but est de trouver une fonction de coût satsifaisant toutes ces contraintes, tout en restant proche de celle fournie en entrée.

### Prérequis

Logiciel(s) nécessaires au fonctionnement du projet :

```
Lp Solve
```

Version du sdk utilisé :

```
Java 1.8
```

### Solveurs disponibles

Liste des différents solveurs compatibles avec l'interface :

* lp_solve version 5.5.2.11

## Exécution

Ligne de commande pour lancer l'archive jar :

```
java -jar jarName.jar solver file_path [solve_ options]
```

*[solver] doit être remplacer par un des solveurs de la liste "Solveurs disponibles".*

## Format de fichier
Le fichier doit être un fichier texte au format pour être utilisé avec lp_solve :
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
La structure du fichier texte est adaptée à l’utilisation de lp_solve, mais il est possible que cette structure ne soit pas adaptée à l'utilisation d'un autre solveur.

## Explication des programmes

La classe AbstractSolver permet de regrouper les méthodes communes aux différents solveurs,
comme la méthode run, qui sert à lancer l'exécutable du solver, ou display qui permet
de récupérer et d'afficher la sortie standard du solver.
La classe Lpsolve contient les méthodes plus spécifiques au solveur lp_solve. Cette classe
contient les méthodes permettant de créer un fichier lp avec les informations de base. Elle
contient aussi les méthodes permettant d'analyser les différents fichiers, de calculer la plus
courte distance ou d'afficher le résultat.

## Exemple d'exécution

Exemple d'éxécution sur le fichier test.txt avec le serveur lp_solve :
```
min
// fonction
5 2
// c1 sup
x1 1
// c2 inf
x1 3
// c3 sup
x2 1
// c4 inf
x2 3
```

On exécute la commande ``java -jar jarName.jar lp_solve test.txt``, 
ce qui donne en sortie : 
```
This problem is infeasible

Trying to optimize the solution :

min: z1 + z2;
c1: z1 >= y1 - 5;
c2: z1 >= 5 - y1;
c3: z2 >= y2 - 2;
c4: z2 >= 2 - y2;
c5: y1 >= 1;
c6: y1 <= 3;
c7: y2 >= 1;
c8: y2 <= 3;

The solution is in MRU :

Value of objective function: 2.00000000

Actual values of the variables:
z1                              2
z2                              0
y1                              3
y2                              2
```

On détecte que la fonction de coût donnée dans le fichier test.txt ne satisfait pas les contraintes utilisateurs.
Il est alors calculé une nouvelle fonction de coût, qui correspond à l'ensemble des variables y<sub>i</sub>.

## Documentation

* [Lp Solve](http://lpsolve.sourceforge.net/5.5/lp_solve.htm) - options pour Lp Solve
