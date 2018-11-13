function add_table(data) {
    return '<td>'
        + data +
        '</td>'
}

maxPages = 0;

function print_table(page, order, direction, size) {
    document.getElementById('main_table').innerHTML = "<tr>\n" +
        "<td>id</td>\n" +
        "<td>name</td>\n" +
        "<td>address</td>\n" +
        "<td>phone</td>\n" +
        "<td>edit</td>\n" +
        "<td>delete</td>\n" +
        "</tr>";
    fetch("/api/contractors?page=" + page + "&order=" + order + "&direction=" + direction + "&size=" + size)
        .then(response => response.json())
        .then(contractors => {
            contractors.content.forEach(contractor => {
                const el = document.createElement('tr');
                a = add_table(contractor.id);
                a += add_table(contractor.name);
                a += add_table(contractor.address);
                a += add_table(contractor.phone);
                a += add_table('<a href="../../contractors/' + contractor.id + '/edit/index">edit</a>');
                a += add_table('<a href="../../contractors/' + contractor.id + '/delete/">delete</a>');
                el.innerHTML = a + '</tr>';
                document.querySelector('#main_table').append(el);
            })
            maxPages = contractors.totalPages - 1;
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