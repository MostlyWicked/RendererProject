package RayTracing;

public class Plane extends Surface{

    private Vector _normal;
    private float _offset;

    public Intersection findIntersection(Ray ray)
    {
        Intersection intersection = null;

        Vector P0 = ray.getOrigin();
        Vector V = ray.getDirection();

        double t = -(P0.dotProduct(_normal) - _offset)/(V.dotProduct(_normal));

        if(t > 0)
        {
            intersection = new Intersection(t, _normal);
        }

        return intersection;
    }

    public void setNormal(Vector normal)
    {
        _normal = normal;
    }

    public void setNormal(String x, String y, String z)
    {
        Vector normal = new Vector(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
        setNormal(normal);
    }

    public void setOffset(int offset)
    {
        _offset = offset;
    }

    public void setOffset(String offset)
    {
        setOffset(Integer.parseInt(offset));
    }

}
