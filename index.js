var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);

app.get('/', function(req, res){
  res.sendFile(__dirname + '/index.html');
});

io.on('connection', function(socket){
    ///////////////////
    socket.on('newUserName', function(msg){
        io.emit('newUser', msg);
    });
    
    socket.on('userTypingCompleteCall', function(msg){
        io.emit('userTypingComplete', msg);
    });
    socket.on('userTypingCall', function(msg){
        io.emit('userTyping', msg);
    });
    
    socket.on('disconnect', function(){
        io.emit('userDisconnect', "User Disconnected");
    });
    ////////////////////
  socket.on('chat message', function(msg){
    io.emit('chat message', msg);
  });
});

http.listen(3000, function(){
  console.log('listening on *:3000');
});

