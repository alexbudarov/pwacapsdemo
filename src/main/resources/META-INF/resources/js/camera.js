// code taken from https://github.com/doug2k1/javascript-camera

// todo global variable - bad?
let cameraVideoStream;

const constraints = {
  video: {
    width: {
      min: 640,
      ideal: 1280,
      max: 1280,
    },
    height: {
      min: 480,
      ideal: 720,
      max: 720,
    },
  },
};

/* This function is called from view controllers */
function isCameraSupported() {
  var response = "mediaDevices" in navigator && "getUserMedia" in navigator.mediaDevices;
  return response;
}

function stopCamera() {
  if (cameraVideoStream) {
      cameraVideoStream.getTracks().forEach((track) => {
        track.stop();
      });
      cameraVideoStream = null;
    }
}

/* This function is called from view controllers */
function startCamera(videoElement, useFrontCamera) {
  stopCamera();

  constraints.video.facingMode = useFrontCamera ? "user" : "environment";

  navigator.mediaDevices.getUserMedia(constraints)
      .then(stream => {
        cameraVideoStream = stream;
        videoElement.srcObject = cameraVideoStream;
      })
      .catch(err => {
        alert("Could not access the camera");
      });
}
