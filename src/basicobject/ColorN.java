package basicobject;

import java.io.IOException;
import javafx.scene.paint.Color;

public class ColorN implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private transient Color color;

    public ColorN(Color c) {
        this.color = c;
    }

    public Color getColor() {
        return color;
    }

    private void writeObject(java.io.ObjectOutputStream out)
    throws IOException {
        out.writeDouble(color.getRed());
        out.writeDouble(color.getGreen());
        out.writeDouble(color.getBlue());
        out.writeDouble(color.getOpacity());
    }

    private void readObject(java.io.ObjectInputStream in)
    throws IOException, ClassNotFoundException {
        color = new Color(in.readDouble(), in.readDouble(),
                          in.readDouble(), in.readDouble());
    }

}
