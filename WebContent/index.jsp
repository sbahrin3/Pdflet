<html>
<body>
	<h1>Pdflet Demo</h1>
	<p>
		<br/><br/><br/>
		<a target="blank" href="pdflet/pdflet.sample.HelloWorldPDF">Open PDF in new window</a>
		<br/><br/><br/>
		<button type="button" onclick="printPdf('pdflet/pdflet.sample.HelloWorldPDF')">Send PDF to printer</button>
	</p>
</body>
<script>
function printPdf(url) {
	console.log('Loading for printing... Please wait...');
	iframe = document.createElement('iframe');
	document.body.appendChild(iframe);
	iframe.style.display = 'none';
	iframe.onload = function() {
		setTimeout(function() {
			iframe.focus();
		    iframe.contentWindow.print();
		}, 100);
	};
	iframe.src = url;
}
</script>
</html>