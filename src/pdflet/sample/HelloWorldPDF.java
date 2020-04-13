package pdflet.sample;

import javax.servlet.http.HttpServletRequest;

import pdflet.PDFServlet;


public class HelloWorldPDF  extends PDFServlet {

	@Override
	public String getTemplateName() {
		return "demo/main.vm";
	}

	@Override
	public void processTemplate(HttpServletRequest request) {
		
		context.put("name", "John Doe");
		
	}

}
