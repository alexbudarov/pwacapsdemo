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
      console.log("Stop camera");
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

// see https://medium.com/@impulsejs/convert-dataurl-to-a-file-in-javascript-1921b8c3f4b
function dataUrlToFile(dataurl, filename) {
  var arr = dataurl.split(","),
    mime = arr[0].match(/:(.*?);/)[1],
    bstr = atob(arr[arr.length - 1]),
    n = bstr.length,
    u8arr = new Uint8Array(n);
  while (n--) {
    u8arr[n] = bstr.charCodeAt(n);
  }
  return new File([u8arr], filename, { type: mime });
}

/* This function is called from view controllers */
function takeAndUploadScreenshot(videoElement, uploadUrl) {
  var canvasElement = document.createElement("canvas");
  canvasElement.width = videoElement.videoWidth;
  canvasElement.height = videoElement.videoHeight;
  canvasElement.getContext("2d").drawImage(videoElement, 0, 0);

  var mimeType = "image/jpeg";
  var dataUrl = canvasElement.toDataURL(mimeType);
  var imageFile = dataUrlToFile(dataUrl, "photo.jpg");

  console.log("Calling fetch POST to: " + uploadUrl);
  fetch(uploadUrl, {
      method: "POST",
      headers: {
        "Content-Type": mimeType,
        "File-Name": imageFile.name
      },
      body: imageFile
  })
  .then(r => {
    console.log("Upload successful")
  })
  .catch(err => {
    console.log("Upload failed " + err)
  })
}
