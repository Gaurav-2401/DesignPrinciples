package udemy.design.principles;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;
//SRP implementation
class Journal{
    private final List<String> entries = new ArrayList<String>();
    private static int count=0;

    public void addEntries(String text){
        entries.add("" + (++count) +": "+ text);
    }
    public void removeEntry(int index){
        entries.remove(index);
    }
    @Override
    public String toString(){
        return String.join(System.lineSeparator(), entries);
    }
}
//creating class udemy.design.principles.Persistence separately and not adding the functions like save, load in udemy.design.principles.Journal class to separate persistence
//related concerns to separate class
class Persistence{
    public void saveToFile(Journal journal, String fileName, boolean overwrite) throws FileNotFoundException {
        if(overwrite || new File(fileName).exists()){
            try(PrintStream out = new PrintStream(fileName)){
                out.println(journal.toString());
            }
        }
    }
//    public udemy.design.principles.Journal load(String fileName){}
//    public udemy.design.principles.Journal load(URL url){}
}

public class JournalDemoSRP {
    public static void main(String[] args) throws Exception{
        Journal j = new Journal();
        j.addEntries("I am happy");
        j.addEntries("I ate pizza");
        System.out.println(j);

        Persistence p = new Persistence();
        String fileName = "/Users/gauravpa/Documents/intellij_workspace/JavaDesignPatterns/res/journal.txt";
        p.saveToFile(j, fileName, true);
    }
}
