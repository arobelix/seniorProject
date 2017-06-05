var app = require('express')();
var http = require('http').Server(app);
var fs = require('fs');

app.get('/', function(req, res){
     
    //fs.writeFileSync('seniorProjectData.json', JSON.stringify({ force: req.params.forceValue, time: req.params.timeValue }));
    res.sendFile(__dirname + '/seniorProject.html');
});

app.get('/force/:forceValue/time/:timeValue', function(req, res){
     
    console.log(req.params.forceValue);
    console.log(req.params.timeValue);
    var rawJSON = fs.readFileSync("seniorProjectData.json");
    var parsedJSON = JSON.parse(rawJSON);
    parsedJSON.data.push({force: req.params.forceValue, time: req.params.timeValue});
    fs.writeFileSync('seniorProjectData.json', JSON.stringify(parsedJSON, null, '\t'));
    //fs.writeFileSync('seniorProjectData.json', JSON.stringify({ force: req.params.forceValue, time: req.params.timeValue }));
    res.sendFile(__dirname + '/seniorProject.html');
});

app.get('/waterBottle.jpg', function(req, res){
   res.sendFile(__dirname + '/waterBottle.jpg'); 
});

app.get('/seniorProjectData.json', function(req, res){
    
    res.sendFile(__dirname + '/seniorProjectData.json');
});

http.listen(3000, function(){
    
  console.log('listening on *:3000');
});