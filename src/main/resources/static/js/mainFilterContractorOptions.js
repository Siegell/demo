fetch("/api/contractors/all")
    .then(response => response.json())
.then(contractors => {
    contractors.forEach(contractor => {
    document.getElementById("filterContractor").innerHTML += ("<option value=\"" + contractor.name + "\">" + contractor.name + "</option>");
})
})