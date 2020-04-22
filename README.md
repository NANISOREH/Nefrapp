 Mapping di servizio:
 - localhost:8080/utenti per vedere e creare utenti correttamente usabili per login e autenticazione/autorizzazione
 - localhost:8080/clean per pulire la table Utenti
 
 Note: 
- È meglio evitare di alterare lo schema manualmente con query SQL, perché Hibernate/JPA non potrebbero tracciare tali alterazioni e quindi il codice lavorerebbe su uno schema mappato male. Per droppare e ricreare lo schema in caso di necessità basta settare spring.jpa.hibernate.ddl-auto=create-drop in application.properties.
- Stiamo condividendo il pom.xml quindi stiamo attenti a rimozioni e modifiche di versione. Le aggiunte invece dovrebbero essere ok.
