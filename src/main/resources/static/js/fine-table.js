const toggleFormChange = () => {
    let searchType = document.getElementById("search-type").value;
    const nameForm = document.getElementById("form-name");
    const signatureForm = document.getElementById("form-signature");
    const phoneForm = document.getElementById("phone-form")
    if (searchType === "name") {
        nameForm.style.display = "block";
        signatureForm.style.display = "none";
        phoneForm.style.display = "none";
    } else if (searchType === "signature") {
        nameForm.style.display = "none";
        signatureForm.style.display = "block";
        phoneForm.style.display = "none";
    } else if (searchType === "phone") {
        nameForm.style.display = "none";
        signatureForm.style.display = "none";
        phoneForm.style.display = "block";
    }

}
const clearFilters = () => {

    const searchType = document.getElementById("search-type").value;


    let formId = "";
    if (searchType === "name") {
        formId = "form-name";
    } else if (searchType === "signature") {
        formId = "form-signature";
    } else if (searchType === "phone") {
        formId = "phone-form";
    }


    const form = document.getElementById(formId);


    if (form) {
        const inputs = form.querySelectorAll("input, select");
        inputs.forEach(input => {
            if (input.type === "text" || input.type === "date") {
                input.value = "";
            } else if (input.tagName === "SELECT") {
                input.value = "";
            }
        });
    }
};

window.onload = function () {
    toggleFormChange()

};