import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Mandelbrot extends JFrame {

    private final int MAX_ITER = 570;
    private final double ZOOM = 150;
    private BufferedImage I;

    private Mandelbrot(int numberOfThreads, int numberOfTasks) throws ExecutionException, InterruptedException {
        super("Mandelbrot Set");
        setBounds(100, 100, 800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        long width = getWidth();
        long height = getHeight();
        ExecutorService pool = Executors.newFixedThreadPool(numberOfThreads);
        ArrayList<Future<ArrayList<Integer>>> results = new ArrayList<Future<ArrayList<Integer>>>();

        for (int i = 0; i < numberOfTasks; i++) {
            Callable<ArrayList<Integer>> mandelbrotProcessor = new MandelbrotProcessor((height * width) * i / numberOfTasks,
                    (height * width) * (i + 1) / numberOfTasks, height, ZOOM, MAX_ITER);
            Future<ArrayList<Integer>> future = pool.submit(mandelbrotProcessor);
            results.add(future);
        }
        for (long i = 0; i < numberOfTasks; i++) {
            ArrayList<Integer> batch = results.get((int) i).get();
            long coord = height * width * i / (long) numberOfTasks;
            for (long r = 0; r < batch.size(); r++) {
                int iter = batch.get((int) r);
                I.setRGB((int) ((coord + r) / height), (int) ((coord + r) % height), iter | (iter << 8));

            }
        }
    }


    @Override
    public void paint(Graphics g) {
        g.drawImage(I, 0, 0, this);
    }

    public static void main(String[] args) throws InterruptedException {
        //Thread.sleep(5000);
        try {
            for (int i = 0; i < 1; i++) {
                long start = System.nanoTime();
                new Mandelbrot(8, 600 * 800).setVisible(true);
                System.out.println(System.nanoTime() - start);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}