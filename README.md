# Projet SMA

### Auteurs :

Mathieu **Hannoun**

Henri **Bertrand**

### Introduction

Ce projet à été réalisé dans le cadre du Master 2  Intelligence Embarquée de l'université Cergy-Pontoise.

La documentation du code est sous forme de Javadoc (voir `./javadoc`), ce document illustre le fonctionnement des différents agents/behaviours de façon schématique.

## Arguments de compilation

Voici les différents arguments à passer à la compilation pour instancier 3 agents s'achetant et se vendant des produits.

```shell
-gui -agents "p1:agents.ProducerConsumer(oil,weat);p2:agents.ProducerConsumer(weat,wood);p3:agents.ProducerConsumer(wood,oil)"
```

### Notes

Les `MessageTemplate` sont utilisés pour empêcher de défiler les messages qui ne sont pas utilisés par le `behaviour` appelant `receive()`, cela permet de dissocier le comportement de vente de celui d'achat.

## Agents

### ProducerConsumer

Agent principal de cette simulation, possède les différents comportements liés à la production, la consommation, l'achat et la vente de stock.

Il prend en paramètre :

1. Le type de produit qu'il produit et vends
2. Le type de produit qu'il achète et consomme.

Il contient aussi les différentes méthodes liées à son fonctionnement comme le calcul du prix de vente en fonction de son bonheur actuel.



## Behaviours

### BuyingBehaviour

Comportement permettant de réaliser un seul et unique achat de marchandise à un vendeur.

Le vendeur choisi est celui proposant la meilleure offre en terme de prix unitaire.

Ce comportement est considéré comme terminé une fois la transaction réalisée.

C'est une machine à états comme suit :

![finite state machine diagram](https://i.ibb.co/HB9VkFc/buying-Behaviour.png)



### BuyingDecisionBehaviour

Comportement cyclique permettant d'ajouter un comportant `BuyingBehaviour` si l'agent n'a plus de stock et n'est pas déjà en train d'essayer d'acheter.

### ConsumingBehaviour

Consomme des ressources et met à jour le bonheur de l'agent.

SI l'agent peut consommer alors sont bonheur est mis à 1.

Sinon il perd 10% de bonheur.

## Selling behaviour

Routine cyclique qui gère les réponses à envoyées aux acheteurs.

Soit :

- Fais une proposition de vente basé sur le bonheur actuel de l'agent.
- Envoie une confirmation de vente à l'acheteur.

## Protocole de vente

Un protocole léger à été mise en place pour réaliser l'achat et la vente de produits, il est composé de la sorte :

![protocole](https://i.ibb.co/yf0ZNxC/Protocole.png)



les informations envoyées en objets sont constitués sous la forme de csv pour identifier les différents champs, il est donc interdit d'avoir des types de produits ou des noms d'agents contenant des virgules.

| Etape | Performative      | Contenu                                                      |
| ----- | ----------------- | ------------------------------------------------------------ |
| 1     | `CFP`             | nom_acheteur                                                 |
| 2     | `PROPOSE`         | nom_acheteur, quantité_proposée, prix_vente, nom_vendeur     |
| 3     | `ACCEPT_PROPOSAL` | nom_acheteur, meilleur_quantité_proposée, meilleur_prix_vente, nom_meilleur_vendeur |
| 3     | `REJECT_PROPOSAL` | Vide                                                         |
| 4     | `CONFIRM`         | nom_acheteur, meilleur_quantité_proposée, meilleur_prix_vente, nom_meilleur_vendeur |

La redondance d'information permet de s'assurer que la transaction se passe correctement et ce, de manière asynchrone.