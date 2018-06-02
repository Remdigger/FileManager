import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {
    /**
     * принимает путь к папке, возвращает количество файлов в папке и всех подпапках по пути
     *
     * @param path path to the folder
     * @return number of files, -1 no such folder exists
     */
    public static int calculateFiles(String path) {
        if (path == null && Files.notExists(Paths.get(path))) {
            return -1;
        }
        int accumulator = 0;
        File tmpFile = new File(path);
        File[] tmpListOfFile = tmpFile.listFiles();
        for (int i = 0; i < tmpListOfFile.length; i++) {
            if (tmpListOfFile[i].isDirectory()) {
                accumulator += calculateFiles(tmpListOfFile[i].getPath());
            } else {
                accumulator++;
            }

        }
        return accumulator;

    }


    /**
     * принимает путь к папке, возвращает количество папок в папке и всех подпапках по пути
     *
     * @param path path to the folder
     * @return number of the folders in the folder
     */
    public static int calculateDirs(String path) {
        if (path == null && Files.notExists(Paths.get(path))) {
            return -1;
        }
        int accumulator = 0;
        File tmpFile = new File(path);
        File[] tmpListOfFile = tmpFile.listFiles();
        for (int i = 0; i < tmpListOfFile.length; i++) {
            if (tmpListOfFile[i].isDirectory()) {
                accumulator += calculateDirs(tmpListOfFile[i].getPath());
                accumulator++;
            }

        }
        return accumulator;

    }

    /**
     * метод по копированию папок и файлов.
     *
     * @param from путь к файлу или папке
     * @param to   путь к папке куда будет производиться копирование.
     * @return is successful ?
     */
    public static boolean copy(String from, String to) throws IOException {
        if ((from == null || to == null) && (Files.notExists(Paths.get(from)))) {
            return false;
        }
        MyFiles.copy(Paths.get(from), Paths.get(to));
        return true;
    }

    /**
     * метод по перемещению папок и файлов. Параметр from - , параметр to - .
     *
     * @param from путь к файлу или папке
     * @param to   путь к папке куда будет производиться перемещение
     * @return
     */
    public static boolean move(String from, String to) throws IOException {
        if ((from == null || to == null) && (Files.notExists(Paths.get(from)))) {
            return false;
        }
        copy(from, to);
        delete(Paths.get(from));
//        Paths.get(to).toFile().renameTo(fileName.toFile());
//        Files.move(Paths.get(from), Paths.get(to).resolve(Paths.get(from).getFileName()));
        return true;
    }

    public static boolean delete(Path path) throws IOException {


        if (path.toFile().isDirectory()) {

            Files.list(path).forEach(
                    s -> {
                        try {
                            if (s.toFile().isDirectory()) {
                                FileManager.delete(s);
                            } else {
                                Files.delete(s);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

            );

            Files.delete(path);
        } else {
            Files.delete(path);
        }
        return true;
    }

    private static class MyFiles {
        public static void copy(Path from, Path to) throws IOException {

            try (
                    BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(from));
                    BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(to));
            ) {
                byte[] buffer = new byte[1024];
                int numOfbytes = 0;
                int offset = 0;
                while ((numOfbytes = bis.available()) > 0) {
                    offset = numOfbytes > buffer.length ? buffer.length : numOfbytes;
                    bis.read(buffer, 0, offset);
                    bos.write(buffer, 0, offset);
                }
            }


        }
    }

    ;

}