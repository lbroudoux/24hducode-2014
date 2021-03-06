﻿var map;
var autocomplete;

$("#location").keyup(function(event){
    if(event.keyCode == 13){
        geocode();
    }
});

$(document).ready(function(){

	findMyCurrentLocation();
		  $("#btnLocateMe").click(function(){
			findMyCurrentLocation();
		  });
        var options = {
           types: ['(cities)'],
           componentRestrictions: {country: 'fr'}
        };
        var input = document.getElementById("location");
        autocomplete = new google.maps.places.Autocomplete(input);
});

function findMyCurrentLocation(){
	var geoService = navigator.geolocation;
	if (geoService) {
		navigator.geolocation.getCurrentPosition(showCurrentLocation,errorHandler,{enableHighAccuracy:true});
	} else {
		alert("Your Browser does not support GeoLocation.");
	}
}

function showCurrentLocation(position){
  
    console.log(position.coords.latitude + "," + position.coords.longitude);
    traceur.lat = position.coords.latitude;
    traceur.lng = position.coords.longitude;
	//Create the latlng object based on the GPS Position retrieved
	var latlng = new google.maps.LatLng (position.coords.latitude, position.coords.longitude);
	
	//Set Google Map options
	var options = { 
    zoom : 15, 
    center : latlng, 
    streetViewControl: false,
    mapTypeId : google.maps.MapTypeId.ROADMAP 
    };

  var $content = $("#map-page div:jqmData(role=content)");

  //Set the height of the div containing the Map to rest of the screen
  $content.height(screen.height - 50);
  
  //Display the Map
  map = new google.maps.Map ($content[0], options);

  //Change to the map-page
  $.mobile.changePage ($("#map-page"));

  //Create the Marker and Drop It
  new google.maps.Marker ({ map : map, 
                            animation : google.maps.Animation.DROP,
                            position : latlng  
                          });  
}

function errorHandler(error){
	  alert("Error while retrieving current position. Error code: " + error.code + ",Message: " + error.message);
}



 function geocode(){
         var address = $("#location").val();
         var geocoder = new google.maps.Geocoder();
         geocoder.geocode( {'address': address}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
               // Reset lastCenterLatLng to avaid firing of update on bounds changed.
               lastCenterLatLng = null;
               // Update the map and then the trails.
               map.fitBounds(results[0].geometry.viewport);
               map.setCenter(results[0].geometry.location);
               traceur.destLat = results[0].geometry.location.d;
               traceur.destLng = results[0].geometry.location.e;

               $.ajax({
                dataType: "json",
               type: 'GET', // Le type de ma requete
              url: '../path/find.json', // L'url vers laquelle la requete sera envoyee
              data: {
                lat: traceur.lat, // Les donnees que l'on souhaite envoyer au serveur au format JSON
                lng: traceur.lng,
                destLat: traceur.destLat,
                destLng : traceur.destLng
              }, 
              success: function(data) {
                traceur.setPaths(data.paths);
                //traceur.setPaths(bouchon.paths.concat(bouchon.paths));
                traceur.clearAllItems();
                traceur.tracerPath();
              },
              error: function(data) {
                alert("fail");
              }});
               
            } else {
                var errors = [];
                errors.push({
                   field: "", 
                   message: "${message(code:'trail.error.search.message')}"
                   });
                showErrorsDialog("une erreur pas cool ", errors, "Msg a ajouter par la suite");
            }
         });
      }

var traceur = {
curent : 0,
arret : new google.maps.MarkerImage('C:/workspace/bis/client/fmw/images/markerHL.png'),
paths : null,
curentLigne : null,
icone : ["./images/blue-dot.png",
"./images/red-dot.png",
"./images/green-dot.png",
"./images/ltblue-dot.png",
"./images/yellow-dot.png",
"./images/purple-dot.png",
"./images/pink-dot.png"],
petiteIcone : ["http://maps.google.com/mapfiles/ms/micons/blue-dot.png",
"http://maps.google.com/mapfiles/ms/micons/red-dot.png",
"http://maps.google.com/mapfiles/ms/micons/green-dot.png",
"http://maps.google.com/mapfiles/ms/micons/ltblue-dot.png",
"http://maps.google.com/mapfiles/ms/micons/yellow-dot.png",
"http://maps.google.com/mapfiles/ms/micons/purple-dot.png",
"http://maps.google.com/mapfiles/ms/micons/pink-dot.png"],
curentCouleur : ["rgb(87,129,252)",
"rgb(252,99,85)",
"rgb(0,225,60)",
"rgb(85,215,215)",
"rgb(252,243,87)",
"rgb(126,85,252)",
"rgb(225,79,158)"],

noeud: [],
iconeNoeud: [],
posNoeud: [],
items:[],

lat: 0,
lng : 0,
destLat : 0,
destLng : 0,

tracerRoute : function(route, param){
  this.nextLigne();
  if(param == 0){
    //this.depart(route.stopTimes[0]);
    noeud[route.stopTimes[0].latitude,route.stopTimes[0].longitude] = "<h3>Départ proposition ";
  } else {
    this.changement(route.stopTimes[0]);
  }
  this.curentLigne = route.stopTimes[0].idLigne;
  for(var i = 1; i < route.stopTimes.length - 1; i++){
      //this.etape(route.stopTimes[i]);
      this.tracer(route.stopTimes[i - 1], route.stopTimes[i]);
    }
  //  this.arrive(route.stopTimes[route.stopTimes.length - 1]);
    this.tracer(route.stopTimes[route.stopTimes.length - 2], route.stopTimes[route.stopTimes.length - 1]);
},

timeFormater : function(chaine){
  if(chaine.length == 5){
    chaine = "0" + chaine;
  }
  return chaine[0] + chaine[1] + "h" + chaine[2] + chaine[3] + "m" + chaine[4] + chaine[5];
},

tracerPath : function(){
  for(var i = 0; i < this.paths.length; i++){
    routes = this.paths[i].routes;
    for(var j = 0; j < routes.length; j++){
      this.nextLigne();
      route = routes[j];
      var clef = "" + route.stopTimes[0].latitude + route.stopTimes[0].longitude;
        if(!this.noeud[clef])
          this.noeud[clef] = "";
      //Départ
      if(j == 0){
        this.noeud[clef] += "<h3>Départ chemin " + (i + 1) + "</h3>";
        this.noeud[clef] += "<h6>" + route.stopTimes[0].name + "</h6>";
        this.noeud[clef] += "<h6>Ligne : " + route.long_name + "</h6>";
        this.noeud[clef] += "<h6>Départ : " + this.timeFormater("" + route.stopTimes[0].arrival_time) + "</h6>";
        this.noeud[clef] += "<h6>Arrivée : " + this.timeFormater("" + routes[routes.length - 1].stopTimes[routes[routes.length - 1].stopTimes.length - 1].arrival_time) + "</h6>";
        this.noeud[clef] += "<h6>Changements : " + (routes.length - 1) + "</h6>";
      } else {//changement
        this.noeud[clef] += "<h6>Vers ligne : " + route.long_name + "</h6>";
        this.noeud[clef] += "<h6>Départ : " + this.timeFormater("" + route.stopTimes[0].arrival_time) + "</h6>";
      }
      this.iconeNoeud[clef] = this.icone[this.curent];
      this.posNoeud[clef] = [route.stopTimes[0].latitude, route.stopTimes[0].longitude];

      for(var k = 1; k < route.stopTimes.length - 1; k++){
        clef = "" + route.stopTimes[k].latitude + route.stopTimes[k].longitude;
        if(!this.noeud[clef])
          this.noeud[clef] = "";
        this.noeud[clef] += "<h5>étape chemin " + (i + 1) + "</h5>";
        this.noeud[clef] += "<h6>" + route.stopTimes[k].name + "</h6>";
        this.noeud[clef] += "<h6>Ligne : " + route.long_name + "</h6>";
        this.noeud[clef] += "<h6>Passage : " + this.timeFormater("" + route.stopTimes[0].arrival_time) + "</h6>";
        this.tracer(route.stopTimes[k - 1], route.stopTimes[k]);
        this.iconeNoeud[clef] = this.icone[this.curent];
        this.posNoeud[clef] = [route.stopTimes[k].latitude, route.stopTimes[k].longitude];
      }

      clef = "" + route.stopTimes[route.stopTimes.length - 1].latitude + route.stopTimes[route.stopTimes.length - 1].longitude;
      //Arrivée
        if(!this.noeud[clef.longitude])
          this.noeud[clef] = "";
      if(j == routes.length - 1){
        this.noeud[clef] += "<h5>Arrivée chemin " + (i + 1) + "</h5>";
        this.noeud[clef] += "<h6>" + route.stopTimes[route.stopTimes.length - 1].name + "</h6>";
        this.noeud[clef] += "<h6>Ligne : " + route.long_name + "</h6>";
        this.noeud[clef] += "<h6>Arrivée : " + this.timeFormater("" + route.stopTimes[route.stopTimes.length - 1].arrival_time) + "</h6>";
      } else {//Changement
        this.noeud[clef] += "<h5>Changement chemin " + (i + 1) + "</h5>";
        this.noeud[clef] += "<h6>" + route.stopTimes[route.stopTimes.length - 1].name + "</h6>";
        this.noeud[clef] += "<h6>Arrivée : " + this.timeFormater("" + route.stopTimes[route.stopTimes.length - 1].arrival_time) + "</h6>";
        this.noeud[clef] += "<h6>De ligne : " + route.long_name + "</h6>";
      }
      this.tracer(route.stopTimes[route.stopTimes.length - 2], route.stopTimes[route.stopTimes.length - 1]);
      this.iconeNoeud[clef] = this.icone[this.curent];
      this.posNoeud[clef] = [route.stopTimes[route.stopTimes.length - 1].latitude, route.stopTimes[route.stopTimes.length - 1].longitude];
    }
  }
  this.ajouterIcones();
},

ajouterIcones: function(){
  for (var key in this.iconeNoeud) {
    if (key === 'length' || !this.iconeNoeud.hasOwnProperty(key)) continue;
    this.marquer(this.posNoeud[key][0], this.posNoeud[key][1], this.iconeNoeud[key], this.noeud[key]);
}

},

depart : function(stop){
  var info;
  this.marquer(stop.latitude, stop.longitude, this.icone[this.curent], "", "depart");
},

arrive : function(stop){
  this.marquer(stop.latitude, stop.longitude, this.icone[this.curent], "", "arrivée");
},

etape : function(stop){
  this.marquer(stop.latitude, stop.longitude, this.petiteIcone[this.curent], "", "étape");
},

changement : function(stop){
  this.marquer(stop.latitude, stop.longitude, this.icone[this.curent], "", "changement");
},

tracer : function(stop1, stop2){
  if(stop1.idLigne != stop2.idLigne){
    return;
  }
  var flightPath = new google.maps.Polyline({
    path: [new google.maps.LatLng (stop1.latitude, stop1.longitude), new google.maps.LatLng (stop2.latitude, stop2.longitude)],
    geodesic: true,
    strokeColor: this.curentCouleur[this.curent],
    strokeOpacity: 1.0,
    strokeWeight: 2
  });
  flightPath.setMap(map);
  this.items.push(flightPath);
},

toutTracer : function(routes){
  for(var i = 0; i < routes.length; i++){
    var j = i;
    if(i == routes.length - 1){
      j = -1;
    }
    this.tracerRoute(routes[i], j);
  }
},

//Icone suivante
nextLigne : function(){
  this.curent++;
  if(this.curent >= this.icone.length){
    this.curent = 0;
  }
},

setPaths : function(paths){
  this.paths = paths;
},

      marquer : function(x, y, etape, info){
        var position = new google.maps.LatLng (x, y);
        var myMarker = new google.maps.Marker({
        // Coordonnées du cinéma
        position: position,
        map: map,
        icon : etape
        });

        var infowindow = new google.maps.InfoWindow({
          content: info
        });
        google.maps.event.addListener(myMarker, 'click', function() {
          infowindow.open(map,myMarker);
        });
        this.items.push(myMarker);
        this.items.push(infowindow);
      },

  requeteServeur : function(){
    this.tracerPath();
  },

  clearAllItems : function(){
    for(var i = 0; i < this.items.length; i++){
      this.items[i].setMap(null);
    }
    this.items = [];
    this.noeud = [];
    this.iconeNoeud = [];
    this.posNoeud = [];
    this.items = [];
  }
}
