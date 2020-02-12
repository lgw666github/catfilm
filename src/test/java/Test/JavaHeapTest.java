package Test;

public class JavaHeapTest {
    public final static int OUTOFMEMORY = 200000000;
    private String oom;
    private int length;

    StringBuffer xxxtempOOM = new StringBuffer();

    public JavaHeapTest(int leng) {
        this.length = leng;

        int i = 0;
        while (i < leng) {
            i++;
            try {
                xxxtempOOM.append("a");
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                break;
            }
        }
        this.oom = xxxtempOOM.toString();

    }

    public String getOom() {
        return oom;
    }

    public int getLength() {
        return length;
    }

    public static void main(String[] args) {
        JavaHeapTest javaHeapTest = new JavaHeapTest(OUTOFMEMORY);
        System.out.println(javaHeapTest.getOom().length());
    }
}
