const fs = require("fs");
let nums = fs.readFileSync(0).toString();
let numsArr = nums.split(" ");
[numsArr[0], numsArr[1]] = [numsArr[1], numsArr[0]];
console.log(`${Number(numsArr[0])} ${Number(numsArr[1])}`);