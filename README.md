### About
This project demonstrates some PWA aspects:
- Obtaining geo coordinates once
- Watching for geo coordinate changes
- Using `<fileStorageUploadField/>` to take photos from camera and from gallery
- Displaying video from camera
- Taking camera shot and uploading it to server

### Launching
To test features from a smartphone, you need to launch the application with `ssl` profile (add `SPRING_PROFILES_ACTIVE=ssl` environment variable to the run configuration). A self-signed certificate will be used which is enough to test features from the phone's browser.

#### Installing as PWA to smartphone
Installing this app as PWA to smartphone requires you to launch it on a server with a trusted SSL certificate.

### Features
#### Obtaining geo coordinates
Menu -> Cases -> Create.
In the detail view, Latitude and Longitude fields will be automatically filled.

Code:
- com.haulmont.pwacapsdemo.view.caraccidentcase.CarAccidentCaseDetailView
- com.haulmont.pwacapsdemo.component.GeoLocationAccess
- src/main/resources/META-INF/resources/js/geolocation.js

#### Watching for geo coordinate changes
Looks like watching for geolocation changes will work only when app is launched in foreground. Background mode is not supported! See links:
- https://github.com/w3c/geolocation-sensor/issues/22
- https://github.com/w3c/ServiceWorker/issues/745
- https://github.com/w3c/geolocation/issues/74

Menu -> Location watcher.

Open and walk with your phone. New coordinates will accumulate on the screen.

Code:
- com.haulmont.pwacapsdemo.view.locationwatcher.LocationWatcher
- com.haulmont.pwacapsdemo.component.GeoLocationAccess
- src/main/resources/META-INF/resources/js/geolocation.js

#### Using fileStorageUploadField to take photos
A `<fileStorageUploadField/>` is well integrated with smartphone's camera and gallery.

Menu -> Cases -> Create. Then press Photo -> Upload. The phone should offer you choices to take a photo from camera, or to select it from the gallery.

Code:
- src/main/resources/com/haulmont/pwacapsdemo/view/caraccidentcase/car-accident-case-detail-view.xml

#### Displaying video from camera
Menu -> Cases -> Quick create.

The video will start under "Take camera shot" section. You can start/stop it and switch between front and back cameras.

Code:
- com.haulmont.pwacapsdemo.view.caraccidentcase.CaseQuickCreateView
- com.haulmont.pwacapsdemo.component.DeviceCameraAccess
- src/main/resources/META-INF/resources/js/camera.js

#### Taking camera shot and uploading it to server
Menu -> Cases -> Quick create.

When the video is active, press "Take and upload photo". The screenshot will be taken, uploaded to server and saved to file storage. The file is temporarily stored in-memory as byte array. Custom upload endpoint is registered within the view.

Code:
- com.haulmont.pwacapsdemo.view.caraccidentcase.CaseQuickCreateView
- com.haulmont.pwacapsdemo.component.DeviceCameraAccess
- com.haulmont.pwacapsdemo.component.upload.InMemoryStreamVariable
- src/main/resources/META-INF/resources/js/camera.js
