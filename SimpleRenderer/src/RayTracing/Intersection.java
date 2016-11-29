package RayTracing;

public class Intersection implements Comparable<Intersection>{

    private double _coefficient; //distance from origin of the Ray to intersection point
    private Vector _normal;

    public Intersection(double coefficient, Vector normal)
    {
        _coefficient = coefficient;
        _normal = normal;
    }

    public double getCoefficient()
    {
        return _coefficient;
    }

    public Vector getNormal()
    {
        return _normal;
    }

    @Override
    public int compareTo(Intersection intersection) {
        if(intersection.getCoefficient() > _coefficient)
        {
            return -1;
        }
        else if(intersection.getCoefficient() < _coefficient)
        {
            return 1;
        }
        return 0;
    }

}