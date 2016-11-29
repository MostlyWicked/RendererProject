package RayTracing;

public class Camera {

    private Vector _position;
    private Vector _lookAtPoint;
    private Vector _upVector;
    private float _screenDistance;
    private float _screenWidth;

    private Vector _correctedUpVector = null;
    private Vector _direction = null;

    public void setPosition(float x, float y, float z)
    {
        _position = new Vector(x, y, z);
    }

    public void setPosition(String x, String y, String z)
    {
        setPosition(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
    }

    public Vector getPosition()
    {
        return _position;
    }

    public void setLookAtPoint(float x, float y, float z)
    {
        _lookAtPoint = new Vector(x, y, z);
    }

    public void setLookAtPoint(String x, String y, String z)
    {
        setLookAtPoint(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
    }

    public void setUpVector(float x, float y, float z)
    {
        _upVector = new Vector(x, y, z);
    }

    public void setUpVector(String x, String y, String z)
    {
        setUpVector(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
    }

    public void setScreenDistance(float screenDistance)
    {
        _screenDistance = screenDistance;
    }

    public void setScreenDistance(String screenDistance)
    {
        setScreenDistance(Float.parseFloat(screenDistance));
    }

    public void setScreenWidth(float screenWidth)
    {
        _screenWidth = screenWidth;
    }

    public void setScreenWidth(String screenWidth)
    {
        setScreenWidth(Float.parseFloat(screenWidth));
    }

    public float getScreenWidth()
    {
        return _screenWidth;
    }

    /**
     * Gets the normalized camera direction.
     * @return The normalized camera direction.
     */
    public Vector getDirection()
    {
        if(_direction != null)
        {
            return new Vector(_direction);
        }
        Vector direction = _lookAtPoint.subtract(_position);
        Vector normalizedDirection = direction.multiply(1/direction.norm());
        _direction = new Vector(normalizedDirection);
        return normalizedDirection;
    }

    /**
     * Calculates the up vector of the camera, corrected so that it is normal to the direction.
     * @param direction The normalized camera direction.
     * @return The corrected up vector of the camera.
     */
    public Vector getUpVector(Vector direction)
    {
        if(_correctedUpVector != null)
        {
            return new Vector(_correctedUpVector);
        }
        // Projection of the up vector on the plan normal to the direction vector.
        Vector projectedUpVector = _upVector.subtract(direction.multiply(_upVector.dotProduct(direction)));
        Vector normalizedUpVector = projectedUpVector.multiply(1/projectedUpVector.norm());
        _correctedUpVector = new Vector(normalizedUpVector);
        return normalizedUpVector;
    }

    public float getScreenDistance()
    {
        return _screenDistance;
    }

}