package muargus.unusedClasses.i18n;

public enum Names{
    Microdata("Microdata"),
    Metadata("Metadata"),
    Instructions("Instructions");
    
    private String text;
    
    private Names(String s){
        text = s;
    }
    
    public String getName(){
        return text;
    }
}