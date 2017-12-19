# CodingWeek 2017 - DreamTeam
Matta KARCZEWSKI - Quentin LEFEUVRE - Maxence LEFORT - Jeremy SERNIT

##  RELEASE_DAY_1
En ce premier jour de travail, nous avons implémenté plusieurs fonctionnalités de notre projet (identification, liste des projets gitlab, interface de réglagles, interface de création des groupes, interface de statistiques des groupes)
Voici comment lancer cette première version.

    1ère étape
Entrer son "Personal Access Tokens" dans le champ "Clé de connexion" (ce token se trouve sur gitlab)
Entrez "admin" en identifiant ET mot de passe.

La fenêtre qui s'ouvre alors répertorie tous les projets gitlab associés à la clé de connexion sous la forme d'une liste.

    2ème étape
En cliquant sur un projet, on voit apparaître un bouton "modifier" qui permet d'accéder aux réglagles de ce projet (cette fonctionnalité n'est pas encore implémentée, seule l'interface est faite).

Les autres interfaces ne sont pas encore accessibles depuis ce programme.


##  RELEASE_DAY_2
Deuxième jour de code.

Les 2 premières étapes sont indentiques. Cependant, des messages d'erreur ont été ajoutés en cas d'echec de connexion.

    3ème étape
On peut maintenant créer un nouveau fichier en appuyant sur "Nouveau Projet" ou actualiser la session.
De plus, sur chaque projet est disponible un certains nombres d'informations dont les options "modifier" et "supprimer".

    4ème étape
Le bouton "Gestionnaire de groupes" permet d'accéder à des réglages permettant de créer des groupes. Ce gestionnaire prend en charge la lecture des fichiers .csv.


À noter que d'autres fonctionnalités n'ont pas encore été liées au reste du projet (notemment les statistiques de chacun des groupes)



