package pdflet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */

public class PDFWriter {
	
	
	private VelocityEngine engine;
	private VelocityContext context;	


	public PDFWriter(VelocityEngine engine, VelocityContext context) {
		this.engine = engine;
		this.context = context;
	}
	
	
	public String createContent(String templateName) throws Exception {
		StringBuffer sb = new StringBuffer("");
		try {
			Template template = engine.getTemplate(templateName); 
			StringWriter writer = new StringWriter();
			template.merge(context, writer);
			writer.close();
			sb = writer.getBuffer();
		} catch ( Exception ex ) {
		    ex.printStackTrace();
			throw ex;			
		}
		return sb.toString();
	}		
	

	
	public void writeContent(String templateName, HttpServletResponse response) throws Exception {

		// Create content and set up streams
		String content = createContent(templateName);
		InputStream contentStream = new ByteArrayInputStream(content.getBytes());
		BufferedInputStream in = new BufferedInputStream(contentStream);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		BufferedOutputStream out = new BufferedOutputStream(byteArrayOutputStream);

		// Process the content with Tidy
		Tidy tidy = new Tidy();
		tidy.parse(in, out);

		// Close input and output streams
		in.close();
		out.flush();
		out.close();

		// Now use the processed HTML from byteArrayOutputStream as input for the PDF generation
		ByteArrayInputStream tidyOutputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

		// Set up PDF renderer (ITextRenderer) to work with the processed HTML
		ServletOutputStream os = response.getOutputStream();
		ITextRenderer renderer = new ITextRenderer();

		// Load the processed HTML directly from the stream
		renderer.setDocumentFromString(byteArrayOutputStream.toString("UTF-8"));

		// Layout and generate the PDF
		renderer.layout();
		renderer.createPDF(os);

		// Flush and close the response output stream
		os.flush();
		os.close();

		
	}



}
