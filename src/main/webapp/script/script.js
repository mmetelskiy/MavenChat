function get(id) {
    return document.getElementById(id);
}
function create(element, className) {
    var el = document.createElement(element);
    if(className) {
        el.className = className;
    }
    return el;
}

function ajax(type, url, data, onReadyState4, onBadStatus) {
    var xhr = new XMLHttpRequest();
    xhr.open(type, url, true);
    xhr.send(data);

    xhr.onreadystatechange = function() {
        if(xhr.status == 200) {
            showServerState(true);
            if(xhr.readyState == 4) {
                onReadyState4(xhr.responseText ? JSON.parse(xhr.responseText) : '');
            }
        }
        else {
            if(onBadStatus)
                onBadStatus();
            else
                showServerState(false);
        }
    }
}

var usernameId = '';
var selectedMessages = [];

var textarea = get('textarea');
var sendButton = get('send-message-button');

var messages = get('messages');
var emptyDiv = messages.lastElementChild;
var deleteButton = get('delete-button');

var hint = get('username-hint');

var users = {};
var messageToken;
var messageEditToken;
var messageDeleteToken;
var userToken;
var userChangeToken;

var host = "http://localhost";
var port = ":8080";
var adr = "/Servlet";
var adress = host + port + adr;

var gettingMessages = false;
var firstUpdateRequest = false;