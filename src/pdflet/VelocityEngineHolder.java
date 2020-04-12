package pdflet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class VelocityEngineHolder {
	
	public static VelocityEngineHolder instance;
	private static VelocityEngine engine;
	
	private VelocityEngineHolder(ServletConfig config) throws Exception  {
		engine = new VelocityEngine();
		Properties p = loadConfiguration(config);
        engine.setApplicationAttribute("javax.servlet.ServletContext", config.getServletContext());
    	engine.init(p);
	}
	
	public synchronized static VelocityEngineHolder getInstance(ServletConfig config) throws Exception {
		if ( instance == null ) instance = new VelocityEngineHolder(config);
		return instance;
	}
	
	public VelocityEngine getVelocityEngine() {
		return engine;
	}
	
    private Properties loadConfiguration(ServletConfig config ) throws IOException, FileNotFoundException {
   		String path = config.getServletContext().getRealPath("/");
        Properties p = new Properties();
        p.setProperty( Velocity.FILE_RESOURCE_LOADER_PATH, path );
        p.setProperty( Velocity.FILE_RESOURCE_LOADER_CACHE, "true" );
        return p;	    	
    }
}
