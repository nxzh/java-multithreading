package fun.code4.jmt.promise;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DataSyncTask implements Runnable {
    private final Map<String, String> taskParameters;

    public DataSyncTask(Map<String, String> taskParameters) {
        this.taskParameters = taskParameters;
    }

    @Override
    public void run() {
        String ftpServer = taskParameters.get("server");
        String ftpUserName = taskParameters.get("userName");
        String password = taskParameters.get("password");

        Future<FTPClientUtil> ftpClientUtilPromise = FTPClientUtil.newInstance(ftpServer, ftpUserName, password);
        generateFilesFromDB();

        FTPClientUtil ftpClientUtil = null;
        try {
            ftpClientUtil = ftpClientUtilPromise.get();
        } catch (InterruptedException e) {
            ;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        uploadFiles(ftpClientUtil);
    }

    private void generateFilesFromDB() {
    }

    private void uploadFiles(FTPClientUtil ftpClientUtil) {
        Set<File> files = retrieveGeneratedFiles();
        for (File file : files) {
            try {
                ftpClientUtil.upload(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Set<File> retrieveGeneratedFiles() {
        Set<File> files = new HashSet<File>();

        return files;
    }
}
