# Final report

## Korte beschrijving
Deze applicatie is handig voor als je benieuwd bent naar het aantal kilometers dat je meestal rent of wilt gaan rennen.
Je kan met deze app markers plaatsen op een kaart en gelijktijdig de afstand tussen de markers zien. Je kan je target opgeven zodat je weet hoeveel markers je nog moet zetten en als je je route hebt opgeslagen kan je in de volgende activity je target zien tijdens het rennen. Verder is het mogelijk om je afgelegde afstand te zien en kan je de muziekspeler handmatig bedienen. Als je vragen hebt over de app dan kan je terecht in de helpactivity en als je het dan alsnog niet begrijpt kan je contact met mij opnemen.

## Technische design
Om de afstand te bepalen gebruik ik methods die ik heb geïmporteerd. Om de muziekspeler te bedienen gebruik ik een songsmanager class die alle liedjes importeert en de media player buttons handelt. Voor de rest gebruik ik een GPSTracker om de locatie te bepalen.

## Uitdagingen die ik ben tegengekomen en belangrijke gemaakte veranderingen
* Aan het begin vond ik het erg lastig om te begrijpen hoe je aan een polyline de juiste coördinaten moest meegeven zodat het een lijn kan trekken op je kaart. Ik dacht dat GoogleMaps vanzelf wel een redelijke wandelroute zou maken maar het bleek zo te zijn dat GoogleMaps de kortste afstand bepaalt en alle lijnen aan de hand daarvan tekent. Dus de lijnen gaan ook door gebouwen heen. Aangezien er geen algoritme is die het plaatsen van lijnen tussen coördinaten goed zet zodat het echt een wandelroute wordt, is het dus erg belangrijk om de gebruiker goed te informeren over hoe ze de markers moeten plaatsen. 
* Ik wilde eigenlijk dat de gebruiker een paar markers zet en als de totale afstand tussen de markers, kleiner zou zijn dan de ingevoerde goal, dat mijn app vanzelf de route afsluit en dus een route bedenkt waarvan de totale lengte ongeveer even lang wordt als de goal en waarbij mijn eindpositie gelijk is aan mijn startpositie.
* Verder wilde ik in mijn start run activity een tablayout met een streetview in de middelste tab. Dit voor elkaar krijgen was erg lastig, omdat ik nog niet goed genoeg wist wat het verschil is tussen een fragment en een activity. Verder wist ik ook niet waar de adapter de fragments inlaadt en ze toont. Hierachter komen heeft mij veel tijd gekost. Ook heb ik gehad dat ik geen streetview te zien kreeg, omdat de locatie van het Science Park op Google StreetView voor android niet zichtbaar is. 
* In de derde tab wilde ik mijn geschienis aan routes tonen maar dat heb ik achterwege gelaten, daar ben ik niet aan toegekomen.

## Argumenten voor de veranderingen, trade-offs
Het was voor mij niet mogelijk om een algoritme te schrijven die de route afsluit door zelf de route aan te vullen tot de gewenste afstand is bepaald. De trade-off hiervan is dat de gebruiker wel zelf goed de marker moet plaatsen.
Voor de rest heb ik niks veranderd aan wat ik aan het begin wilde doen, alleen heb ik een aantal dingen niet gehaald, ik wilde heel veel doen in maar 4 weken.

