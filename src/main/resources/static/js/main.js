function add_table(data) {
    return '<td>'
        + data +
    '</td>'
}

fetch("/api/contracts")
    .then(response => response.json())
    .then(contracts => {
        contracts.forEach(contract => {
            const el = document.createElement('tr');
            a = add_table(contract.id)
            a += add_table(contract.contractor)
            a += add_table(contract.contractDate)
            a += add_table(contract.beginDate)
            a += add_table(contract.endDate)
            a += add_table(contract.totalCost)
            a += add_table('<a href="../../contract/' + contract.id + '/stages/">stages</a>')
            a += add_table('<a href="../../' + contract.id + '/edit/">edit</a>')
            a += add_table('<a href="../../' + contract.id + '/delete/">delete</a>')
            el.innerHTML = a + '</tr>'
            document.querySelector('#main_table').append(el)
        })
    })