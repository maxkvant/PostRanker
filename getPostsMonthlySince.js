var iter = 16; 
var count = 100;
var res = [];
var i = 0;
while (i < iter) {
    var items = API.wall.get({"owner_id" : Args.id, "count" : count,  "offset" : i * count}).items;
    res = res + items;
    var len = items.length;
    if ((len == 0 || items[len - 1].date < Args.date) && i < iter - 2) {
        i = iter - 2;
    }
    i = i + 1;
}
return res;
