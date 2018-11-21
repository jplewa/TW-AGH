import java.util.ArrayList;
import java.util.concurrent.Callable;

public class MandelbrotProcessor implements Callable<ArrayList<Integer>> {

    private final long START_POINT;
    private final long END_POINT;
    private final long HEIGHT;
    private final double ZOOM;
    private final int MAX_ITER;

    MandelbrotProcessor(long start_point, long end_point, long height, double zoom, int max_iter) {
        START_POINT = start_point;
        END_POINT = end_point;
        HEIGHT = height;
        ZOOM = zoom;
        MAX_ITER = max_iter;
    }

    private int getPixel(int x, int y){
        double zy;
        double zx = zy = 0;
        double cX = (x - 400) / ZOOM;
        double cY = (y - 300) / ZOOM;
        int iter = MAX_ITER;
        while (zx * zx + zy * zy < 4 && iter > 0) {
            double tmp = zx * zx - zy * zy + cX;
            zy = 2.0 * zx * zy + cY;
            zx = tmp;
            iter--;
        }
        return iter;
    }

    //@Override
    public ArrayList<Integer> call() {
        ArrayList<Integer> results = new ArrayList<Integer>();
        for (long i = 0; i < END_POINT - START_POINT; i++){
            results.add(getPixel((int) ((START_POINT + i) / HEIGHT), (int) ((START_POINT + i) % HEIGHT)));
        }
        return results;
    }
}
