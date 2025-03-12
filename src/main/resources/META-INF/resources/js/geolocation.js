function updateCoordinates(position, element){
    // These are the coordinates returned by the Geolocation API
    let latitude = position.coords.latitude;
    let longitude = position.coords.longitude;

    // send event to View on server
    // see https://vaadin.com/blog/asynchronous-javascript-execution-in-vaadin-flow
    // Method 2: Listen to custom JS events
    const event = new Event("geo-location-obtained");
    event.lat = latitude;
    event.lon = longitude;
    element.dispatchEvent(event);
}

function geolocationInaccessible(message, element){
    // send event to View on server
    const event = new Event("geo-location-inaccessible");
    event.message = message;
    element.dispatchEvent(event);
}

/* This function is called from view controllers */
function requestGeoLocation(element){
    console.log(element);
    if (navigator.geolocation){
        navigator.geolocation.getCurrentPosition(
            function(position) { updateCoordinates(position, element) },
            function(error) { geolocationInaccessible(error.message, element) }
        );
    }
    else {
        geolocationInaccessible("Your browser does not support the Geolocation feature", element)
    }
}
