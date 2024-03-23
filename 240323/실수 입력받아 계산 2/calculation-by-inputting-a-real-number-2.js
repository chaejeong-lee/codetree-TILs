const fs = require("fs");
let num = Number(fs.readFileSync(0).toString());
console.log((num+1.5).toFixed(2));