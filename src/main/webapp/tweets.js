// If you're adding a number of markers, you may want to
// drop them on the map consecutively rather than all at once.
// This example shows how to use setTimeout() to space
// your markers' animation.

var map;
var port = (document.location.host.indexOf('localhost') >= 0) ? undefined : 8000;
var url = (port !== undefined) ? 'ws://' + document.location.host + port + document.location.pathname + 'tweets' :
  'ws://' + document.location.host + document.location.pathname + 'tweets';
function initialize() {
  var mapOptions = {
    zoom: 2,
    center: new google.maps.LatLng(0, 0)
  };

  map = new google.maps.Map(document.getElementById('map-canvas'),
    mapOptions);
  dropPins();
}

function dropPins() {
  var connection = new WebSocket(url);
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
  }, 2000);
  markers.push(marker);
}
google.maps.event.addDomListener(window, 'load', initialize);