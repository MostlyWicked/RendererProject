package RayTracing;

import java.awt.*;

public class Light {

    private Vector _position;
    private CustomColor _color;
    private float _specularIntensity;
    private float _shadowIntensity;
    private float _lightRadius;

    public Vector getPosition()
    {
        return _position;
    }

    public CustomColor getColor()
    {
        return _color;
    }

    public float getSpecularIntensity()
    {
        return _specularIntensity;
    }

    public float getShadowIntensity()
    {
        return _shadowIntensity;
    }

    public float getLightRadius()
    {
        return _lightRadius;
    }

    public void setPosition(Vector position)
    {
        _position = position;
    }

    public void setPosition(String x, String y, String z)
    {
        Vector position = new Vector(Float.parseFloat(x), Float.parseFloat(y), Float.parseFloat(z));
        setPosition(position);
    }

    public void setColor(CustomColor color)
    {
        _color = color;
    }

    public void setColor(String r, String g, String b)
    {
        CustomColor color = new CustomColor(Float.parseFloat(r), Float.parseFloat(g), Float.parseFloat(b));
        setColor(color);
    }

    public void setSpecularIntensity(float specularIntensity)
    {
        _specularIntensity = specularIntensity;
    }

    public void setSpecularIntensity(String specularIntensity)
    {
        setSpecularIntensity(Float.parseFloat(specularIntensity));
    }

    public void setShadowIntensity(float shadowIntensity)
    {
        _shadowIntensity = shadowIntensity;
    }

    public void setShadowIntensity(String shadowIntensity)
    {
        setShadowIntensity(Float.parseFloat(shadowIntensity));
    }

    public void setLightRadius(float lightRadius)
    {
        _lightRadius = lightRadius;
    }

    public void setLightRadius(String lightRadius)
    {
        setLightRadius(Float.parseFloat(lightRadius));
    }
}