function showImage(imageUri, fileUri) {

    var objs = document.getElementsByTagName("img");

    for (var i=0; i < objs.length; i++) {

          if (objs[i]['src'] == imageUri) {

                  objs[i]['src'] = fileUri;
          }
    }
}

function showDefaultImage(imageUri) {

    var objs = document.getElementsByTagName("img");

    for (var i=0; i < objs.length; i++) {

            objs[i]['src'] = imageUri;
     }
}