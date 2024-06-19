*Toto je repozitár so serverovou časťou semestrálnej práce.*
*Klientská časť je tu: https://github.com/BBOFY/Spring_REST_client*

Téma: "Paintballové zápasy vo vozidlách"

V databázi budú prítomné 3 hlavné entity:

**Team**
- *key* idTeam *[Integer]*
- teamName *[String]*
- score *[Integer]*
- numberOfPlayers *[Integer]*

**Vehicle**
- *key* idVehicle
- licensePlate *[String]*
- vehicleName *[String]*
- type *[String]*
- *nullable* nickname *[String]*

**Sponsor**
- *key* idSponsor
- sponsorName *[String]*
- *nullable* industry *[String]*


Relácie medzi entitami:
- Team ----< Vehicle (1:M)
- Team >---< Sponsor (M:N)

Obe relácie sú nepovinné

**Spustenie serveru**

- z repozitára https://github.com/BBOFY/Spring_REST_client stiahnite Artifacts/build-gradle
- stiahnutý archív rozbaľte
- pomocou `java -jar <file>` v termináli spustite súbor `build/libs/polakemi_server.jar`, ktorý sa nachádza v rozbalenom archíve
  (testované s openjdk-16)
- server by mal fungovať bez problémov
