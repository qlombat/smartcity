# SmartCity :vertical_traffic_light:


Projet dans le cadre du cours **INFOM453 Laboratoire en informatique ambiante et mobile** sur le sujet des *Smart cities*.

Serveur/Scala | Maquette/Phidgets
------------- | -------------
Quentin       | Justin
Jérémy        | Noé
Noé           | Quentin


## Phase 1 : Design

### 3 tâches
- **Passer des zones routières en zones piétonnes et inversement :**
Nous entendons par là la possibilité de rendre une partie de la ville ou la ville, dans son intégralité, piétonne. Cette mesure pourrait être de courte durée ou prolongée à long terme. Afin de réaliser cette première tâche, aucun capteur n’est réellement nécessaire, seuls des feux ainsi que des écrans d’affichage seraient nécessaires. Ces derniers permettraient de dévier les véhicules pour les faire contourner les zones devenues piétonnes et ainsi bloquer l’accès à celles-ci.

- **Prioriser certains véhicules (bus) :**
Cette deuxième tâche a pour but de privatiser certaines voies aux transports en commun afin que ceux-ci respectent le plus possible leurs horaires annoncés. Actuellement la plupart de ces voies sont réservées en permanence ce qui n’est pas solution adéquate. En effet, les transports en commun circulent à des fréquences différentes en fonction des périodes de la journée et de l’année (ex. : vacances scolaires), il n’est donc pas nécessaire de constamment privatiser ces bandes. Ces dernières pourraient donc être partagées avec les usagers quotidiens afin de ne pas surcharger le réseau lorsque cela n’est pas nécessaire. Nous aurons désormais besoin de capteurs, notamment des capteurs de présence de véhicules afin de vérifier si le réseau est surchargé ou non. Des périphériques d’affichage seront également utiles afin de signaler aux usagers s’ils peuvent ou non circuler sur les bandes de bus.

- **Adapter la circulation en fonction d’événements / de l’environnement :**
Cette troisième et dernière tâche est la plus complexe, en effet, elle permet d’adapter le trafic en fonction de plusieurs critères tels que des événements (prévus : concerts, événements sportifs, etc. et non prévus : accidents de la route, etc.), la météo, le taux de pollution, etc. Plusieurs capteurs seront dès lors nécessaires, notamment des capteurs de température au sol, des capteurs de pollution, des capteurs de présence. De plus, afin d’être au courant des événements imprévus tels qu’un accident de la route, une interface utilisateur serait nécessaire (application mobile ou web). En plus de ces trois tâches principales, il va de soi que notre système gérera l’intégralité des feux de signalisation du système. Ceci afin d’assurer un fonctionnement “classique” de ces feux (ex. : piétons souhaitant traverser).

### Maquette
Dessin abstrait de la maquette disponible [ici](https://docs.google.com/drawings/d/1vkHto2qSTscVSHUA4KIwAOQl6Z28mmhF0PmFOPPPtXE/edit?usp=sharing) (7/3/2017)

### Arduino - Maquette
Emplacement de chaque LED géré par l'arduino. Le numéro du port associé y est précisé  [ici](https://docs.google.com/drawings/d/1HutxQbEiszSulH7tbw2Y27EUrotwv26EINmu5cpH85Q/edit?usp=sharing) (05/04/2017).


## Phase 2 : Phidgets & Arduino
### Arduino
Le dossier libraries contient toutes les librairies utiles pour faire tourner le code sur l'arduino. Copiez simplement le contenu de ce dossier dans le dossier librairies de votre installation arduino.

Utilisation des ports sur l'arduino :
- Digital
  - Zone NO : 1
  - Zone NE : 2
  - Zone N : 1, 2 et 3
  - Zone SO : 5
  - Zone SE : 6
  - Zone S : 4, 5 et 6
  - Zone Bus : 7
  - Déviation Est : 0
- Analogue
  - Temperature et himidité : A0
    - Déviation Ouest : A6
- Autre
  - LCD : 11 (SDA) et 12 (SCL)

### Phidgets

Port | Sensor                                |Input |Output
-----|---------------------------------------|------|-------------------------------------------------
0    | MainCarDetector West :car:            |/     |MainTrafficLight :red_circle:
1    | MainCarDetector East :blue_car:       |/     |MainTrafficLight :white_check_mark:
2    | SecondaryCarDetector South :car:      |/     |AuxiliaryTrafficLight :red_circle:
3    | SecondaryCarDetector North :blue_car: |/     |AuxiliaryTrafficLight :white_check_mark:
4    | IndorLightSensor :bulb:               |/     |PedestrianTrafficLight :large_orange_diamond:
5    | /                                     |/     |PublicLight1 :low_brightness:
6    | /                                     |/     |PublicLight2 :low_brightness: / :high_brightness:
7    | /                                     |/     |PublicLight3 :high_brightness:

## Phase 3 : Scala
Pour pouvoir compiler le projet, il faut installer la librairie Phidgets dans votre repository local Maven :
- Télécharger la documentation [ici](http://www.phidgets.com/documentation/JavaDoc.zip)
- Dézipper le ficier .zip télécharger et ouvrir un terminal dans le dossier résultant de dézippage.
- Lancer la commande `jar cf ../phidget-javadoc.jar .`
- Télécharger la librairie Java de Phidgets [ici](http://www.phidgets.com/downloads/libraries/phidget21jar.zip)
- Lancer la commande `mvn install:install-file -Dfile=phidget21.jar -DgroupId=com.phidgets -DartifactId=phidget -Dversion=2.1 -Dpackaging=jar -Djavadoc=phidget-javadoc.jar` dans le dossier où se trouvent les .jar de la librairie et de la JavaDoc de celle-ci.
- S'assurer que la dépendance suivante se trouve dans le pom.xml (ce qui est normalement déjà le cas) :
```
<dependency>
    <groupId>com.phidgets</groupId>
    <artifactId>phidget</artifactId>
    <version>2.1</version>
    <scope>compile</scope>
</dependency>
```
