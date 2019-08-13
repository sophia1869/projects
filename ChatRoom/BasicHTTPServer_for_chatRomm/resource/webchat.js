
let username, room,mess;

var x = document.createElement("INPUT");
x.setAttribute("type", "text");
x.setAttribute("value", "username");
x.id="username";
username=document.getElementById("username");
document.body.appendChild(x);
document.body.appendChild(document.createElement('br'));

var x2 = document.createElement("INPUT");
x2.setAttribute("type", "text");
x2.setAttribute("value", "room");
x2.id="room";
room=document.getElementById("room");
document.body.appendChild(x2);
document.body.appendChild(document.createElement('br'));

var x3 = document.createElement("BUTTON");
var t = document.createTextNode("join");
x3.appendChild(t);
document.body.appendChild(x3);
document.body.appendChild(document.createElement('br'));
x3.addEventListener("click",userinfo);

function userinfo(){
    username=document.getElementById("username").value;
    room=document.getElementById("room").value;

    let mySocket = new WebSocket("ws://"+location.host);

    mySocket.onmessage = function(e){

       console.log(e);
       
        var dataob = JSON.parse(e.data); 
        var innex6 = document.createElement('div');
        innex6.classList.add("historybox");

        innex6.innerHTML = dataob.user + " : " + dataob.message;
        //innex6.appendChild(document.createTextNode(dataob.user + " : " + dataob.message));
        document.getElementById("history").appendChild(innex6);
        
    }
    
    mySocket.onopen = function (){

        document.body.removeChild(x);
        document.body.removeChild(x2);
        document.body.removeChild(x3);

        mySocket.send("join "+ room);

        var x4 = document.createElement("TEXTAREA");
        var t4 = document.createTextNode("input text here");
        x4.appendChild(t4);
        //x4.classList.add("dia");
        x4.cols = "50";
        x4.style.height = "200px";

        document.body.appendChild(x4);
        document.body.appendChild(document.createElement('br'));

        var x5 = document.createElement("BUTTON");
        var t5 = document.createTextNode("submit");
        x5.appendChild(t5);
        document.body.appendChild(x5);
        document.body.appendChild(document.createElement('br'));

        var x6 = document.createElement('div');
        x6.id = 'history';
        x6.classList.add("motherbox");
        document.body.appendChild(x6);

        x5.addEventListener("click",chat);
        function chat(){
        mess = x4.value;
        mySocket.send(username + " " + mess);
        }
        
    }


}
