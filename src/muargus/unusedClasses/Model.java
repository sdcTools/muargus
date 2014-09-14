/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package muargus.unusedClasses;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author ambargus
 */
public class Model {
    
    private Locale locale;
    private ResourceBundle bundle;
    private String language;
    private String country;

    /**
     * 
     */
    public Model() {
        this.locale = new Locale(language, country);
        this.language = "";
        this.country = "";
        this.bundle = ResourceBundle.getBundle("muargus.model.resources.i18n.LanguagePackage", this.locale);
    }
    
    /**
     * 
     * @return 
     */
    public Locale getLocale(){
        this.locale = new Locale(this.language, this.country);
        return locale;
    }
    
    /**
     * 
     * @param s
     * @return 
     */
    public String getTitleName(String s) {
        return bundle.getString(s);
    }
    
    /**
     * 
     * @param s
     * @return 
     */
    public String getLabelName(String s) {
        return bundle.getString(s);
    }
    
    /**
     * 
     * @param s
     * @return 
     */
    public String getButtonName(String s) {
        return bundle.getString(s);
    }

    /**
     * 
     * @return 
     */
    public String getLanguage() {
        return language;
    }

    /**
     * 
     * @param language 
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * 
     * @return 
     */
    public String getCountry() {
        return country;
    }

    /**
     * 
     * @param country 
     */
    public void setCountry(String country) {
        this.country = country;
    }
    
}
