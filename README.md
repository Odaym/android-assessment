# Android Assessment
You are probably familiar with the Spotify service. It’s the biggest music streaming platform in the world and they have a nice and well documented API that you will be using in this assignment.

We tried to create an Android application which would allow us to log in and search for artists and tracks on Spotify. But we only had 20 minutes to build it and this project is the final result! Your task is to help us make some improvements to the quality of the code, and the functionality, robustness and UI of the app.

We want the app to consist of the following parts:

- Login Screen - this screen is already done
- Search Screen - consisting of a search bar and a list of results
- Artist or Track page - consisting of the name, image, and 2 or more interesting fields from either an artist or a track item (you don’t have to support both types)


## Requirements

### Caching
- The app must perform some sort of caching of fetched API data, if a screen is reloaded we should expect to see the cached data before the refetched data

### Responsiveness
- The UI must be updated in real-time, according to the refresh rule explained above.

### Resilience
- The user should be informed if an error occurred while fetching data.
- If no network is available when a request is due, the app should park the call and perform it as soon as network is back.

### UI
- You can decide on your own how will the app look, if you need a guide, feel free to look at the sample app as an example.

### Third party libraries
- We have implemented the HTTP requests using Retrofit, but we would like to see how you would solve this without using any such library. So OkHttp and some JSON parsing library is ok but nothing more.
- Aside from this, you’re free to use whatever libraries you wish to get the job done, however you should consider if it prevents us from being able assess the quality of your work.

### Testing
- We would like to see how you write unit tests for at least one class
- We recommend you to test your solution on real hardware.

### Spotify API reference:

https://beta.developer.spotify.com/documentation/web-api/reference/search/search/
https://beta.developer.spotify.com/documentation/web-api/reference/tracks/
https://beta.developer.spotify.com/documentation/web-api/reference/artists/

Use your own Spotify API key when developing, we will replace it with ours when reviewing your assignment.

## General notes
- Write the solution in Kotlin.
- Pay attention to the quality of the code, and the overall design of your software.
- Document your API and classes where you think it is necessary.

Good luck!
