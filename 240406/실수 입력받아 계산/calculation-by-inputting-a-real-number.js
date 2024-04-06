const fs = require("fs");
let input = fs.readFileSync(0).toString();

let inputs = input.split("\n");
let a = Number(inputs[0]);
let b = Number(inputs[1]);
console.log((a+b).toFixed(2));