document.body.onload = function() {
    showUsernameForm();
};

function startGettingMessages() {
    gettingMessages = true;
    doGet();
}

function stopGettingMessages() {
    gettingMessages = false;
}

function doGet() {
    //alert("DOGET");
    var params = 	'?type=GET_UPDATE' +
        '&messageToken=' + messageToken +
        '&messageEditToken=' + messageEditToken +
        '&messageDeleteToken=' + messageDeleteToken +
        '&userToken=' + userToken +
        '&userChangeToken=' + userChangeToken;

    if(firstUpdateRequest){
        params += '&firstUpdateRequest=true';
        firstUpdateRequest = false;
    }
    else{
        params += '&firstUpdateRequest=false';
    }

    var xhr = new XMLHttpRequest();
    xhr.open('GET', host + port + adr + params, true);
    xhr.send();
    xhr.onreadystatechange = function() {
        if(xhr.status == 200) {
            showServerState(true);

            if(xhr.readyState == 4) {
                var resp = JSON.parse(xhr.responseText);

                if(resp.userToken) {
                    userToken = resp.userToken;
                    JSON.parse(resp.users).forEach(function(user) {
                        users[user.userId] = {
                            "username":user.username,
                            "userImage":user.userImage
                        };

                        if(usernameId == user.userId) {
                            if(user.username) {
                                setUsername(user.username);
                            }
                            if(user.userImage) {
                                get('img').style.backgroundImage = 'url(' + user.userImage + ')';
                            }
                        }
                    });


                }

                if(resp.userChangeToken) {
                    userChangeToken = resp.userChangeToken;
                    JSON.parse(resp.changedUsers).forEach(function(user) {
                        users[user.userId] = {
                            "username":user.username,
                            "userImage":user.userImage
                        };
                        if(usernameId == user.userId) {
                            if(user.username) {
                                setUsername(user.username);
                            }
                            if(user.userImage) {
                                get('img').style.backgroundImage = 'url(' + user.userImage + ')';
                                var divs = document.querySelectorAll('[usernameId="' + user.userId + '"]');
                                divs.forEach = [].forEach;
                                divs.forEach(function(div) {
                                    div.style.backgroundImage = 'url(' + user.userImage + ')';
                                });
                            }
                        }
                    });
                }

                if(resp.messageToken) {
                    messageToken = resp.messageToken;
                    JSON.parse(resp.messages).forEach(function(message) {
                        drawMessage(message);
                    });
                }

                if(resp.messageEditToken) {
                    messageEditToken = resp.messageEditToken;
                    JSON.parse(resp.editedMessages).forEach(function(editing) {
                        setMessageText(editing.messageId, editing.messageText);
                    });
                }

                if(resp.messageDeleteToken) {
                    messageDeleteToken = resp.messageDeleteToken;
                    JSON.parse(resp.deletedMessagesIds).forEach(function(id) {
                        makeMessageDeleted(id);
                    });
                }



                if(resp.userChangeToken) {
                    userChangeToken = resp.userChangeToken;
                    JSON.parse(resp.changedUsers).forEach(function(user) {
                        users[user.userId] = {
                            "username":user.username,
                            "userImage":user.userImage
                        };
                        if(usernameId == user.userId) {
                            if(user.username) {
                                setUsername(user.username);
                            }
                            if(user.userImage) {

                            }
                        }
                    });
                }
                //alert("Upload made!");
                if(gettingMessages){
                    setTimeout(doGet, 0);
                }
            }
        }
        else {
            showServerState(false);
        }
    }
}

function setMessageText(messageId, text) {
    get(messageId).getElementsByClassName('message-text')[0].innerHTML = text;
}

function drawMessage(message) {
    var messageNode = new MessageNode(message);

    messages.insertBefore(messageNode, emptyDiv);
    messageNode.scrollIntoView();
}

function clearMessageContainer() {
    while(messages.firstElementChild != emptyDiv) {
        messages.removeChild(messages.firstElementChild);
    }
}