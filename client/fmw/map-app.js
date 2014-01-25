var map;
var autocomplete;
var watchID;
var guyMarker;
var ReferenceError;

$(document).ready(function(){

	findMyCurrentLocation();
		  $("#btnLocateMe").click(function(){
			findMyCurrentLocation();
		  });
        var options = {
           types: ['(cities)'],
           componentRestrictions: {country: 'fr'}
        };
        //var input = $("#location");
        //autocomplete = new google.maps.places.Autocomplete(input, options);          
});

function findMyCurrentLocation(){
	var geoService = navigator.geolocation;
	if (geoService) {
		navigator.geolocation.getCurrentPosition(showCurrentLocation,errorHandler,{enableHighAccuracy:true});
        watchID = navigator.geolocation.watchPosition(function(position) {
          refreshMap(position.coords.latitude, position.coords.longitude);
        });        
	} else {
		alert("Your Browser does not support GeoLocation.");
	}
}

function showCurrentLocation(position){
  
    console.log(position.coords.latitude + "," + position.coords.longitude);  
	//Create the latlng object based on the GPS Position retrieved
	var latlng = new google.maps.LatLng (position.coords.latitude, position.coords.longitude);
	
    //var zoom = determineZoomLevel();
    
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
  guyMarker = new google.maps.Marker ({ map : map, 
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
               lastCenterLatLng = null;
               // Update the map and then the trails.
               map.fitBounds(results[0].geometry.viewport);
               map.setCenter(results[0].geometry.location);
               
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
      
      
function refreshMap(latitude, longitude) {
    var latlong = new google.maps.LatLng(latitude,longitude);
     map.setCenter(latlong);
     guyMarker.setPosition(latlong);
}


function showPath() {
    //on récupere les points
/*    "1001","1001","Longs Champs","Rennes","48.12903716","-1.6325079","","","","","","1"
"1002","1002","Bouzat","Rennes","48.12826604","-1.63765619","","","","","","1"
"1003","1003","Gallets","Rennes","48.12744122","-1.64029002","","","","","","1"
"1004","1004","Donzelot","Rennes","48.12528847","-1.64253775","","","","","","1"
"1005","1005","Mirabeau","Rennes","48.1233588","-1.64623199","","","","","","1"
"1006","1006","Turmel","Rennes","48.12226258","-1.65069568","","","","","","1"
"1007","1007","Assomption","Rennes","48.12141888","-1.65505801","","","","","","1"
*/
    var points = [
        {latitude : 48.12903716, longitude : -1.6325079,isPOI : true },
        {latitude : 48.12826604,longitude : -1.63765619,isPOI : false },
        {latitude : 48.12744122,longitude : -1.64029002,isPOI : true }
        ];
    //on les affiches
    map.setCenter(latlong);
   for (var i=0; i<points.length; i++){
      var location = new google.maps.LatLng(points[i].latitude, points[i].longitude);
      var marker;
      if (points[i].isPOI){
         marker = new google.maps.Marker({
            position: location, map: map,
            title: "Point #" + (i + 1),
            icon: markerIcon, shadow: shadowIcon
         });
      } else {
        marker = new google.maps.Marker({
           position: location, map: map,
           title: "Point #" + (i + 1),
           icon: markerIcon
        });
      }
      markers[i] = marker;
      //addMarkerListener(marker, i);
      // Complete trail line drawing.
      polyline.getPath().push(location);
   }
   //if (trail.isRoundtrip == true){
   //   polyline.getPath().push(markers[0].position);
   //}
};

/*
function determineZoomLevel(){
   if (trail.distance < 2){
      return 14;
   } else if (trail.distance < 5){
      return 13;
   } else if (trail.distance < 10){
      return 12; 
   } else if (trail.distance < 50){
      return 11;
   } else if (trail.distance < 100){
      return 10;
   } else if (trail.dsitance < 200){
      return 9;
   } else if (trail.distance < 300){
      return 8;
   } else if (trail.distance < 400){
      return 7;
   }
   return 3; 
};
*/