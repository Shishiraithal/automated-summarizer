<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body>

<head>
<style>
p1 {
    position: absolute;
    left: 5%;
    top: 5%;
}

p2 {
    position: absolute;
    right: 10%;
    top: 10%;
}

p3 {
    position: absolute;
    left: 10%;
    bottom: 10%;
}
p4 {
    position: absolute;
    right: 10%;
    bottom: 20%;
}
p5{
    position: absolute;
    top: 48%;
    bottom: 40%;
	right: 40%;
	left: 43%;
}
</style>
</head>

<canvas id="myCanvas" width="1300" height="600" style="border:1px solid #d3d3d3;">
Your browser does not support the HTML5 canvas tag.</canvas>

<script>

var c = document.getElementById("myCanvas");
var ctx = c.getContext("2d");
//Second quadrant
ctx.beginPath();
ctx.moveTo(550,290);
ctx.lineTo(300,200);
ctx.stroke();

//Third quadrant
ctx.beginPath();
ctx.moveTo(550,330);
ctx.lineTo(310,440);
ctx.stroke();

//First quadrant
ctx.beginPath();
ctx.moveTo(685,290);
ctx.lineTo(950,200);
ctx.stroke();

//Fourth quadrant
ctx.beginPath();
ctx.moveTo(680,330);
ctx.lineTo(955,460);
ctx.stroke();

</script>

<p1>${sub1}</p1>
<p2>${sub2}</p2>
<p3>${sub3}</p3>
<p4>${sub4}</p4>
<p5>${varname}</p5>

</body>
</html>