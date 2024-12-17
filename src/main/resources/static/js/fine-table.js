const toggleFormChange = () => {
    let searchType = document.getElementById("search-type").value;
    const searchForm = document.querySelector("form");
    const nameDiv = document.getElementById("name-input");
    const phoneDiv = document.getElementById("phone-input");
    const signatureDiv = document.getElementById("signature-input");

    if (searchType === "name") {
        nameDiv.style.display = "block";
        signatureDiv.style.display = "none";
        phoneDiv.style.display = "none";
    } else if (searchType === "signature") {
        nameDiv.style.display = "none";
        signatureDiv.style.display = "block";
        phoneDiv.style.display = "none";
    } else if (searchType === "phone") {
        nameDiv.style.display = "none";
        signatureDiv.style.display = "none";
        phoneDiv.style.display = "block";
    }

}
const clearFilters = () => {

    const form = document.querySelector("form");

    const inputs = form.querySelectorAll("input, select");
    inputs.forEach(input => {
        if (input.type === "text" || input.type === "date") {
            input.value = "";
        } else if (input.tagName === "SELECT") {
            input.value = "";
        }
    });
};

window.onload = function () {
    toggleFormChange()

};