const fs = require("fs");
let num = fs.readFileSync(0).toString();
num = Number(num) * 30.48
console.log(num.toFixed(1));