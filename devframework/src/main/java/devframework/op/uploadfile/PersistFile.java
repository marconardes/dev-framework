package devframework.op.uploadfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import devframework.utils.Utils;
import devframework.validation.ClassValidationService;

import devframework.domain.FileClass;

public class PersistFile {
	
	public List<String> save(HttpServletRequest request) throws Exception {
		
		Utils utils = Utils.getInstance(); 

		// Create a factory for disk-based file items
		DiskFileItemFactory diskFactory = new DiskFileItemFactory();

		// Configure a repository (to ensure a secure temp location is used)
		diskFactory.setSizeThreshold(1024 * 1024 * utils.getPropertyAsInt("upload.memory_threshold", 3));
		diskFactory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		// Create a new file upload handler
		ServletFileUpload fileUpload = new ServletFileUpload(diskFactory);
		fileUpload.setFileSizeMax(1024 * 1024 * utils.getPropertyAsInt("upload.max_file_size", 40));
		fileUpload.setSizeMax(1024 * 1024 * utils.getPropertyAsInt("upload.max_request_size", 50));

		// Parse the request
		List<FileItem> files = fileUpload.parseRequest(request);
		if (files.size() > 0) {
			// Process a file upload
			FileItem item = files.get(0);
			
			if (!item.isFormField()) {
				// salva a classe no diretorio temporario para poder ser analisada
				String fileName = new File(item.getName()).getName();
				File tmpFile = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName);
				item.write(tmpFile);
				
				try {
					
					List<String> newClasses = new ArrayList<String>();
					
					String[] typeFileName = fileName.replace(".", " ").split(" ");
					boolean isJar = typeFileName[typeFileName.length-1].toString().equalsIgnoreCase("jar");
					// verifica se eh arquivo .jar, le o arquivo, verificar e salva as classes
					if (isJar) {
						
						List<String> pathsJar = new ArrayList<String>();
						pathsJar = listClassJar(tmpFile);
						
						for (String path : pathsJar) {

							File classFile = new File(path);
							
							// verifica se a classe eh valida!
							if (ClassValidationService.getInstance().isClassValid(path) != null) {
								// salva a classe no diretorio de upload
								this.saveClass(path);
								
								newClasses.add(classFile.getName());
								
							}							
						}
						
					}else {
						// verifica se a classe eh valida!
						if (ClassValidationService.getInstance().isClassValid(System.getProperty("java.io.tmpdir"),
								fileName) != null) {
							// salva a classe no diretorio de upload
							this.saveClass(item, fileName);

							newClasses.add(fileName);
							
						}						
					}
					
					return newClasses;
					
				} 
				catch ( Exception e ) {
					throw new Exception("O arquivo \"" + fileName + "\", não possui uma classe ou método válido!");
				}				
				finally {
					// apaga o arquivo temporario
					tmpFile.delete();
				}
			}
		}

		throw new Exception("Erro ao tentar salvar, nenhuma classe compativel encontrada!");
	}
	
	private List<String> listClassJar(File fileJar) throws IOException{
		
		List<String> pathsJar = new ArrayList<String>();
		JarFile jar = new JarFile(fileJar);
		Enumeration<JarEntry> enumEntries = jar.entries();
		while (enumEntries.hasMoreElements()) {
			JarEntry file = (JarEntry) enumEntries.nextElement();
		    File f = new File(System.getProperty("java.io.tmpdir") + File.separator + file.getName());

		    if (file.isDirectory())
		        continue;
		    else {
		    	String[] typeItem = file.getName().replace(".", " ").split(" ");
	        	boolean isClass = typeItem[typeItem.length-1].toString().equalsIgnoreCase("class");
	        	if (isClass) {
	        		InputStream is = jar.getInputStream(file);
				    FileOutputStream fos = new FileOutputStream(f);
				    while (is.available() > 0) { 
				        fos.write(is.read());
				    }
				    pathsJar.add(f.getPath());
				    
				    fos.close();
				    is.close();
				    
	        	}
		    }						    
		}
		jar.close();
	
		return pathsJar;
	}
	
	public List<FileClass> list() throws FileNotFoundException {
		// diretorio de upload
		File uploadDir = new File(Utils.getInstance().getProperty("upload.dir", System.getProperty("java.io.tmpdir")));
		if(!uploadDir.exists()) {
			//throw new FileNotFoundException("Diretorio nao encontrado");
			uploadDir.mkdir();
		}
		// procura as classes salvas
		List<String> classesList = new ArrayList<>();
		searchFile(".*\\.class", uploadDir, classesList);
				
		List<FileClass> fileClasses = new ArrayList<FileClass>();
		for (String item : classesList) {
			
			FileClass fileClass = new FileClass();
			
			try {
				Class<?> clazz = ClassValidationService.getInstance().isClassValid(item);
				fileClass.setClassName(clazz.getName());
				fileClass.setPackageName(clazz.getPackageName());
				fileClass.setMethods(fileClass.listMethods(item));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			fileClasses.add(fileClass);
		}

		return fileClasses;
	}

	private void saveClass(String filePath) throws Exception {
		// obtem o pacote da classe
		Utils utils = Utils.getInstance();
		String packageName = utils.getClassInfo(filePath).getPackageName();

		// cria os diretorios do pacote da classe no diretorio de upload
		String classDir = utils.getProperty("upload.dir", System.getProperty("java.io.tmpdir") + "upload");
		String dir = packageName.replace(".", File.separator);
		classDir = this.createDir(classDir, dir);

		// salva a classe no diretorio de upload
		File classFile = new File(filePath);
		classFile.renameTo(new File(classDir + File.separator + classFile.getName()));
	}
	
	private void saveClass(FileItem fileItem, String fileName) throws Exception {
		// obtem o pacote da classe
		Utils utils = Utils.getInstance();
		String packageName = utils.getClassInfo(fileItem.getInputStream(), fileName).getPackageName();

		// cria os diretorios do pacote da classe no diretorio de upload
		String classDir = utils.getProperty("upload.dir", System.getProperty("java.io.tmpdir") + "upload");
		String dir = packageName.replace(".", File.separator);
		classDir = this.createDir(classDir, dir);

		// salva a classe no diretorio de upload
		File classFile = new File(classDir + File.separator + fileName);
		fileItem.write(classFile);
	}

	private String createDir(String pathRoot, String dir) {
		String dirPath = pathRoot + File.separator + dir;
		File fileDir = new File(dirPath);

		if (!fileDir.exists())
			fileDir.mkdirs();

		return dirPath;
	}

	private static void search(final String pattern, final File folder, List<String> result) {
		for (final File f : folder.listFiles()) {
			if (f.isDirectory()) {
				search(pattern, f, result);
			}
			if (f.isFile() && f.getName().matches(pattern)) {
				try {
					Class<?> clazz = ClassValidationService.getInstance().isClassValid(f.getPath());
					if (clazz != null) {
						result.add(clazz.getName());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void searchFile(final String pattern, final File folder, List<String> result) {
		for (final File f : folder.listFiles()) {
			if (f.isDirectory()) {
				searchFile(pattern, f, result);
			}
			if (f.isFile() && f.getName().matches(pattern)) {
				try {
					Class<?> clazz = ClassValidationService.getInstance().isClassValid(f.getPath());
					if (clazz != null) {
						result.add(f.getPath());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}