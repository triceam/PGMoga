# PGMoga #

The PGMoga plugin is a PhoneGap native plugin that is used to handle input from a Moga game controller inside of a PhoneGap application targetting the Android OS/platform.

More on Moga controllers:
http://www.mogaanywhere.com/


This implementation is intended to be a proof of concept demonstrating how you could integrate the gamepad within your application.  It currently only supports input from the  joysticks (axisX and axisY) and the A and B buttons, it does not handle all possible input from the controller.

This implementation is adapted directly from the com.bda.controller.example.demo.listen example from the Moga developers SDK samples available for download at:: http://www.mogaanywhere.com/developers/

These samples were developed targeting PhoneGap version 2.9, for more detail see:
http://www.phonegap.com


All samples need the com.bda.controller.jar file that is available as part of the Moga SDK download (target Moga SDK version 1.3.1).

This directory contains the following:

1. ***samples*** folder contains sample implementations
	- ***PGMogaDemo*** folder contains a sample project that simply writes values out to the html interface
	- ***PGMogaDemoGame*** contains a simple game demo that can be controlled using the Moga gamepad
1. ***src*** folder contains source code for the plugin
	- ***java*** folder contains native plugin java (native) interface
	- ***js*** folder contains native plugin JavaScript interface 

You can read more about this sample and see a video of it in action at:
http://www.tricedesigns.com/?p=3298