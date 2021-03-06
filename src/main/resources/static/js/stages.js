function add_table(data) {
    return '<td>'
        + data +
        '</td>'
}

fetch("/api/stages/{{contractID}}")
    .then(response => response.json())
    .then(stages => {
        stages.forEach(stage => {
            const el = document.createElement('tr');
            a = add_table(stage.id)
            a += add_table(stage.name)
            a += add_table(stage.beginDate)
            a += add_table(stage.endDate)
            a += add_table(stage.cost)
            a += add_table(stage.paymentDate)
            a += add_table('<a href="../../contract/' + contract.id + '/stages/">stages</a>')
            a += add_table('<a href="../../contract/' + contract.id + '/edit/">edit</a>')
            a += add_table('<a href="../../contract/' + contract.id + '/delete/">delete</a>')
            el.innerHTML = a + '</tr>'
            document.querySelector('#main_table').append(el)
        })
    })