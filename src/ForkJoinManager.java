import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ForkJoinManager extends RecursiveAction {

    /* List of directories */
    private final List<String>      m_lDirectories;

    /* Threshold count of image files */
    private final Integer           m_iCount;

    ForkJoinManager(List<String> dirs, int count) {
        this.m_lDirectories = dirs;
        this.m_iCount = count;
    }

    private List[] SplitDirs(List<String> dirs) {
        int size = dirs.size();
        ArrayList<String> first = new ArrayList<String>();
        ArrayList<String> second = new ArrayList<String>();

        for (int i = 0; i < size; i++) {
            if (i < (size + 1)/2)
                first.add(dirs.get(i));
            else
                second.add(dirs.get(i));
        }
        return new List[] {first, second};
    }

    @Override
    protected void compute() {

        if (m_lDirectories.size() < m_iCount) {
            for (String dir: m_lDirectories) {
                ImageProcessor ImgPrc = new ImageProcessor(dir);
                ImgPrc.ProcessImage();
                ImgPrc.WriteImage();
            }
        } else {
            List[] lHalves = SplitDirs(m_lDirectories);
            ForkJoinManager first = new ForkJoinManager(lHalves[0], m_iCount);
            ForkJoinManager second = new ForkJoinManager(lHalves[1], m_iCount);
            ForkJoinTask.invokeAll(first, second);
        }
    }
}
