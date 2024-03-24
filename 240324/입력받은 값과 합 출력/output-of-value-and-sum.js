const fs = require("fs");
let arr = fs.readFileSync(0).toString();
let nums = arr.split(" ");
let a = Number(nums[0]);
let b = Number(nums[1]);

console.log(a, b, a+b)