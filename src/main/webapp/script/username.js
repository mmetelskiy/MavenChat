function setUsername(username) {
    get('userdiv').innerText = username;
    get('userdiv').style.display = '';
}

function Background() {
    return create('div', 'modal-background');
}

function UsernameForm() {
    var container = create('div', 'entering');
    var p = create('p');
    p.innerText = 'Username:';
    var input = create('input');
    input.type = 'text';
    input.placeholder = 'Enter username';
    var button = create('button');
    button.innerText = 'Enter';
    container.appendChild(p);
    container.appendChild(input);
    container.appendChild(button);
    return {
        "container":container,
        "input":input,
        "button":button
    };
}

function showUsernameForm(isChanging) {
    get('userdiv').style.display = 'none';

    var background = new Background();
    var usernameForm = new UsernameForm();

    background.appendChild(usernameForm.container);
    document.body.appendChild(background);

    usernameForm.input.focus();

    if(isChanging) {
        usernameForm.button.onclick = function() {
            var text = usernameForm.input.value;
            if(!text) {
                return;
            }

            changeUsername(text);
        }
    }
    else {
        usernameForm.button.onclick = function() {
            var text = usernameForm.input.value;
            if(!text) {
                return;
            }

            enter(text);
        }
    }

    function enter(username) {
        function setStartVars(resp) {
            usernameId = resp.currentUserId;
            messageToken = resp.messageToken;
            messageEditToken = resp.messageEditToken;
            messageDeleteToken = resp.messageDeleteToken;
            userToken = resp.userToken;
            userChangeToken = resp.userChangeToken;
            document.body.removeChild(background);
            textarea.focus();

            firstUpdateRequest = true;
            startGettingMessages();
        }
        var params = '?type=BASE_REQUEST&username=' + username;
        ajax('GET', adress + params, null, setStartVars);
    }


    function changeUsername(username) {
        function hideBackground() {
            get('userdiv').style.display = '';
            document.body.removeChild(background);
            textarea.focus();
        }
        var requestBody = {};
        requestBody.type = "CHANGE_USERNAME";
        requestBody.user = {};
        requestBody.user.userId = usernameId;
        requestBody.user.username = username;

        ajax('PUT', adress, JSON.stringify(requestBody), hideBackground);
    }
}