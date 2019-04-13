import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static List<String> GetDirs(String path) {

        List<String> dirs = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {
            dirs = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith(".jpg")).collect(Collectors.toList());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return  dirs;
    }

    public static void main(String[] args) {

        String path = "D:\\Photos_Java";
        Integer count = 4;

        List<String> lDirectories = GetDirs(path);

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        try {
            forkJoinPool.invoke(new ForkJoinManager(lDirectories, count));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
