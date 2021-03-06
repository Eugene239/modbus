package ru.epavlov.desc;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ru.epavlov.entity.Desc_Entity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/**
 * Created by Eugene on 14.03.2017.
 */
public class Description {

    public  static String description ;
    static {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
                description = "C:\\modbusRTUJ\\target\\description";
        } else {
            description = "/usr/java/description/";
        }
    }
    private String s="";
    public ArrayList<String> getFiles(){
        System.out.println(description);
        ArrayList<String> files = new ArrayList<>();
        File f = new File(description);
        if (f.exists()&& f.listFiles()!=null){
            for (File f_ :f.listFiles()){
                files.add(f_.getName());
            }
        }
        return files;
    }
    public boolean createEmptyFile(String name){

        File f = new File(description+name);
        try {
            if (!(new File(description).exists())){
                (new File(description)).createNewFile();
            }
             return f.createNewFile();
            //  FileWriter fw = new FileWriter(f);
            //Files.write(f.toPath(), bytes, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public void add(byte[] bytes, String name){
        File f = new File(description+name);
        try {
           // f.createNewFile();
          //  FileWriter fw = new FileWriter(f);
            Files.write(f.toPath(), bytes, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean delete(String name){
        File f = new File(description+name);
        return f.delete();
    }
    public String getRaw(String name){
        final String[] out = {""};
        File f = new File(description+name);
        System.out.println(f.getPath());
        if (f.exists()){
            try {
                Files.readAllLines(f.toPath()).stream().forEach(s1->{
                    out[0] +=s1;
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out[0];
    }
    public boolean edit(String name,String text){
        File f =new File(description+name);
        try {
            FileWriter fw = new FileWriter(f);
            fw.write(text);
            fw.flush();
            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    public ArrayList<Desc_Entity> getDesc(String name){
        ArrayList<Desc_Entity> list = new ArrayList<>();
        File f = new File(description+name);
      //  System.out.println(f.getPath());
        s="";
        if (f.exists()){
            try {
                JsonParser parser =new JsonParser();
                Files.readAllLines(f.toPath()).stream().forEach(s1->{
                    s+=s1;
                });
                JsonArray array =parser.parse(s).getAsJsonArray();
                s="";
                array.forEach(jsonElement -> {
                    JsonObject jo = (JsonObject) jsonElement;
                    list.add(new Desc_Entity(jo.get("id").getAsInt(),jo.get("desc").getAsString()));
                });
            } catch (Exception e) {
                System.err.println(e.toString());
                return null;

            }
        } else {
            System.err.println("File not found");
        }
        return list;
    }

    public static void main(String[] args) throws IOException {

        JsonArray array =new JsonArray();
        for(int i=0;i<90;i++){
            JsonObject item = new JsonObject();
            item.addProperty("id",i);
            item.addProperty("desc","h"+i);
            array.add(item);
        }
        File f = new File("C:\\modbusRTUJ\\target\\classes\\description\\first_plata.js");
        FileWriter fw = new FileWriter(f);
        fw.write(array.toString());
        fw.flush();
        fw.close();
        System.out.println(array.toString());
    }

}
