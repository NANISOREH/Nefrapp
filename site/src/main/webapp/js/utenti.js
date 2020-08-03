function deleteUser (button){
    $.ajax({
        url : 'deleteUser',
        method : 'delete',
        data : {
            cf : button.value
        },
        fail : function () {
            location.reload()
        }
    })
}

$(document).ajaxStop(function(){
    window.location.reload();
});