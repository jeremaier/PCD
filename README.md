# CodingWeek 2017 - DreamTeam
Matta KARCZEWSKI - Quentin LEFEUVRE - Maxence LEFORT - Jeremy SERNIT

##  RELEASE_DAY_1
En ce premier jour de travail, nous avons implémenté plusieurs fonctionnalités de notre projet (identification, liste des projets gitlab, interface de réglagles, interface de création des groupes, interface de statistiques des groupes)
Voici comment lancer cette première version.

    1ère étape
Entrer son "Personal Access Tokens" dans le champ "Clé de connexion" (ce token se trouve sur gitlab)
Entrez "admin" en identifiant ET mot de passe.

La fenêtre qui s'ouvre alors répertorie tous les groupes gitlab associés à la clé de connexion sous la forme d'une liste.

    2ème étape
En cliquant sur un projet, on voit apparaître un bouton "modifier" qui permet d'accéder aux réglagles de ce projet (cette fonctionnalité n'est pas encore implémentée, seule l'interface est faite).

Les autres interfaces ne sont pas encore accessibles depuis ce programme.

##  RELEASE_DAY_2
Deuxième jour de code.

Les premières étapes sont similaires à la première release. Cependant, de nouvelles fenêtres d'erreur s'ouvrent en cas d'échec de connexion.

    3ème étape
Il est possible de créer un nouveau projet en appuyant sur "Nouveau Projet" et d'actualiser la session en appuyant sur "Actualiser".
De plus, chaque projet affiché à l'écran peut être "déroulé" afin d'obtenir des informations complémentaires.

    4ème étape
Dans chaque projet, il est possible de démarrer le "Gestionnaire de groupes" permettant de créer les groupes de projet.

À noter que d'autres fonctionnalités n'ont pas encore liées au reste du projet (comme par exemple l'affichage des statistiques de chaque groupe).

##  RELEASE_DAY_3
Encore un peu plus de code...

En cette 3ème journée, plusieurs problèmes ont été résolus (comme par exemple le fait qu'un enseignant génère une instance de Group et non de Project)

    Pour lancer l'application, se référer à l'étape 1 de la RELEASE_DAY_1
    
Il est maintenant possible d'envoyer des mails à tous les membres d'un groupe en appuyant sur le bouton "Mail" dans le panneau d'informations concernant les groupes.
Tout groupe créé est maintenant mis à jour automatiquement sur gitlab.

Les fonctions permettant de créer des groupes de projets et d'afficher les statistiques sont presque terminées mais ne sont pas encore accessible depuis la fenêtre principale.

##  RELEASE_DAY_4

Le projet se lance toujours de la même manière sur gitlab
De nouvelle fonctionnalité ont été ajoutées.

L'interface est assez intuitive donc les fonctionnalités sont facilement testables par l'utilisateur.

Le token de connexion peut maintenant être enregistré pour ne pas avoir à l'entrer à chaque connexion.
De plus, dès le menu nous avons maintenant tout de suite une vision des projets de chaque groupe.

