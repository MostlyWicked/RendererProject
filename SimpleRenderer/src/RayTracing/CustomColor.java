package RayTracing;


import java.awt.*;

public class CustomColor {

    Vector _rgb;

    public CustomColor(double r, double g, double b) {

        if (r < 0 || r > 1) {
            System.out.println(String.format("Warning: color out of [0 1] range (red %f)", r));
            r = Math.max(r, 0);
            r = Math.min(r, 1);
        }

        if (g < 0 || g > 1) {
            System.out.println(String.format("Warning: color out of [0 1] range (green %f)", g));
            g = Math.max(g, 0);
            g = Math.min(g, 1);
        }

        if (b < 0 || b > 1) {
            System.out.println(String.format("Warning: color out of [0 1] range (blue %f)", b));
            b = Math.max(b, 0);
            b = Math.min(b, 1);
        }
        _rgb = new Vector(r, g, b);
    }

    public CustomColor(Vector rgb) {
        _rgb = new Vector(rgb);
    }

    public CustomColor(CustomColor color) {
        _rgb = new Vector(color.getRgb());
    }

    public CustomColor(Color color) {
        _rgb = new Vector(((double)color.getRed())/255,((double) color.getGreen())/ 255, (double)(color.getBlue())/255);
    }

    public Vector getRgb()
    {
        return _rgb;
    }

    public CustomColor multiply(CustomColor other)
    {
        return new CustomColor(_rgb.multiply(other.getRgb()));
    }

    public CustomColor multiply(double factor)
    {
        return new CustomColor(_rgb.multiply(factor));
    }

    public CustomColor add(CustomColor color)
    {
        return new CustomColor(_rgb.add(color.getRgb()));
    }

    public CustomColor clip()
    {
        double r = Math.max(Math.min(_rgb.getX(), 1), 0);
        double g = Math.max(Math.min(_rgb.getY(), 1), 0);
        double b = Math.max(Math.min(_rgb.getZ(), 1), 0);

        return new CustomColor(new Vector(r, g ,b ));
    }

    public Color getJavaColor()
    {
        return new Color((float)_rgb.getX(),(float) _rgb.getY(),(float) _rgb.getZ());
    }

    public String toString(){
        return this.getRgb().toString();
    }
}



