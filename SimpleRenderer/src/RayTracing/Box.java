package RayTracing;

import java.util.TreeMap;

public class Box extends Surface{

    private Vector _center;
    private Vector _scale;
    private Vector _rotation;

    Vector[] _rotatedNormals = null;
    double[] _offsets = null;

    public Intersection findIntersection(Ray ray)
    {
        Intersection intersection = null;

         getRotatedNormals();
         getOffsets(_rotatedNormals);

        double[] intersectionCoefficient = getPlaneIntersections(_rotatedNormals, _offsets, ray);

        TreeMap<Double, Vector> candidates = new TreeMap<Double, Vector>();

        for(int ii = 0; ii < 6; ii ++)
        {
            if(isIntersectionOnBox(_rotatedNormals, ray,  intersectionCoefficient[ii], ii))
            {
                candidates.put(intersectionCoefficient[ii], _rotatedNormals[ii / 2].multiply(Math.pow(-1, ii)));
            }
        }

        if(!candidates.isEmpty())
        {
            //We consider all the objects as one sided. So we need to select only the first surface crossed.
            intersection = new Intersection(candidates.firstEntry().getKey(), candidates.firstEntry().getValue());
        }
        return intersection;
    }

    private void getRotatedNormals()
    {
        if(_rotatedNormals != null)
        {
            return;
        }

        _rotatedNormals = new Vector[3];

        double XX = cosine(_rotation.getZ())*cosine(_rotation.getY());
        double XY = sine(_rotation.getZ())*cosine(_rotation.getY());
        double XZ = - sine(_rotation.getY());
        double YX = cosine(_rotation.getZ())*sine(_rotation.getX())*sine(_rotation.getY()) - sine(_rotation.getZ())*cosine(_rotation.getX());
        double YY = sine(_rotation.getZ())*sine(_rotation.getX())*sine(_rotation.getY()) + cosine(_rotation.getZ())*cosine(_rotation.getX());
        double YZ = sine(_rotation.getX())*cosine(_rotation.getY());
        double ZX = cosine(_rotation.getZ())*sine(_rotation.getY())*cosine(_rotation.getX()) + sine(_rotation.getZ())*sine(_rotation.getX());
        double ZY = sine(_rotation.getZ())*sine(_rotation.getY())*cosine(_rotation.getX()) - cosine(_rotation.getZ())*sine(_rotation.getX());
        double ZZ = cosine(_rotation.getX())*cosine(_rotation.getY());

        _rotatedNormals[0] = new Vector(XX, XY, XZ);
        _rotatedNormals[1] = new Vector(YX, YY, YZ);
        _rotatedNormals[2] = new Vector(ZX, ZY, ZZ);
    }

    private void getOffsets(Vector[] rotatedNormals)
    {
        if(_offsets != null)
        {
            return;
        }

        _offsets = new double[6];

        _offsets[0] = _center.add(rotatedNormals[0].multiply(_scale.getX()/2)).dotProduct(rotatedNormals[0]);
        _offsets[1] = _center.subtract(rotatedNormals[0].multiply(_scale.getX()/2)).dotProduct(rotatedNormals[0]);
        _offsets[2] = _center.add(rotatedNormals[1].multiply(_scale.getY()/2)).dotProduct(rotatedNormals[1]);
        _offsets[3] = _center.subtract(rotatedNormals[1].multiply(_scale.getY()/2)).dotProduct(rotatedNormals[1]);
        _offsets[4] = _center.add(rotatedNormals[2].multiply(_scale.getZ()/2)).dotProduct(rotatedNormals[2]);
        _offsets[5] = _center.subtract(rotatedNormals[2].multiply(_scale.getZ()/2)).dotProduct(rotatedNormals[2]);
    }

    private double[] getPlaneIntersections(Vector[] rotatedNormals, double[] offsets, Ray ray)
    {
        Vector P0 = ray.getOrigin();
        Vector V = ray.getDirection();

        double[] tValues = new double[6];

        for(int ii = 0; ii<6; ii++)
        {
            tValues[ii] = -(P0.dotProduct(rotatedNormals[ii/2]) - offsets[ii])/(V.dotProduct(rotatedNormals[ii/2]));
        }

        return tValues;
    }

    private boolean isIntersectionOnBox(Vector[] rotatedNormals, Ray ray, double t , int ii)
    {
        if(t < 0)
        {
            return false;
        }

        double[] scaleArray = {_scale.getX(), _scale.getY(), _scale.getZ()};

        Vector P0 = ray.getOrigin();
        Vector V = ray.getDirection();
        Vector faceCenter;

        if(ii%2 == 0){
            faceCenter = _center.add(rotatedNormals[ii/2].multiply(scaleArray[ii/2]));
        }else{
            faceCenter = _center.subtract(rotatedNormals[ii / 2].multiply(scaleArray[ii / 2]));
        }

        Vector intersectionPointFromCenter = P0.add(V.multiply(t)).subtract(faceCenter);

        double projectionOnFirstAxis = intersectionPointFromCenter.dotProduct(rotatedNormals[(ii/2 + 1)%3]);
        double projectionOnSecondAxis = intersectionPointFromCenter.dotProduct(rotatedNormals[(ii/2 + 2)%3]);

        if(Math.abs(projectionOnFirstAxis) <= scaleArray[(ii/2 + 1)%3]/2 && Math.abs(projectionOnSecondAxis) <= scaleArray[(ii/2 + 2)%3]/2) {
            return true;
        }

        return false;
    }

    public void setCenter(Vector center)
    {
        _center = center;
    }

    public void setCenter(String x, String y, String z)
    {
        Vector center = new Vector(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
        setCenter(center);
    }

    public void setScale(Vector scale)
    {
        _scale = scale;
    }

    public void setScale(String x, String y, String z)
    {
        Vector scale = new Vector(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
        setScale(scale);
    }

    public void setRotation(Vector rotation)
    {
        _rotation = rotation;
    }

    public void setRotation(String x, String y, String z)
    {
        Vector rotation = new Vector(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
        setRotation(rotation);
    }

    public void setMaterial(Material material)
    {
        super.setMaterial(material);
    }

    public double cosine(double angleInDegrees)
    {
        return Math.cos(Math.toRadians(angleInDegrees));
    }

    public double sine(double angleInDegrees)
    {
        return Math.sin(Math.toRadians(angleInDegrees));
    }
}
