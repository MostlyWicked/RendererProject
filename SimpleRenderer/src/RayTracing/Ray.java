package RayTracing;

public class Ray {

    private Vector _origin;
    private Vector _direction;

    public Ray(Vector origin, Vector direction)
    {
        _origin = origin;
        _direction = direction;
    }

    public Vector getOrigin()
    {
        return _origin;
    }

    public Vector getDirection()
    {
        return _direction;
    }

    public boolean isEqual(Ray otherRay)
    {
        if(_origin.isEqual(otherRay.getOrigin()) && _direction.isEqual(otherRay.getDirection()))
        {
            return true;
        }

        return false;
    }

}
