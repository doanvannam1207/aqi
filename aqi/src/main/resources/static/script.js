var map = L.map('map').setView([40, -100], 4);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19
}).addTo(map);

// Fetch air quality data from backend
fetch('/airquality')
    .then(response => response.json())
    .then(data => {
        data.forEach(location => {
            L.marker([location.latitude, location.longitude]).addTo(map)
                .bindPopup(`<b>${location.location}</b><br>Air Quality Index: ${location.airQualityIndex}`)
                .openPopup();
        });
    })
    .catch(error => console.error('Error fetching air quality data:', error));
