const input = document.getElementById("input");
const button = document.getElementById("button");

const sock = new SockJS("http:localhost:8761/api/v1/broadcast");
const stomp = Stomp.over(sock);



button.addEventListener("click", function (msg) {

    })
stomp.connect({}, function (e) {
    console.log("Connected")
    stomp.subscribe("/topic/messages/qrcode/login/123", function (message) {
        console.log("RECEIVED MESSAGE: " + message.body);
        window.location.href = "https://www.youtube.com/watch?v=hIqvbgSQnUo&list=RDhIqvbgSQnUo&start_radio=1"
    })
   let req = new XMLHttpRequest();
   req.open("GET", "http://localhost:8761/api/v1/qrcode/generate?clientId=123");
   req.responseType = "blob";
   req.send();
   req.onload = function () {
       if (req.status === 200) {
           let image = document.createElement("img");
           image.src = window.URL.createObjectURL(req.response);
           let code = document.getElementById("code");
           code.appendChild(image);
       }
   }
})


