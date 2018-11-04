const taskInput = document.getElementById("new-title");
const addButton = document.getElementById("add-button");//first button
const incompleteTaskHolder = document.getElementById("incomplete-tasks");//ul of #incomplete-tasks
const completedTasksHolder = document.getElementById("completed-tasks");//completed-tasks

//New task list item
var createNewTaskElement = function (data) {
    let html =
        `<li id="${data.id}">
            <input type="checkbox">
            <label>${data.title}</label>
            <input type="text" class="cont" value="${data.content}" style="display: block"/>
            <input type="text" class="end-date" value="${data.endDate? new Date(data.endDate).toLocaleString():''}" style="visibility:${data.endDate ? "visible" : "hidden"}; display:${data.endDate ? "block" : "none"}; width:225px;" readonly/>
            <input type="hidden" class="end-hidden-date" value="${data.endDate? data.endDate:''}"/>
            <input type="number" value="${data.priority > 0 ? data.priority:''}"/>
            <button class="edit">Edit</button>
            <button class="delete">Delete</button>
        </li>`;

    return html;
}

var createCompletedElement = function (data) {
    let html =
        `<li id="${data.id}">
            <label>${data.title}</label>
            <input type="text" class="cont" value="${data.content}" style="display: block" readonly/>
            <button class="delete">Delete</button>
        </li>`;

    return html;
}

function addToDo() {
    const title = document.getElementById("new-title").value;//Add a new task.
    const content = document.getElementById("new-content").value;
    const endDate = document.getElementById('end-date').value;
    const priority = 0;
    if(!title){
        alert("제목을 입력하세요")
        return;
    }

    if(title.length>20){
        alert("제목은 20자 이하로 입력하세요")
        return;
    }

    if(!content){
        alert("내용을 입력하세요")
        return;
    }

    fetch('/api/todo', {
        method     : 'POST',
        headers    : {'content-type': 'application/json'},
        credentials: 'same-origin',
        body       : JSON.stringify({
            title,
            content,
            endDate,
            priority
        })
    })
        .then(response => {
            if (response.status >= 400 && response.status <= 404) {
                return response.json();
            }
            else if (response.status === 201) {
                return response.json();
            }
        })
        .then(({data}) => {
            const toDo = createNewTaskElement(data);
            incompleteTaskHolder.insertAdjacentHTML('beforeend', toDo);
            bindTaskEvents(data.id, taskIncomplete);
        })
        .catch(error => {
            console.log(error);
        });

}

//Edit an existing task.

var editTask = function () {
    var listItem = this.parentNode;
    var ul = listItem.parentNode;
    var editInput = listItem.querySelector('input[class=cont]');
    var containsClass = listItem.classList.contains("editMode");
    //If class of the parent is .editmode
    if (containsClass) {
        //switch to .editmode
        editInput.readonly = false;
    } else {
        editInput.readonly = true;
    }
    const title = listItem.querySelector('label').innerText;
    const content = editInput.value;
    const priority = listItem.querySelector('input[type=number]').value;

    if(!title){
        alert("제목을 입력하세요")
        return;
    }

    if(title.length>20){
        alert("제목은 20자 이하로 입력하세요")
        return;
    }

    if(!content){
        alert("내용을 입력하세요")
        return;
    }

    if(priority < 1){
        alert("우선순위는 최소 1 이상이어야 합니다");
        listItem.querySelector('input[type=number]').value='';
        return;
    }
    fetch('/api/todo/' + listItem.id, {
        method     : 'PUT',
        headers    : {'content-type': 'application/json'},
        credentials: 'same-origin',
        body       : JSON.stringify({
            title,
            content,
            priority
        })
    })
        .then(response => {
            if (response.status >= 400 && response.status <= 404) {
                return response.json();
            }
            else if (response.status === 200) {
                return response.json();
            }
        })
        .then(response => {
            if(response.errors){
                alert(response.errors[0].message);
                listItem.querySelector('input[type=number]').value='';
            }else{
                alert("수정 성공");
            }
            // console.log("data :",data);
            // const toDo = createNewTaskElement(data);
            // incompleteTaskHolder.insertAdjacentHTML('beforeend', toDo);
            // bindTaskEvents(data.id, taskIncomplete);
        })
        .catch(error => {
            console.log(error);
            alert("error comes!");
        });

    //toggle .editmode on the parent.
    // listItem.classList.toggle("editMode");
}


//Delete task.
var deleteTask = function () {
    var listItem = this.parentNode;
    var ul = listItem.parentNode;

    fetch('/api/todo/' + listItem.id, {
        method     : 'DELETE',
        headers    : {'content-type': 'application/json'},
        credentials: 'same-origin'
    })
        .then(response => {
            if (response.status >= 400 && response.status <= 404) {
                return response.json();
            }
            else if (response.status === 200) {
                return response.json();
            }
        })
        .then(response => {
            ul.removeChild(listItem);
        })
        .catch(error => {
            console.log(error);
            alert("error comes!");
        });
    //Remove the parent list item from the ul.


}


//Mark task completed
var taskCompleted = function () {
    var listItem = this.parentNode;
    completedTasksHolder.appendChild(listItem);
    bindTaskEvents(listItem, taskIncomplete);

}


var taskIncomplete = function () {
//Mark task as incomplete.
    //When the checkbox is unchecked
    //Append the task list item to the #incomplete-tasks.
    var listItem = this.parentNode;
    var ul = listItem.parentNode;
    // incompleteTaskHolder.appendChild(listItem);
    // bindTaskEvents(listItem.id, taskCompleted);
    fetch('/api/todo/complete/' + listItem.id)
        .then(response => {
            if (response.status >= 400 && response.status <= 404) {
                return response.json();
            }
            else if (response.status === 200) {
                return response.json();
            }
        }).then(({data}) => {
        ul.removeChild(document.getElementById(data.id));
        const toDoCompletedTemplate = createCompletedElement(data);
        completedTasksHolder.insertAdjacentHTML('beforeend', toDoCompletedTemplate);
        bindCompletedTaskEvents(data.id, taskCompleted);
    }).catch(error => {
        console.log("error : ", error);
        // location.reload();
    });
}


addButton.addEventListener('click', addToDo);

var bindTaskEvents = function (id, checkBoxEventHandler) {
    const todoElement = document.getElementById(id);
    var checkBox = todoElement.querySelector("input[type=checkbox]");
    var editButton = todoElement.querySelector("button.edit");
    var deleteButton = todoElement.querySelector("button.delete");


    //Bind editTask to edit button.
    editButton.onclick = editTask;
    //Bind deleteTask to delete button.
    deleteButton.onclick = deleteTask;
    //Bind taskCompleted to checkBoxEventHandler.
    checkBox.onchange = checkBoxEventHandler;
}

var bindCompletedTaskEvents = function (id, checkBoxEventHandler) {
    const todoElement = document.getElementById(id);
    var deleteButton = todoElement.querySelector("button.delete");

    deleteButton.onclick = deleteTask;
}

function loadToDos() {
    fetch('/api/todo')
        .then(response => {
            if (response.status >= 400 && response.status <= 404) {
                return response.json();
            }
            else if (response.status === 200) {
                return response.json();
            }
        })
        .then(({data}) => {
            data.forEach(todo => {
                // not completed yet
                if (!todo.isCompleted) {
                    const toDoTemplate = createNewTaskElement(todo);
                    incompleteTaskHolder.insertAdjacentHTML('beforeend', toDoTemplate);
                    bindTaskEvents(todo.id, taskIncomplete);
                } else {
                    const toDoCompletedTemplate = createCompletedElement(todo);
                    completedTasksHolder.insertAdjacentHTML('beforeend', toDoCompletedTemplate);
                    bindCompletedTaskEvents(todo.id, taskCompleted);


                }
            });
        })
        .catch(error => {
            console.log("error : ", error);
            // location.reload();
        });
}

function alarmDate(listNode) {
    listNode.classList.add("alarm");
}

function checkEndDateOver() {
    for (let i = 0; i < incompleteTaskHolder.children.length; i++) {
        console.log(i);
        const endDate = incompleteTaskHolder.children[i].querySelector('input[class=end-hidden-date]').value;
        if (endDate) {
            var diffTime = Math.round((((new Date(endDate) - new Date()) % 86400000) % 3600000) / 60000);
            if (diffTime < 1 && !incompleteTaskHolder.children[i].classList.contains('alarm')) {
                alarmDate(incompleteTaskHolder.children[i]);
            }
        }

    }
}

setInterval(function () {
        checkEndDateOver();
    },
    60000);
document.addEventListener("DOMContentLoaded", loadToDos);