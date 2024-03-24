const fs = require("fs");
let nums = fs.readFileSync(0).toString();
let numsArr = nums.split(" ");
console.log(Number(numsArr[0])+Number(numsArr[1]));