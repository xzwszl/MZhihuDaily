function showImage(imageUri, fileUri) {

    var objs = document.querySelectorAll('img'));

    for (var i=0; i < objs.length; i++) {

          if (objs[i].dataset.url == imageUri) {

                  console.log(objs[i].dataset.url);
                  objs[i]['src'] = fileUri;
                  break;
          }
    }
}

function onLoad() {
    showDefaultImage('default_pic_content_image_download_dark.png');
}

function showDefaultImage(imageUri) {

    var objs = document.document.querySelectorAll('img');

    for (var i=0; i < objs.length; i++) {
            objs[i].dataset.url = objs[i]['src'];i
            objs[i]['src'] = imageUri;
     }
}