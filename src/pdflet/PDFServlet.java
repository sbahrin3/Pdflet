package pdflet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * 
 * @author Shamsul Bahrin
 *
 */

public abstract class PDFServlet implements IServlet {

	protected VelocityEngine engine;
	protected VelocityContext context;	
	protected String templateName;

	@Override
	public void doService(HttpServletRequest request, HttpServletResponse response, ServletConfig servletConfig, ServletContext servletContext) throws IOException, ServletException {
		
		HttpSession session = request.getSession();
		context = (VelocityContext) session.getAttribute("VELOCITY_CONTEXT");
		engine = (VelocityEngine) session.getAttribute("VELOCITY_ENGINE");	
		if (context == null || engine == null) {
			initVelocity(servletConfig, servletContext);
			session.setAttribute("VELOCITY_CONTEXT", context);
			session.setAttribute("VELOCITY_ENGINE", engine);
		}
		
		String contextPath = request.getContextPath();
		String requestURL = request.getRequestURL().toString();
		String appURL = requestURL.substring(0, requestURL.indexOf(contextPath)) + contextPath;
		System.out.println("url = " + appURL);
		
		context.put("contextPath", request.getContextPath());
		context.put("requestURL", request.getRequestURL());
		context.put("appURL", appURL);
		
		try {
			templateName = getTemplateName();
			processTemplate(request);
			PDFWriter pdfw = new PDFWriter(engine, context);
			pdfw.writeContent(templateName, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public void initVelocity(ServletConfig servletConfig, ServletContext servletContext) throws ServletException {
        try { 
        	engine = VelocityEngineHolder.getInstance(servletConfig).getVelocityEngine();
        	context = new VelocityContext();
        	context.put("request_uid", UniqueID.getUID());
        } catch ( Exception e ) {
	        e.printStackTrace();
        }
    }
	
	public abstract String getTemplateName();
	public abstract void processTemplate(HttpServletRequest request);

}
