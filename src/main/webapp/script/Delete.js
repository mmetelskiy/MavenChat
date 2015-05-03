function deleteSelected() {
	var xhr = new XMLHttpRequest();
	xhr.open('DELETE', host + port + adr, true);
	xhr.send(JSON.stringify(selectedMessages));
	xhr.onreadystatechange = function() {
		if(xhr.status == 200) {
			showServerState(true);

			if(xhr.readyState == 4) {
				selectedMessages = [];
			}
		}
		else {
			showServerState(false);
		}
	}
}

function makeMessageDeleted(messageId) {
	var msgNode = get(messageId);
	var editButton = msgNode.getElementsByClassName('edit-button')[0];

	msgNode.removeChild(editButton);
	msgNode.classList.add('deleted-message');

	setMessageText(messageId, 'Message was deleted...');
}

function isDeleted(msgNode) {
	if(msgNode.classList.contains('deleted-message')) {
		return true;
	}
	return false;
}