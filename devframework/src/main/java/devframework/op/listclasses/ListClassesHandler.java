package devframework.op.listclasses;

import java.io.FileNotFoundException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

<<<<<<< HEAD
import devframework.domain.ClassDescriptor;
import devframework.services.PersistenceService;
=======
import devframework.domain.FileClass;
import devframework.op.uploadfile.PersistFile;


import devframework.domain.ClassDescriptor;
import devframework.services.PersistenceService;

>>>>>>> 58f2235f0333f63c946c180650e7a7aa794b8e99
import devframework.servlet.IJsonRequestHandler;

/**
 * Trata as requisicoes de listar as classes salvas.
 */
<<<<<<< HEAD
public class ListClassesHandler implements IJsonRequestHandler {
	public JsonObject handleAsync(HttpServletRequest request) throws FileNotFoundException {
		// obtem a lista de classes validas
		List<ClassDescriptor> classesList = PersistenceService.getInstance().list();

		Gson gson = new GsonBuilder().create();

		JsonArray jarray = gson.toJsonTree(classesList).getAsJsonArray();
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("classes", jarray);
=======
public class ListClassesHandler implements IJsonRequestHandler
{
	public JsonObject handleAsync(HttpServletRequest request) throws FileNotFoundException
	{
/*
		<<<<<<< HEAD
		// Fake classes list
		List<FileClass> classesList = new PersistFile().list();
=======
		// obtem a lista de classes validas
		List<ClassDescriptor> classesList = PersistenceService.getInstance().list();
>>>>>>> develop
*/
		List<FileClass> classesList = new PersistFile().list();
		List<ClassDescriptor> classesList2 = PersistenceService.getInstance().list();
		
        Gson gson = new GsonBuilder().create();
        
        JsonArray jarray = gson.toJsonTree(classesList).getAsJsonArray();
        JsonArray jarray2 = gson.toJsonTree(classesList2).getAsJsonArray();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("classes", jarray);
        jsonObject.add("classes2", jarray2);
>>>>>>> 58f2235f0333f63c946c180650e7a7aa794b8e99

		return jsonObject;
	}
}
