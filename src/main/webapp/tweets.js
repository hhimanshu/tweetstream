// If you're adding a number of markers, you may want to
// drop them on the map consecutively rather than all at once.
// This example shows how to use setTimeout() to space
// your markers' animation.

var sre = new google.maps.LatLng(29.9640, 77.5460);
var map;

function initialize() {
  var mapOptions = {
    zoom: 2.5,
    center: sre
  };

  map = new google.maps.Map(document.getElementById('map-canvas'),
    mapOptions);
  dropPins();
}

function dropPins() {
  var connection = new WebSocket('ws://127.0.0.1:8080/tweetstream-1.0-SNAPSHOT/tweets');
  connection.onopen = function() {
    connection.send('brazil');
  };
  connection.onerror = function(error) {
    console.log('WebSocket Error ' + error);
  };
  connection.onmessage = function(e) {
    var parse = JSON.parse(e.data);
    var coordinates = parse["geo"]["coordinates"];
    console.log("coordinates:" + JSON.stringify(coordinates, undefined, 2));
    addDynamicMarker(new google.maps.LatLng(coordinates[0], coordinates[1]));
  };
}

var image = {
  url: 'assets/point.png',
  size: new google.maps.Size(20, 32)
};

function addDynamicMarker(location) {
  var marker = new google.maps.Marker({
    position: location,
    map: map,
    draggable: false,
    icon: image
  });
  setTimeout(function() {
    marker.setMap(null);
    delete marker;
  }, 4000);
  markers.push(marker);
}
google.maps.event.addDomListener(window, 'load', initialize);