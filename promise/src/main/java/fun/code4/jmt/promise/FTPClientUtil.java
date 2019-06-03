package fun.code4.jmt.promise;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class FTPClientUtil {

    private final FTPClient ftp = new FTPClient();

    private final Map<String, Boolean> dirCreateMap = new HashMap<String, Boolean>();

    private FTPClientUtil() {

    }

    public static Future<FTPClientUtil> newInstance(String ftpServer, String ftpUserName, String password) {
        Callable<FTPClientUtil> callable = new Callable<FTPClientUtil>() {
            @Override
            public FTPClientUtil call() throws Exception {
                FTPClientUtil self = new FTPClientUtil();
                self.init(ftpServer, userName, password);
                return self;
            }
        };

        final FutureTask<FTPClientUtil> task = new FutureTask<FTPClientUtil>(callable);
        new Thread(task).start();
        return task;
    }

    private void init(String ftpServer, String userName, String password) {
        FTPClientConfig config = new FTPClientConfig();
        ftp.configure(config);

        int reply;
        ftp.connect(ftpServer);

        System.out.print(ftp.getReplyString());

        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new RuntimeException("FTP server refused connection.");
        }

        boolean isOK = ftp.login(userName, password);
        if (isOK) {
            System.out.println(ftp.getReplyString());
        } else {
            throw new RuntimeException("Failed to login." + ftp.getReplyString());
        }
    }

    public void upload(File file) {

    }
}
