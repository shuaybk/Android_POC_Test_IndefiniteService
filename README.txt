INTRO
	
    This is a POC test app that creates a Service to run in the foreground indefinitely with a notification.
This means the Service does not stop unless the app is explicity shut down by the user.  If the system
kills the main activity due to resource constraints, the Service does not pause/stop.

    The Service also needs to be able to communicate with the main Activity, so it needs to be a "Bound"
Service as well (Services can be both Bound and Started).  A Bound Service will run as long as it is bound
to a component (in this case, our main Activity).

    The app lets you start and stop a timer and it displays a count.  Once the timer is started, it
displays a notification that also allows stop/start control from there.  The timer will never stop if it is
started unless explicity told to by control or if the app is closed and the notification is cleared.


HOW IT WORKS

    The Service needs to be a "Started" service with START_STICKY type.  This means the Service runs
indefinitely until it is shut down explicity in code.  However, it might still pause if the app is in
the background due to the way Android OS manages system resources.  To keep it from stopping, it needs
to be a "Foreground" Service.

    A Foreground Service means it displays a notification that the user sees.  As long as the notification
is displayed, it is a Foreground Service and will not be paused.

    In order for the UI of the Activity to be updated by the Service, we need to Bind the Service to the
Activity.  This allows the Activity to query the Service for data.  We use BIND_AUTO_CREATE type to Bind
them, meaning the Activity keeps rebinding on recreation to the Service (like on screen rotation).

    So finally, it comes together like this:

	1) The Activity stays bound to the Service at all times while it's open.  When the app launches,
	   the Activity binds itself to the Service (onCreate() and onBind() get called in the Service)
	   using BIND_AUTO_CREATE.

	2) Start the Service:  The Activity has the ability to "Start" something (in this case, a timer).  To
	   ensure the timer keeps running indefinitely, make it a Started Service by calling startService()
	   from the Activity.  This will also launch a notification to make it a Foreground Service so it
	   runs indefinitely.

	3) Stop the Service:  The Activity can stop the Service at any time by calling stopService().  Now
	   it won't run indefinitely but it still won't be destroyed as long as the Activity is open
	   because it is still bound.  With the Service stopped and the the Activity destroyed, the Service
	   may stop.


WHAT TO USE IT FOR

    Any time you need an indefinitely running service.  This could be used as a music player (constantly
runs in the background even with screen off and app minimized, controlled from App UI or from notification).

    Could be used as a timer/stop watch that runs in the background even with the screen off.