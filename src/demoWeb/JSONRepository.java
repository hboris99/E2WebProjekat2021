package demoWeb;

import com.google.gson.stream.JsonReader;
import demoWeb.IRepository;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static demoWeb.Test.gson;


public class JSONRepository<T extends IRepository<I>, I> {
    private String file;
    private Type type;

    public JSONRepository(String file, Type type){
        this.file = file;
        this.type = type;
        Initialize();
        List<T> users = getAll();
    }

    private void Initialize() {

            File f = new File(file);
            if (!f.isFile()) {
                Save(new ArrayList<T>());

            }
            List<T> users = getAll();


    }


    public boolean Create(T entity){
        List<T> allEntities = getAll();
        //Da li postoji entity koji dodajem, i ako postoji da li je logicki obrisan, necemo duplikate entiteta
        Optional<T> exits = allEntities.stream().filter(e -> e.getID().equals(entity.getID())).findFirst();

        if(!exits.isPresent()){
            allEntities.add(entity);
            Save(allEntities);
            return true;
        }else if(exits.get().isDeleted()){
            exits.get().setDeleted(false);
            return Update(exits.get());
        }
        return false;
    }

    public boolean Update(T entity){
        List<T> allEntities = getAll();
        for(int i = 0; i < allEntities.size(); ++i){
            if(allEntities.get(i).getID().equals(entity.getID())){
                allEntities.set(i, entity);
                Save(allEntities);
                return true;
            }

        }
        return false;
    }

    public boolean Delete(I id){
        List<T> allEntities = getAll();
        Optional<T> exists = allEntities.stream().filter(e -> e.getID().equals(id)).findFirst();
        if(!exists.isPresent()){
            return false;
        }
        exists.get().setDeleted(true);
        Save(allEntities);
        return true;
    }

    public Optional<T> get(I id){
        return getAll().stream().filter(e -> e.getID().equals(id)).findFirst();
    }

    public List<T> getAll() {
        List<T> allEntities = null;
        try(FileReader fileReader = new FileReader(file);
            JsonReader jsonReader = new JsonReader(fileReader)) {

            allEntities = gson.fromJson(jsonReader, type);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return allEntities == null ? Collections.emptyList() :allEntities;
    }

    public void LoadAdminUsers(String adminFile){
        List<T> adllAdmins = null;

        try(FileReader fileReader = new FileReader(adminFile);
            JsonReader jsonReader = new JsonReader(fileReader)) {
            jsonReader.setLenient(true);
            adllAdmins = gson.fromJson(jsonReader, type);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Save(adllAdmins);
    }

    public void Save(List<T> entities){
        try(FileWriter fileWriter = new FileWriter(file)){
            gson.toJson(entities, type, fileWriter);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
