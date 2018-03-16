# HHC18_Sample_App

Sample of basic application leveraging Stellar LBS SDK to deliver Indoor Positioning services : position user on indoor map, receive position updates, apply wayfinding between user position and the given POI etc...

## Preamble

You will need an:
-Android phone (Android 6+) to run the application.
-Android Studio 3.0 to compile the source code.
-An infrastructure of Alcatel-Lucent Enterprise Stellar LBS beacons deployed in a venue. You can create an account here : www.omniaccess-stellar-lbs.com
or re-use the sample API KEY provided in source code (free of charge)
-An indoor map for the venue. sample API KEY for map are also provided in source code.

caution: API KEYs can be removed at any time without disclaimer.

### Installing

Compile the project under Android Studio and use ADB to install the app on your phone.

## Running the tests

By default without being located in a venue with beacons, your position will be the GPS position.
To emulate the positioning on a site the file 'brest.gwl' can be uploaded on the phone into :
<APP_INSTALLATION_PATH>/.nao/replay
replay folder need to be created.

## Contributing

This demo app has been built with partner Mapwize delivering the indoor map. Please see https://docs.mapwize.io/android-sdk/
Other providers like VisioGlobe are also delivering super nice indoor maps. Please see https://developer.visioglobe.com/docs/VisioMoveEssential-Android/Doc/VisioMoveEssential-Android/html/index.html

