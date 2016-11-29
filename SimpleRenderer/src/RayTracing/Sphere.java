package RayTracing;

public class Sphere extends Surface{

    private Vector _center;
    private double _radius;

    public Intersection findIntersection(Ray ray)
    {
        Intersection intersection = null;

        Vector P0 = ray.getOrigin();
        Vector V = ray.getDirection();
        Vector L = _center.subtract(P0);

        double tca = L.dotProduct(V);

        if(tca < 0 ){
            return intersection;
        }

        double d2 = L.dotProduct(L) -  tca*tca;

        if(_radius*_radius < d2 ){
            return intersection;
        }

        double thc = Math.sqrt(_radius*_radius - d2);

        //We consider all the objects as one sided. So we need to select the first surface crossed.
        double intersectionCoefficient = Math.min(tca - thc, tca + thc);
        Vector intersectionLocation = P0.add(V.multiply(intersectionCoefficient));

        return new Intersection(intersectionCoefficient, intersectionLocation.subtract(_center).normalize());
    }

    public void setCenter(double x, double y, double z)
    {
        _center = new Vector(x, y, z);
    }

    public void setCenter(String x, String y, String z)
    {
        setCenter(Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(z));
    }

    public void setRadius(double radius)
    {
        _radius = radius;
    }

    public void setRadius(String radius)
    {
        setRadius(Double.parseDouble(radius));
    }

    public void setMaterial(Material material)
    {
        super.setMaterial(material);
    }
}