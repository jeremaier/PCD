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

## RELEASE_DAY_5

LIEN VERS LE DIAGRAMME DE CLASSE :
https://www.planttext.com/plantuml/svg/fLbBSzGu4BxxL-ZeL3aSziYcKeJ99ZLLISYIa0l5GR5xPWGQQqgI0rcA_xvwsNew6E2GiQMlM_rGFzoy4H9pEUnfLRKK2u6kCUchTI-1Rt0BQDqJIJ0b_-572YFcbhFFq4gnOhtaZ5BWw7k5z3_IIzGF-qVWV_i5YOd2k8Lx-2Ol2D3kuwUFdv1KRo3SrXdd-FcA27bo9pdfjwUentiGPrq7NGQvv6mudAAj_W7nbzk_aqC7lTI81m9V4LKFuybEQdImltRraJ4Ak4V24huTf6IztSgrh1CMHmRtn4Y7y23P7alIhZYe7zRNHvNPuD0n_xndJw0jK6kxV7RB7L2GS4PfZ3CQ6k212RU3kyukf-_chxD6Ws8YiMDV0yECj6HqBTGVtbyXobeZU8FUWs03R-7jq7SKb3JsLR7xaTu0RMJb-l2rlfDu2yf4P8_vitbfKji7d2ZRMYPNzW4Lw6EuzyVtobuKrBA-LsoLz99zWTxgTy-VLtQzFZDx_pmf2o3GVt_V18QktK7xHGbS1vAfipPaEt33mQgKBTMNH5xXnxC3GLlpr0H-EweYPbA96QgghrgsFFgzGG5dYeEB-HigJhGxhBTArfi4lTohuBwxlxxzNbbHv8u8equrXXPmpdWr0Uj8PNj1eUzKtg7tYg9sBYLTWzpZlZjCpnq8INgiKWg7QL4Cdq5ERotZ1_673Xub2XrivBP-qF55kZ7bc5DrgafpO9gpha6lhliDutiZXpX5UxCcBDI7VA2-vjm4N8z-YbrqecCpfX6Bh7C2Xm_XDZyvYYdNXDROWWosoP6tZDgvCMhK6SMD4hwEeUlp76OP2-kQKl15KPDKZ0ZeoeVGsu5H8uo5H4iEceHomC3IZ7BhRDQ6z6R9xeunmdh5pcl-H0Hv99J8PxysUzzyPBIS7Bo08-zMXV4M7FM7NYNLTmTGaZ1-OegnyRIgzwzM1dYgJuCjy7SzVRuWL8xtECjcOHfAyngAejnVdIGW1qxpM7mi4-I-gSl0Mzo3li7HjW1vnjiTSVMtJAal0gYIOMwN1zWykAAa_QAQ4MMJOauiubUuRu4kXZiH5-Fly0lWyG8GSKus6-1gvuAplV9LfDrmwB2qrWtZlbuK2uc9SnfzuhfpUSg4xCfdKzCmH2jHIrd5GKPraBdRRkkyUnc4kgDLb7Zzipqe3k_2jQMuSTnKv_6a37AlJuWIQ-w36rNI5xZgFMnKWTajWVuxa6AxCXj12qeQijGth0Fj8zT4k3hielD7wWNlGTVQKwOhBN1rzeCA4BNnONsE5kIdKa8RAplh1XgMpSVhi1nUATtekV85sX0kv3b-JjULo7vvBX4kAeU-1enXWsqincgf1dRFvch5VENs-ZUnxXuxgjuKr6xGZE9DGUc5zL_lDvcoJQHe4jr174Vhfd3F18hQVfYKhsDR9BXhOv4wiizuUQUZHZFLqNrnd2ROXJDSdTjoX4xOAvn2dN4pvBatSftRFOEFaYvsHyR1bOEkJjsK8M-rk-g2xtmhrTMZCUUQhWaRUEme9t5-IecyQwOTRz9ju9FozkY6QJSuCJIytt_PhhvJT1-z8zbeosrWtzAXrgH-vnSxPLHgbXSG-QgzOlk3dWmKP2VYah7EcYm2UF5rctABvQvei58lJ1csmmIXQxRfSX9L17uQ-mJR1Bt8JtK-HDMPowd5zjnAhGN8jyzfWWbbDbSClbfsQzqlrzaWA3ac5beiaZha4nT5rRPB-rrlsRbNb3U_vfF8JZhMvoaRnoBiFAffe_vv3v8BbKu9YbycaKpLv0GHqVzKbScglpbM-67-EWLSU0RHOELtwkeiV3Ds4MxD9F5fCO-cEg5jCi9f1VH0uYXaxf9Ga173Q5BqlT-92arO_wQ3Ojp4E14mHbshLU1dhRANI0ww7438QMUw1C5SBxq6wT3DjrasvPYvwj8EIVanOXhEVjqCDfl0pWzEcnVO97C9nw_EvXz_CCFCJEV6c4NPzRUxVeI1Lmd9hwcb3jR3TNHyF0uzA_UBsF7nQJpQHAyHV8DsKBNHV05K_dl9ektx2mJXJDQI58HBYR9lV4ivCrx9oAjO1wLfA4LL4411iZDdSC4NOvIF0tl4sV1roO85dXEZgeh-kmETl7hrqjIZZ8u7j0NCD0ajxPd1qSnwdAiBgCZwrOmPywmx0qmkUthvjTbd8gOgBmVN8H3p1MV7KpShE3efwt1dlam3Oop154x9FAYaOO7FqYc5lS29qwTwRebZaZn89dXLwGxb6lTuEsk6197u_0tqtR2d_mC0


LIEN VERS LA VIDEO TUTORIEL:
https://www.youtube.com/watch?v=em2EHwkGc3w&feature=youtu.be
