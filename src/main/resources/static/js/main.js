function add_table(data) {
    return '<td>'
        + data +
    '</td>'
}

maxPages = 0;

function print_table(page, order, direction, size) {
    document.getElementById('main_table').innerHTML = "<tr>\n" +
        "<td>export</td>\n" +
        "<td>id</td>\n" +
        "<td>contractor name</td>\n" +
        "<td>contract date</td>\n" +
        "<td>start date</td>\n" +
        "<td>end date</td>\n" +
        "<td>expected total cost</td>\n" +
        "<td>calculated total cost</td>\n" +
        "<td>recalculate total cost</td>\n" +
        "<td>stages</td>\n" +
        "<td>edit</td>\n" +
        "<td>delete</td>\n" +
        "</tr>";
    fetch("/api/contracts?page=" + page +"&order="+order+"&direction="+direction +"&size="+size)
        .then(response => response.json())
        .then(contracts => {
            contracts.content.forEach(contract => {
                const el = document.createElement('tr');
                a = add_table('<input type="checkbox" name="export" value="' + contract.id + '">');
                a += add_table(contract.id);
                a += add_table(contract.contractor);
                a += add_table(contract.contractDate);
                a += add_table(contract.beginDate);
                a += add_table(contract.endDate);
                a += add_table(contract.expectedTotalCost);
                a += add_table(contract.calculatedTotalCost);
                a += add_table('<a href="../../' + contract.id + '/recalc/">recalculate</a>');
                a += add_table('<a href="../../contract/' + contract.id + '/stages/">stages</a>');
                a += add_table('<a href="../../' + contract.id + '/edit/">edit</a>');
                a += add_table('<a href="../../' + contract.id + '/delete/">delete</a>');
                el.innerHTML = a + '</tr>';
                document.querySelector('#main_table').append(el);
            })
            maxPages = contracts.totalPages - 1;
        })
}

page = 0;
order = "id";
direction = "asc";
size = 10;

function print_table_right(){
    if(page<maxPages) page++;
    print_table(page, order, direction, size);
}

function print_table_left(){
    if(page>0) page--;
    print_table(page, order, direction, size);
}

function sorting(){
    var sort_param;
    sort_param = document.getElementById("sort_param").value;
    sort_param = sort_param.split('_');
    order = sort_param[0];
    direction = sort_param[1];
    print_table(page, order, direction, size);
}

function resize(){
    size = document.getElementById("page_size").value;
    print_table(page, order, direction, size);
}

print_table(page, order, direction, size);

