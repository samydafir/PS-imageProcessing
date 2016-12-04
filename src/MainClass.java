import org.opencv.core.Core;

public class MainClass {


	public static void main(String[] args) {
		
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		EdgeHistogram eh = new EdgeHistogram("examples/edge/src1.jpg", 5, 1000, 10, 1);
		eh.calcHistogram();
		eh.evaluate(100, 500, 10);
	}

}
