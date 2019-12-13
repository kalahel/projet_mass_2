# Projet SMA

### Auteurs :

Mathieu **Hannoun**

Henri **Bertrand**

### Introduction

Ce projet à été réalisé dans le cadre du Master 2  Intelligence Embarquée de l'université Cergy-Pontoise.

La documentation du code est sous forme de Javadoc, ce document illustre le fonctionnement des différents agents/behaviours de façon schématique.

### Notes

Les `MessageTemplate` sont utilisés pour empécher de défiler les messages qui ne sont pas utilisés par le `behaviour` appelant `receive()`, cela permet de dissocier le comportement de vente de celui d'achat.

## Agents

### ProducerConsumer



## Behaviours

### BuyingBehaviour

Comportement permettant de réaliser un seul et unique achat de marchandise à un vendeur.

Le vendeur choisi est celui proposant la meilleure offre en terme de prix unitaire.

Ce comportement est considéré comme terminé une fois la transaction réalisée.

C'est une machine à état comme suit :

![finite state machine diagram](https://i.ibb.co/HB9VkFc/buying-Behaviour.png)



## Selling behaviour

Routine cyclique qui gère les réponses à envoyées aux acheteurs.

Soit :

- Fais une proposition de vente basé sur le bonheur actuel de l'agent.
- Envoie une confirmation de vente à l'acheteur.

## Protocole de vente

Un protocole léger à été mise en place pour réaliser l'achat et la vente de produits, il est composé de la sorte :

![protocole](https://i.ibb.co/yf0ZNxC/Protocole.png)



les information envoyées en objets sont constitué sous la forme de csv pour identifier les différents champs, il est donc interdit d'avoir des types de produits ou des noms d'agents contenant des virgules.

| Etape | Performative      | Contenu                                                      |
| ----- | ----------------- | ------------------------------------------------------------ |
| 1     | `CFP`             | nom_acheteur                                                 |
| 2     | `PROPOSE`         | nom_acheteur, quantité_proposée, prix_vente, nom_vendeur     |
| 3     | `ACCEPT_PROPOSAL` | nom_acheteur, meilleur_quantité_proposée, meilleur_prix_vente, nom_meilleur_vendeur |
| 3     | `REJECT_PROPOSAL` | Vide                                                         |
| 4     | `CONFIRM`         | nom_acheteur, meilleur_quantité_proposée, meilleur_prix_vente, nom_meilleur_vendeur |

La redondance d'information permet de s'assurer que la transaction se passe correctement et ce, de manière asynchrone.