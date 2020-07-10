 Mapping di servizio:
 - localhost:8080/utenti per vedere e creare utenti correttamente usabili per login e autenticazione/autorizzazione
 - localhost:8080/clean per pulire la table Utenti
 
 Note: 
- È meglio evitare di alterare lo schema manualmente dall'esterno con query SQL, perché Hibernate/JPA non potrebbero tracciare tali alterazioni e quindi il codice lavorerebbe su uno schema mappato male. Per droppare e ricreare lo schema in caso di necessità basta settare spring.jpa.hibernate.ddl-auto=create-drop in application.properties.
- Stiamo condividendo il pom.xml quindi stiamo attenti a rimozioni e modifiche di versione. Le aggiunte invece dovrebbero essere ok.
- Questo è l'application.properties su cui mi trovo io (ometto le credenziali del database):
```
spring.mvc.view.prefix: /
spring.mvc.view.suffix: .jsp
spring.application.name=Nefrapp
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.connection.characterEncoding=utf-8  
spring.jpa.properties.hibernate.connection.CharSet=utf-8  
spring.jpa.properties.hibernate.connection.useUnicode=true
```
