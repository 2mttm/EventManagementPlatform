function autoResize(textarea) {
    textarea.style.height = 'auto';
    textarea.style.height = (textarea.scrollHeight) + 8 + 'px';
}
window.addEventListener('load', function() {
    const textarea = document.getElementById('description');
    autoResize(textarea);
});

let map;

function initMap() {
    const map = new google.maps.Map(document.getElementById('map'), {
        center: {lat: 0, lng: 0},
        zoom: 12
    });

    const geocoder = new google.maps.Geocoder();
    const infowindow = new google.maps.InfoWindow();
    const marker = new google.maps.Marker({
        map: map,
        draggable: true
    });

    // Установка маркера по координатам
    function placeMarker(location) {
        marker.setPosition(location);
        map.panTo(location);
        document.getElementById('latitude').value = location.lat();
        document.getElementById('longitude').value = location.lng();
    }

    // Установка маркера при drag-and-drop
    google.maps.event.addListener(marker, 'dragend', function(event) {
        placeMarker(this.getPosition());
    });

    google.maps.event.addListenerOnce(map, 'idle', function() {
        let defaultLocation = {
            lat: parseFloat(document.getElementById('latitude').value),
            lng: parseFloat(document.getElementById('longitude').value)
        };
        if (isNaN(defaultLocation.lat)) {
            defaultLocation = {
                lat: parseFloat(document.getElementById('latitude').getAttribute('value')),
                lng: parseFloat(document.getElementById('longitude').getAttribute('value'))
            }
        }
        placeMarker(defaultLocation);
    });

    // Поиск места по введенному адресу
    document.getElementById('location').addEventListener('input', function() {
        const address = this.value;
        geocoder.geocode({ 'address': address }, function(results, status) {
            if (status === 'OK') {
                map.setCenter(results[0].geometry.location);
                placeMarker(results[0].geometry.location);
            }
        });
    });


}

initMap();