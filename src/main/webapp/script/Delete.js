function deleteSelected() {
    function onSuccess() {
        selectedMessages = [];
        checkDeleteButtonState();
    }
    ajax('DELETE', adress, JSON.stringify(selectedMessages), onSuccess);
}

function makeMessageDeleted(messageId) {
    var msgNode = get(messageId);
    var editButton = msgNode.getElementsByClassName('edit-button')[0];

    if(editButton) {
        msgNode.removeChild(editButton);
    }
    msgNode.classList.add('deleted-message');
    msgNode.classList.remove('selected-message');

    setMessageText(messageId, 'Message was deleted...');
}

function isDeleted(msgNode) {
    if(msgNode.classList.contains('deleted-message')) {
        return true;
    }
    return false;
}