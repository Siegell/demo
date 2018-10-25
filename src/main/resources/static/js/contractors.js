function add_table(data) {
    return '<td>'
        + data +
        '</td>'
}

fetch("/api/contractors")
    .then(response => response.json())
.then(contractors => {
    contractors.forEach(contractor => {
    const el = document.createElement('tr');
a = add_table(contractor.id);
a += add_table(contractor.name);
a += add_table(contractor.address);
a += add_table(contractor.phone);
a += add_table('<a href="../../contractor/' + contractor.id + '/edit/">edit</a>');
a += add_table('<a href="../../contractor/' + contractor.id + '/delete/">delete</a>');
el.innerHTML = a + '</tr>';
document.querySelector('#main_table').append(el);
})
})