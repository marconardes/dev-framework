package devframework.op.uploadfile;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonObject;

import devframework.servlet.IJsonRequestHandler;
import devframework.utils.Utils;

/**
 * Trata as requisicoes de upload de novas classes.
 */
public class UploadFileHandler implements IJsonRequestHandler
{
	public JsonObject handleAsync(HttpServletRequest request)
	{
		JsonObject jsonObject = new JsonObject();

		try
		{
			List<String> filesClass = new PersistFile().save(request);
			Utils utils = Utils.getInstance();			
			String fileName = utils.convertListToString(filesClass, ", ");
			
			jsonObject.addProperty("success", true);
			jsonObject.addProperty("msg", (filesClass.size() > 1 ? "Classes Java \""+ fileName +"\" cadastradas com sucesso." : "Classe Java \""+ fileName +"\" cadastrada com sucesso."));
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			jsonObject.addProperty("success", false);
			jsonObject.addProperty("msg", e.getMessage());
		}
		
		return jsonObject;	
	}
}
