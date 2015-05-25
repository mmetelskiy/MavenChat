function MessageNode(message) {
    var msgNode = create('div', 'message');
    msgNode.id = message.messageId;
    var isDeleted = message.isDeleted == 1 ? 1 : 0;
    var isMy = (usernameId == message.userId);
    if(isMy) {
        msgNode.classList.add('my-message');
    }
    if(isDeleted) {
        msgNode.classList.add('deleted-message');
    }
    var imgUrlStr = 'url(' + users[message.userId].userImage + ')';
    var text = isDeleted ? 'Message was deleted...' : message.messageText;
    var date = new Date(+message.messageTime);
    var timeHTML = date.toLocaleTimeString() + "<br>" + date.toLocaleDateString();

    msgNode.innerHTML =
        '<div class="message-img" usernameId="' + message.userId + '" style="background-image:' + imgUrlStr + ';"></div>' +
        '<div class="message-text">' + text + '</div>' +
        '<time>' + timeHTML + '</time>' +
        (isMy && !isDeleted ? '<div class="edit-button"></div>' : '');

    return msgNode;
}

function addLineDividers(text, fromHtml) {
    if(fromHtml) {
        return text.replace( /<br>+/g, '\n');
    }
    return text.replace( /^\s+|\s+$/g, '' ).replace(/\r?\n+/g, '<br>');
}

function sendMessage() {
    function MessageToPost(text) {
        return {
            "userId":usernameId,
            "messageText":text
        };
    }
    function onSuccess() {
        textarea.value = '';
    }

    var text = textarea.value;
    text = addLineDividers(text);

    if(!text) {
        return;
    }

    var body = JSON.stringify(new MessageToPost(text));
    ajax('POST', adress, body, onSuccess);
}