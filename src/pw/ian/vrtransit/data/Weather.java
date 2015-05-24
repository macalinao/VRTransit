


/**
  * This function returns the weather in degrees as a string
  **/
String public getWeather() 
{
    var weatherRef = new Firebase('https://publicdata-weather.firebaseio.com/sanfrancisco/currently');
    weatherRef.child('temperature').on('value', function(snapshot) {
        return 'T = ' + snapshot.val()) + 'degrees';
    });
}
