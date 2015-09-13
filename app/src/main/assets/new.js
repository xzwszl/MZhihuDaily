function showImage(imageUri, fileUri) {

    var objs = document.querySelectorAll('img');

    for (var i=0; i < objs.length; i++) {

          if (objs[i].getAttribute("img-src") == imageUri && objs[i]['src'] != fileUri) {
                  objs[i]['src'] = fileUri;
                 // break;
          }
    }
}

var DEFAULT_IMAGE_URI = "default_pic_content_image_download_dark.png";
function onLoaded(mode, picture) {
    if (picture)
        showDefaultImage('default_pic_content_image_download_dark.png');
    document.body.className += mode ? 'night ' : ' ';
}

function setNigthMode(mode) {
    document.body.className += mode ? 'night ' : ' ';
}

function showDefaultImage(imageUri) {

    var objs = document.querySelectorAll('img');

    for (var i=0; i < objs.length; i++) {
           objs[i].onclick = click(objs[i]);
           window.control.loadImage(objs[i].getAttribute("img-src"));
     }
}

function click(obj) {

    return function() {
        if (obj['src'] == "default_pic_content_image_download_dark.png") {
            window.control.loadImage(obj.getAttribute("img-src"));
        } else {
            window.control.displayImage(obj.getAttribute("img-src"));
        }
    }
}