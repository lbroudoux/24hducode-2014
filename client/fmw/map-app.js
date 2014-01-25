var map;
var autocomplete;

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
	} else {
		alert("Your Browser does not support GeoLocation.");
	}
}

function showCurrentLocation(position){
  
    console.log(position.coords.latitude + "," + position.coords.longitude);  
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