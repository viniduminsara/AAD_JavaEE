const form = $('#myForm')[0];
const reset = $('#reset-btn');
const url = 'http://localhost:8080/mini_project/item';

$('#submit-btn').on('click', (event) => {
    if(form.checkValidity()){
        const itemData = {
            id: $('#id').val(),
            name: $('#name').val(),
            price: $('#price').val(),
            qty: $('#qty').val()
        };
        //create JSON
        const json = JSON.stringify(itemData);
        
        const sendAjax = (json) => {
            //Ajax
            const http = new XMLHttpRequest();
            http.onreadystatechange = function(){
        
                if(http.readyState == 4 && http.status == 201){
                    alert('Item Successfully Created');
                    reset.click();
                }
                
            }
        
            http.open('POST', url, true);
            http.setRequestHeader('Content-Type', 'application/json');
            http.send(json);
        }
        
        //send data to the backend
        sendAjax(json);
    }else{
        event.preventDefault();
        event.stopPropagation();
    }

    form.classList.add('was-validated')
});

$('#update-btn').on('click', (event) => {
    if(form.checkValidity()){
        const itemData = {
            id: $('#id').val(),
            name: $('#name').val(),
            price: $('#price').val(),
            qty: $('#qty').val()
        };
        
        $.ajax({
            type: 'PUT',
            url: url,
            contentType: 'application/json',
            data: JSON.stringify(itemData),
            success: function(res){
                alert('Item Successfully Updated');
                reset.click();
            },
            error: function(error){
                alert(`${error.status} : ${error.statusText}`);
            }
        });

    }else{
        event.preventDefault();
        event.stopPropagation();
    }

    form.classList.add('was-validated')
});

$('#delete-btn').on('click', (event) => {
    const id = $('#id');

    if(id[0].checkValidity()){
        id.removeClass('is-invalid').addClass('is-valid');

        const itemData = {
            id: $('#id').val()
        };
        
        $.ajax({
            type: 'DELETE',
            url: url,
            contentType: 'application/json',
            data: JSON.stringify(itemData),
            success: function(res){
                alert('Item Successfully Deleted');
                reset.click();
            },
            error: function(error){
                alert(`${error.status} : ${error.statusText}`);
            }
        });
    }else{
        event.preventDefault();
        event.stopPropagation();
        id.removeClass('is-valid').addClass('is-invalid');
    }

    id.addClass('was-validated');
});

$('#id').on('input', function () {
    if (this.checkValidity()) {
        $(this).removeClass('is-invalid').addClass('is-valid');
    } else {
        $(this).removeClass('is-valid').addClass('is-invalid');
    }
});