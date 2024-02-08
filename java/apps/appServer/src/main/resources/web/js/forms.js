function errorMessageElement(input) {
    var parentNode = input.type=='radio' ? input.parentNode.parentNode : input.parentNode;
    var result = parentNode.querySelector('small');
    if(!result) {
        result = document.createElement('small');
        parentNode.appendChild(result);
    }
    return result;
}

function prepareForms() {
    var forms = document.querySelectorAll('form');
    console.log("Setting up forms", Array.prototype.slice.call(forms));
    forms.forEach((form) => {
        form.novalidate = true;
        Array.from(form.elements).forEach((input) => {
            var errorMessage = errorMessageElement(input);
            input.addEventListener('input', event => checkInputValidity(input, errorMessage));
            input.addEventListener('focusout', event => checkInputValidity(input, errorMessage));
        });
        console.log("Adding validation event listener to ", form);
        form.addEventListener('submit', (event) => {
            var valid = true;
            Array.from(form.elements).forEach((input) => {
                if(!checkInputValidity(input, errorMessageElement(input))){
                    valid = false;
                }
            });
            console.log("Validating form before submit ", form, ": ", valid?"No errors fond":"Errors found");
            if(!valid) {
                event.preventDefault();
                event.stopImmediatePropagation();
            }
        });
    });
}



function checkInputValidity(input, errorMessage) {
    if(input.type == "fieldset") return true;
    input.classList.add('validated');
    if (input.validity.valid) {
        errorMessage.textContent = '';
        errorMessage.classList.remove('error');
    } else {
        errorMessage.classList.add('error');
        errorMessage.textContent = input.validationMessage;
    }
    return input.validity.valid;
}
