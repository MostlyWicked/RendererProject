package RayTracing;

public class Vector {

    private double _x;
    private double _y;
    private double _z;

    public Vector(double x, double y, double z)
    {
        _x = x;
        _y = y;
        _z = z;
    }

    public Vector(Vector vector)
    {
        _x = vector.getX();
        _y = vector.getY();
        _z = vector.getZ();
    }

    public double dotProduct(Vector otherVector) {
        return _x*otherVector.getX() + _y*otherVector.getY() + _z*otherVector.getZ();
    }

    public double norm()
    {
        return Math.sqrt((this.dotProduct(this)));
    }

    public Vector normalize()
    {
        return this.multiply(1/this.norm());
    }

    public Vector crossProduct(Vector otherVector) {
        double s1 = _y*otherVector.getZ() - _z*otherVector.getY();
        double s2 = _z*otherVector.getX() - _x*otherVector.getZ();
        double s3 = _x*otherVector.getY() - _y*otherVector.getX();
        return new Vector(s1, s2, s3);
    }

    public Vector add(Vector otherVector) {
        return new Vector(_x + otherVector.getX(), _y + otherVector.getY(), _z + otherVector.getZ());
    }

    public Vector subtract(Vector otherVector)
    {
        return this.add(otherVector.multiply(-1));
    }

    public Vector multiply(Vector otherVector) {
        return new Vector(otherVector.getX()*_x, otherVector.getY()*_y, otherVector.getZ()*_z);
    }

    public Vector multiply(double scalingFactor) {
        return new Vector(scalingFactor*_x, scalingFactor*_y, scalingFactor*_z);
    }

    public double getX()
    {
        return _x;
    }

    public double getY()
    {
        return _y;
    }

    public double getZ()
    {
        return _z;
    }

    public boolean isEqual(Vector otherVector)
    {
        if(_x == otherVector.getX() && _y == otherVector.getY() && _z == otherVector.getZ())
        {
            return true;
        }
        return false;
    }

    public String toString(){
        return "X: "+this._x+" Y: "+this._y+" Z: "+this._z;
    }
}