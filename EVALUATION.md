# Evaluation

**By:** Tapio Malmberg

**Target:** Juuso Melentjeff

## Documentation

Although not hard to figure out, information on how to set up the API key would have been nice to have in the README.

The README contains messages to the teacher, which could have maybe been communicated via some other channel.

The code itself contains a good amount of comments.

## UI & Usability

The UI looks stylish and the font size is large and readable.

Since there are no interactive elements in the UI, not much else can be said about the usability. The only interaction with the user is when the app requests the permission to use the device's location.

## Code

The code is all in one file, but since there is not a lot of it, it is not really an issue.

Some minor issues can be pointed out:

- The onCreate function in MainActivity could be split into smaller functions.
- Variable naming: the local variable "API_KEY" uses the naming convention for constants and the parameter name "cb" would be better called just "callback".
- The downloadUrlAsync function has a parameter "context" that is unused.
- Some of the lines are a bit long, which could be easily fixed with Android Studio's code formatter.
- There is no handling of exceptions.

## Bugs

The app does not seem to work properly when you run it for the first time. After the permission to use the location is given, the UI does not get updated with the weather information (onRequestPermissionsResult is not overridden in MainActivity to deal with the result of the permission request). Due to this, I had to restart the app to see the weather information.

## Summary

The application fills the requirement of using a REST API and also uses a device API, but contains very little functionality beyond that.

There are no major issues in the code, but the app itself is not completely bug-free due to the issue mentioned above.

Grade: 3
