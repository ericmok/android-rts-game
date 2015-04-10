// Copyright (c) 2015, <your name>. All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.
import 'dart:io' as Io;
import 'package:image/image.dart';
import 'package:path/path.dart' as path;

main() {
  //print('Hello world: ${image_processor.calculate()}!');
  process();
}

void process() {
  var list = (new Io.Directory('../screenshots')).listSync();
  
  for (var fileOrDir in list) {
    
    if (fileOrDir is Io.File) {
      
      if (!path.basename(fileOrDir.path).endsWith(".js")) {
        // This is an image
        
        // TODO: Test if this file exists in screenshots_small
        print("translating: ${fileOrDir.path}");
        translateFile(fileOrDir);
      }
    }
  }
}

void translateFile(Io.File fileToTranslate) {
  Image image = decodeImage(fileToTranslate.readAsBytesSync());
  Image thumbnail = copyResize(image, 400);
  
  var outputFile = "${path.basenameWithoutExtension(fileToTranslate.path)}.jpg";
  var output = new Io.File(outputFile);
  print("out: ${output}");
  
  //output.create().then((file) {
  output.writeAsBytesSync(encodeJpg(thumbnail));
  //output.copySync("../screenshots_small/${path.basename(file.path)}");
  //});
}