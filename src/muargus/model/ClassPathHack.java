/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.model;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 *
 * @author Sean Patrick Floyd
 */
public class ClassPathHack {
    private static final Class<URLClassLoader> URLCLASSLOADER = URLClassLoader.class;
    private static final Class<?>[] parameters = new Class[] {URL.class};

    public static void addFile(final String s) throws IOException{
        addFile(new File(s));
    }

    public static void addFile(final File f) throws IOException{
        addURL(f.toURI().toURL());
    }

    public static void addURL(final URL u) throws IOException
    {
        final URLClassLoader sysloader = getUrlClassLoader();
        //Class sysclass = URLClassLoader.class;

        try {
            final Method method = getAddUrlMethod();
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] {u});
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }

    }
    
    private static Method addUrlMethod;
    private static URLClassLoader urlClassLoader;
    
    private static Method getAddUrlMethod()
        throws NoSuchMethodException{
            if(addUrlMethod == null){
                addUrlMethod =
                    URLCLASSLOADER.getDeclaredMethod("addURL", parameters);
            }
            return addUrlMethod;
        }

    private static URLClassLoader getUrlClassLoader() {
        if(urlClassLoader == null){
            final ClassLoader sysloader = ClassLoader.getSystemClassLoader();
            if(sysloader instanceof URLClassLoader){
                urlClassLoader = (URLClassLoader) sysloader;
            } else{
                throw new IllegalStateException("Not an UrlClassLoader: "+sysloader);
            }
        }
        return urlClassLoader;
    }
}
