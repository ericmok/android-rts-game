var fs = require('fs');

var files = fs.readdirSync('./');

files.sort(function(a, b) {
  return fs.statSync('./' + a).mtime.getTime() - fs.statSync('./' + b).mtime.getTime();
});

console.log(files);

files.forEach(function(file, index) {
  if (file.endsWith('js')) {
    return; 
  }
  fs.rename(file, 'img' + index + '.png');
});
