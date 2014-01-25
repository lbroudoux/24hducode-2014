var map;
var autocomplete;
var watchID;
var guyMarker;

$(document).ready(function(){

	findMyCurrentLocation();
		  $("#btnLocateMe").click(function(){
			findMyCurrentLocation();
		  });
        var options = {
           types: ['(cities)'],
           componentRestrictions: {country: 'fr'}
        };
        var input = $("#location");
        autocomplete = new google.maps.places.Autocomplete(input, options);          
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


}

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