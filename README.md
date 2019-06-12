# App-Inventor-MQTT-Extension
MQTT Extension for App Inventor with Tutorial

This repository includes the source code for the MQTT extension I developed. Also you can find the example code for a setup with Tinkerforge sensors. 
Feel free to modify the source code for yourself and to report any issues.
To get the extension, please contact me.

## Introduction
App Inventor gives you the possibilty to include extensions for features that are currently missing. In this case I implemented a MQTT extension with the newest Java client library from Eclipse Paho.
To read more about it, you can open the PDF file in the the folder with documentation. In this App Inventor and MQTT is explained and how you can make your own extension. Additional there are examples to how to use this extension.

## Source Code
I give you the possibility to alter this source code. Clone the repo and start developing. Note that you also need the App Inventor source code https://github.com/mit-cml/appinventor-sources.

## What can the extension? What is missing?
This extension makes a TCP connection to a broker. You can publish un- and subscribe on topics (wildcard subscriptions are possible). Additional you can disconnect from a broker and define last will messages. 
Currently it is only possible to make a connection with TCP. However I would like to implement a way to establish a TLS/SSL connection. Also I would like to upgrade the MQTT version from 3.1.1 to 5, when Paho is ready.

## Docker
You can host your own instance of App Inventor, how you can do this is described on their GitHub site. Because it takes some time to set all up, I created a Docker image which does all these steps. You can find it on DockerHub, https://hub.docker.com/r/alnigg/appinventor.


