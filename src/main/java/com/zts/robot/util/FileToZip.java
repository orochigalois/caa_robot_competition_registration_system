package com.zts.robot.util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;  
/** 
 * 将文件夹下面的文件 
 * 打包成zip压缩文件 
 *  
 * @author admin 
 * 
 */  
public final class FileToZip {  
  
    private FileToZip(){}
      
    /** 
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下 
     * @param sourceFilePath :待压缩的文件路径 
     * @param zipFilePath :压缩后存放路径 
     * @param fileName :压缩后文件的名称 
     * @return 
     */  
    public static boolean fileToZip(String sourceFilePath,String zipFilePath,String fileName){  
        boolean flag = false;  
        File sourceFile = new File(sourceFilePath);  
        FileInputStream fis = null;  
        BufferedInputStream bis = null;  
        FileOutputStream fos = null;  
        ZipOutputStream zos = null;  
          
        if(sourceFile.exists() == false){  
            System.out.println("待压缩的文件目录："+sourceFilePath+"不存在.");  
        }else{  
            try {  
                File zipFile = new File(zipFilePath + "/" + fileName +".zip");  
                if(zipFile.exists()){  
                    System.out.println(zipFilePath + "目录下存在名字为:" + fileName +".zip" +"打包文件.");  
                }else{  
                    File[] sourceFiles = sourceFile.listFiles();  
                    if(null == sourceFiles || sourceFiles.length<1){  
                        System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");  
                    }else{  
                        fos = new FileOutputStream(zipFile);  
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));  
                        byte[] bufs = new byte[1024*10];  
                        for(int i=0;i<sourceFiles.length;i++){  
                            //创建ZIP实体，并添加进压缩包  
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());  
                            zos.putNextEntry(zipEntry);  
                            //读取待压缩的文件并写进压缩包里  
                            fis = new FileInputStream(sourceFiles[i]);  
                            bis = new BufferedInputStream(fis, 1024*10);  
                            int read = 0;  
                            while((read=bis.read(bufs, 0, 1024*10)) != -1){  
                                zos.write(bufs,0,read);  
                            }  
                        }  
                        flag = true;  
                    }  
                }  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
                throw new RuntimeException(e);  
            } catch (IOException e) {  
                e.printStackTrace();  
                throw new RuntimeException(e);  
            } finally{  
                //关闭流  
                try {  
                    if(null != bis) bis.close();  
                    if(null != zos) zos.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                    throw new RuntimeException(e);  
                }  
            }  
        }  
        return flag;  
    }  
    
    /** 
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下 
     * @param sourceFilePath :待压缩的文件路径 
     * @param zipFilePath :压缩后存放路径 
     * @param fileName :压缩后文件的名称 
     * @return 
     */  
    public static boolean fileToZip_new(String sourceFilePath,String zipFilePath,String fileName){  
        boolean flag = false;  
        File sourceFile = new File(sourceFilePath);  
        FileInputStream fis = null;  
        BufferedInputStream bis = null;  
        FileOutputStream fos = null;  
        ZipOutputStream zos = null;  
          
        if(sourceFile.exists() == false){  
            System.out.println("待压缩的文件目录："+sourceFilePath+"不存在.");  
        }else{  
            try {  
                //File zipFile = new File(zipFilePath + "/" + fileName +".zip");  
            	//File zipFile = new File(zipFilePath + "/");
//                if(!zipFile.exists()){  
//                    System.out.println(zipFilePath + "目录下存在名字为:" + fileName +"文件.");  
//                }else{  
                    File[] sourceFiles = sourceFile.listFiles();  
                    if(null == sourceFiles || sourceFiles.length<1){  
                        System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");  
                    }else{  
                        
                        for(int i=0;i<sourceFiles.length;i++){ 
                        	ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());  
                        	File zipFile_snd = new File(zipFilePath + "/" + zipEntry + "ZIP.zip");
                        	File[] sourceFiles_snd = sourceFiles[i].listFiles();  
                        	fos = new FileOutputStream(zipFile_snd);  
                            zos = new ZipOutputStream(new BufferedOutputStream(fos));  
                            byte[] bufs = new byte[1024*10];  
                        	for(int j=0; j<sourceFiles_snd.length;j++){
                        		//创建ZIP实体，并添加进压缩包  
                                ZipEntry zipEntry_snd = new ZipEntry(sourceFiles_snd[j].getName());  
                                zos.putNextEntry(zipEntry_snd);  
                                //读取待压缩的文件并写进压缩包里  
                                fis = new FileInputStream(sourceFiles_snd[j]);  
                                bis = new BufferedInputStream(fis, 1024*10);  
                                int read = 0;  
                                while((read=bis.read(bufs, 0, 1024*10)) != -1){  
                                    zos.write(bufs,0,read);  
                                }  
                        	}
                        }  
                        flag = true;  
                    }  
//                }  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
                throw new RuntimeException(e);  
            } catch (IOException e) {  
                e.printStackTrace();  
                throw new RuntimeException(e);  
            } finally{  
                //关闭流  
                try {  
                    if(null != bis) bis.close();  
                    if(null != zos) zos.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                    throw new RuntimeException(e);  
                }  
            }  
        }  
        return flag;  
    }  
    
    public static void getAllFiles(File dir, List<File> fileList) {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				fileList.add(file);
				if (file.isDirectory()) {
					System.out.println("directory:" + file.getCanonicalPath());
					getAllFiles(file, fileList);
				} else {
					System.out.println("     file:" + file.getCanonicalPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeZipFile(File directoryToZip, List<File> fileList, String outputZip) {

		try {
			FileOutputStream fos = new FileOutputStream(outputZip);
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (File file : fileList) {
				if (!file.isDirectory()) { // we only zip files, not directories
					addToZip(directoryToZip, file, zos);
				}
			}

			zos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws FileNotFoundException,
			IOException {

		FileInputStream fis = new FileInputStream(file);

		// we want the zipEntry's path to be a relative path that is relative
		// to the directory being zipped, so chop off the rest of the path
		String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,
				file.getCanonicalPath().length());
		System.out.println("Writing '" + zipFilePath + "' to zip file");
		ZipEntry zipEntry = new ZipEntry(zipFilePath);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}
	
	
	 public static void delete(File file)
		    	throws IOException{

		    	if(file.isDirectory()){

		    		//directory is empty, then delete it
		    		if(file.list().length==0){

		    		   file.delete();
		    		   System.out.println("Directory is deleted : "
		                                                 + file.getAbsolutePath());

		    		}else{

		    		   //list all the directory contents
		        	   String files[] = file.list();

		        	   for (String temp : files) {
		        	      //construct the file structure
		        	      File fileDelete = new File(file, temp);

		        	      //recursive delete
		        	     delete(fileDelete);
		        	   }

		        	   //check the directory again, if empty then delete it
		        	   if(file.list().length==0){
		           	     file.delete();
		        	     System.out.println("Directory is deleted : "
		                                                  + file.getAbsolutePath());
		        	   }
		    		}

		    	}else{
		    		//if file, then delete it
		    		file.delete();
		    		System.out.println("File is deleted : " + file.getAbsolutePath());
		    	}
		    }
	
      
    public static void main(String[] args){  
        String sourceFilePath = "~/ROOT/staticrobot/pdfImage";  
        String zipFilePath = "D:/ROOT";  
        String fileName = "12700153file";  
        boolean flag = FileToZip.fileToZip(sourceFilePath, zipFilePath, fileName);  
        if(flag){  
            System.out.println("文件打包成功!");  
        }else{  
            System.out.println("文件打包失败!");  
        }  
    }  
      
}  