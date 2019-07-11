package devframework.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import devframework.domain.ClassDescriptor;
import devframework.utils.Utils;

/**
 * Gerencia as classes persistidas com servicos validos.
 */
public class PersistenceService 
{
	// instancia unica da classe
	private static PersistenceService _instance;
	
	/**
	 * Construtor interno.
	 */
	private PersistenceService(){}
	
	/**
	 * Singleton.
	 */
	public static PersistenceService getInstance()
	{
		if ( _instance == null )
			_instance = new PersistenceService();
		
		return _instance;
	}		
    
	/**
	 * Salva o arquivo (classe/jar) no diretorio de upload se o mesmo for valido.
	 */
    public synchronized boolean save(InputStream fileStream, String fileName) throws Exception
    {
    	// salva uma copia do stream (pois o metodo de validacao consome o stream informado, 
    	// nao permitindo utiliza-lo novamente)
    	ByteArrayInputStream streamCopy = new ByteArrayInputStream(IOUtils.toByteArray(fileStream));
    	
		// verifica se o arquivo (classe/jar) eh valido
		Object valid = fileName.endsWith(".jar") 
				? ClassValidationService.getInstance().isValidJar(streamCopy, fileName) 
				: ClassValidationService.getInstance().isValidClass(streamCopy, fileName);

		if ( valid != null )
		{
	    	// arquivo de destino
	    	File file = new File(Utils.getInstance().getUploadDir() + File.separator + fileName);
	    	
	    	// salva o arquivo no diretorio de upload
	    	streamCopy.reset();
	    	FileUtils.copyInputStreamToFile(streamCopy, file);
	    	
	    	return true;
		}
		
		return false;
    }    
    
    /**
     * Lista as classes do diretorio de upload com servicos validos.
     */
    public List<ClassDescriptor> list()
    {
    	return this.listInternal()
    			.stream()
    			.map(c -> new ClassDescriptor(c))
    			.collect(Collectors.toList());
    }
    
    /**
     * Retorna o descritor da classe informada.
     */
    public ClassDescriptor getClass(String classQualifiedName)
    {
    	Class<?> clazz = Utils.getFromCollection(this.listInternal(), 
    			c -> c.getCanonicalName().equals(classQualifiedName));
    	
    	return clazz != null ? new ClassDescriptor(clazz) : null;
    }
    
    /**
     * Retorna as classes do diretorio de upload com servicos validos.
     */
    private List<Class<?>> listInternal()
    {
    	// lista das classes salvas
        List<Class<?>> classesList = new ArrayList<>();

    	// obtem os arquivos de classes e jar do diretorio de upload
    	Collection<File> files = FileUtils.listFiles(new File(Utils.getInstance().getUploadDir()), new String[]{"class", "jar"}, false);
    	
    	// 
    	ClassValidationService validation = ClassValidationService.getInstance();
    	
    	for ( File file : files )
			try
			{
	    		// caminho para o arquivo
	    		String filePath = file.getAbsolutePath();
	    		
	    		// classe
	    		if (FilenameUtils.isExtension(filePath, "class"))
	    		{
	    			Class<?> clazz = validation.isValidClass(filePath); 
	    			if ( clazz != null )
	    				classesList.add(clazz);
	    		}
	    		else //jar 
	    		{
	    			// obtem as classes validas do jare
	    			List<Class<?>> jarClassList = validation.isValidJar(filePath);
	    			if ( jarClassList != null )
	        			for ( Class<?> clazz : jarClassList )
	        				classesList.add(clazz);
	    		}	    			
    		}
			catch ( Exception exc )
			{
				exc.printStackTrace();
			}
        
        return classesList;
    }
}